package com.example.exoest.songrankmaker.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exoest.songrankmaker.R;
import com.example.exoest.songrankmaker.controller.DBHandler;
import com.example.exoest.songrankmaker.model.Ranking;
import com.example.exoest.songrankmaker.model.RankingSong;
import com.example.exoest.songrankmaker.model.Song;

import java.util.ArrayList;
import java.util.List;

public class ViewRankingActivity extends AppCompatActivity {
    ListView listViewViewRanking;
    List<RankingSong> retrievedRankingSongList;
    List<RankingSong> rearrangedRankingSongList;
    Ranking rankingForView;
    MenuItem menuItemRearrangeAll;
    MenuItem menuItemFinishRearranging;
    MenuItem menuItemRearrangeReset;
    int rankCounter;
    int rankingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ranking);

        Bundle bundle = getIntent().getExtras();
        DBHandler db = new DBHandler(this, null, null, 1);
        rankingId = Integer.parseInt(bundle.getString("viewRankingId"));
        rankingForView = db.retrieveRankingById(rankingId);
        setTitle(rankingForView.get_name());

        listViewViewRanking = (ListView) findViewById(R.id.listViewViewRanking);
        refreshListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_ranking, menu);
        menuItemRearrangeAll = (MenuItem) menu.findItem(R.id.menuItemRearrangeAll);
        menuItemFinishRearranging = (MenuItem) menu.findItem(R.id.menuItemFinishRearranging);
        menuItemRearrangeReset = (MenuItem) menu.findItem(R.id.menuItemRearrangeReset);
        menuItemFinishRearranging.setVisible(false);
        menuItemRearrangeReset.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemRearrangeAll:
                rearrangeAllSong();
                return true;
            case R.id.menuItemFinishRearranging:
                finishRearranging();
                return true;
            case R.id.menuItemRearrangeReset:
                resetRearrange();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_float_view_ranking, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menuItemRemoveFromRanking:
                displayDeleteConfirmation(info);
                return true;
            case R.id.menuItemSearchInYoutube:
                searchInYoutube(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void refreshListView(){
        DBHandler db = new DBHandler(this, null, null, 1);
        retrievedRankingSongList = db.retrieveRankingSongByRankingId(rankingForView.get_id(), false);
        retrievedRankingSongList.addAll(db.retrieveRankingSongByRankingId(rankingForView.get_id(), true));
        if (!retrievedRankingSongList.isEmpty()){
            ListAdapter listAdapter = new ViewRankingListAdapter(this, R.layout.custom_row_view_ranking, retrievedRankingSongList);
            listViewViewRanking.setAdapter(listAdapter);
            registerForContextMenu(findViewById(R.id.listViewViewRanking));
        }
    }

    public void rearrangeAllSong(){
        menuItemRearrangeAll.setVisible(false);
        menuItemFinishRearranging.setVisible(true);
        menuItemRearrangeReset.setVisible(true);
        menuItemFinishRearranging.setEnabled(false);
        Toast.makeText(this, getString(R.string.activity_view_ranking_toast_rearrange_all), Toast.LENGTH_LONG).show();
        rearrangedRankingSongList = new ArrayList<RankingSong>();
        rankCounter = 1;
        if (!retrievedRankingSongList.isEmpty()){
            final ViewRankingRearrangeListAdapter listAdapter = new ViewRankingRearrangeListAdapter(this, R.layout.custom_row_view_ranking, retrievedRankingSongList);
            listViewViewRanking.setAdapter(listAdapter);
            listViewViewRanking.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView textViewViewRankingRankNumber = (TextView) view.findViewById(R.id.textViewViewRankingRankNumber);
                            if (textViewViewRankingRankNumber.getText().toString().equals("")){
                                if (rankCounter <= retrievedRankingSongList.size()){
                                    textViewViewRankingRankNumber.setText(String.valueOf(rankCounter));
                                    RankingSong selectedRankingSong = retrievedRankingSongList.get(position);
                                    selectedRankingSong.set_rank(rankCounter);
                                    selectedRankingSong.set_ranking(new Ranking(rankingId, null));
                                    rearrangedRankingSongList.add(selectedRankingSong);
                                    listAdapter.setTempRankUponClick(position, rankCounter);
                                    rankCounter++;
                                    if (rankCounter > retrievedRankingSongList.size())
                                        menuItemFinishRearranging.setEnabled(true);
                                }
                            }
                        }
                    }
            );
        }

    }

    public void finishRearranging(){
        if (!rearrangedRankingSongList.isEmpty()){
            DBHandler db = new DBHandler(this, null, null, 1);
            for (RankingSong rs : rearrangedRankingSongList){
                db.updateRankByRankingIdAndSongId(rs.get_ranking(), rs.get_song(), rs.get_rank());
            }
            menuItemRearrangeAll.setVisible(true);
            menuItemFinishRearranging.setVisible(false);
            menuItemRearrangeReset.setVisible(false);
            Toast.makeText(this, getString(R.string.activity_view_ranking_toast_finish_rearranging), Toast.LENGTH_LONG).show();
            refreshListView();
        }
    }

    public void resetRearrange(){
        rankCounter = 1;
        rearrangedRankingSongList.clear();
        menuItemFinishRearranging.setEnabled(false);
        for (int i = 0; i < retrievedRankingSongList.size() ; i++){
            View customView = listViewViewRanking.getChildAt(i);
            ((TextView) customView.findViewById(R.id.textViewViewRankingRankNumber)).setText("");
        }
    }

    public void displayDeleteConfirmation(final AdapterView.AdapterContextMenuInfo info){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle(getString(R.string.activity_view_ranking_remove_from_ranking_dialog_title));
        deleteDialog.setMessage(getString(R.string.activity_view_ranking_remove_from_ranking_dialog_message));
        deleteDialog.setIcon(android.R.drawable.ic_menu_delete);
        deleteDialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeFromRanking(info);
                dialog.dismiss();
            }
        });
        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog createDialog = deleteDialog.create();
        createDialog.show();
    }

    public void removeFromRanking(AdapterView.AdapterContextMenuInfo info){
        DBHandler db = new DBHandler(this, null, null, 1);
        RankingSong rankingSongForRemove = retrievedRankingSongList.get(info.position);
        rankingSongForRemove.set_ranking(new Ranking(rankingId, null));
        if (rankingSongForRemove.get_rank() != 0)
            db.deleteRankingSong(rankingSongForRemove, true);
        else
            db.deleteRankingSong(rankingSongForRemove, false);
        Toast.makeText(this, "\"" + rankingSongForRemove.get_song().get_name() + "\" " + getString(R.string.activity_view_ranking_remove_from_ranking_toast_after), Toast.LENGTH_LONG).show();
        refreshListView();
    }

    public void searchInYoutube(AdapterView.AdapterContextMenuInfo info){
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage("com.google.android.youtube");
        Song selectedSong = retrievedRankingSongList.get(info.position).get_song();
        String searchQuery = selectedSong.get_name() + " " + selectedSong.get_artist();
        intent.putExtra("query", searchQuery);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
