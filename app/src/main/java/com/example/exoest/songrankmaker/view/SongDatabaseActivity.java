package com.example.exoest.songrankmaker.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.exoest.songrankmaker.R;
import com.example.exoest.songrankmaker.controller.DBHandler;
import com.example.exoest.songrankmaker.model.Ranking;
import com.example.exoest.songrankmaker.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongDatabaseActivity extends AppCompatActivity {
    ListView listViewSongDatabase;
    List<Song> retrievedSongList = new ArrayList<Song>();
    MenuItem menuItemAddSong;
    MenuItem menuItemAssignRank;
    MenuItem menuItemFinishChecking;
    MenuItem menuItemAssignRankBack;
    SongDatabaseListAdapter songDatabaseAdapter;
    private static boolean isSortArtist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_database);

        listViewSongDatabase = (ListView) findViewById(R.id.listViewSongDatabase);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isSortArtist = sharedPreferences.getBoolean("sortArtist", false);

        refreshList(isSortArtist);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_song_database, menu);
        menuItemAddSong = menu.findItem(R.id.menuItemAddSong);
        menuItemAssignRank = menu.findItem(R.id.menuItemAssignRank);
        menuItemFinishChecking = menu.findItem(R.id.menuItemFinishChecking);
        menuItemAssignRankBack = menu.findItem(R.id.menuItemAssignRankBack);
        menuItemFinishChecking.setVisible(false);
        menuItemAssignRankBack.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemAddSong:
                Intent intent = new Intent(this, AddSongActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuItemAssignRank:
                changeToCheckableListForAssignRank();
                return true;
            case R.id.menuItemFinishChecking:
                displayAssignToRankingDialog();
                return true;
            case R.id.menuItemAssignRankBack:
                backFromAssignRank();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_float_song_database, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menuItemEdit:
                editSong(info);
                return true;
            case R.id.menuItemDelete:
                displayDeleteConfirmation(info);
                return true;
            case R.id.menuItemSearchInYoutube:
                searchInYoutube(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void refreshList(boolean isArtistSort){
        DBHandler db = new DBHandler(this, null, null, 1);
        retrievedSongList = db.retrieveAllSong(isArtistSort);
        if (!retrievedSongList.isEmpty()){
            songDatabaseAdapter = new SongDatabaseListAdapter(this, R.layout.custom_row_song_database, retrievedSongList);
//            final ListView listViewSongDatabase = (ListView) findViewById(R.id.listViewSongDatabase);
            listViewSongDatabase.setAdapter(songDatabaseAdapter);
            registerForContextMenu(findViewById(R.id.listViewSongDatabase));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList(isSortArtist);
    }

    public void changeToCheckableListForAssignRank(){
        menuItemFinishChecking.setEnabled(false);
        menuItemAddSong.setVisible(false);
        menuItemAssignRank.setVisible(false);
        menuItemFinishChecking.setVisible(true);
        menuItemAssignRankBack.setVisible(true);
        unregisterForContextMenu(listViewSongDatabase);
        listViewSongDatabase.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewSongDatabase.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songDatabaseAdapter.changeSelectionUponSelected(position);
                if (songDatabaseAdapter.getItem(position).isSelected())
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_custom_row_song_database_darkblue));
                else
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_custom_row_song_database_lightblue));
                listViewSongDatabase.setItemChecked(position, songDatabaseAdapter.getItem(position).isSelected());
                if (listViewSongDatabase.getCheckedItemCount() != 0)
                    menuItemFinishChecking.setEnabled(true);
                else
                    menuItemFinishChecking.setEnabled(false);
            }
        });
    }

    public void displayAssignToRankingDialog(){
        final AlertDialog.Builder assignToRankingDialog = new AlertDialog.Builder(this);
        assignToRankingDialog.setTitle("Assign Song to Ranking");
        assignToRankingDialog.setMessage("Select ranking :");

        final Spinner spinnerRanking = new Spinner(this);
        final List<String> spinnerList = new ArrayList<>();
        DBHandler db = new DBHandler(this, null, null, 1);
        final List<Ranking> rankingList = db.retrieveAllRanking(true);

        spinnerList.add("= Choose a ranking =");
        for (Ranking r : rankingList){
            spinnerList.add(r.get_name());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinnerRanking.setAdapter(adapter);
        spinnerRanking.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        assignToRankingDialog.setView(spinnerRanking);
        assignToRankingDialog.setCancelable(false);
        assignToRankingDialog.setPositiveButton("Assign", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (spinnerRanking.getSelectedItemPosition() != 0){
                    assignCheckedToRanking(rankingList.get(spinnerRanking.getSelectedItemPosition()-1));
//                    Toast.makeText(SongDatabaseActivity.this, "You have already assigned to this ranking.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else
                    Toast.makeText(SongDatabaseActivity.this, "Please select a ranking.", Toast.LENGTH_LONG).show();
            }
        });
        assignToRankingDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog createDialog = assignToRankingDialog.create();
        createDialog.show();
    }

    public void assignCheckedToRanking(Ranking rankingAssignTo){
        SparseBooleanArray sba = listViewSongDatabase.getCheckedItemPositions();
        List<Song> songsForAssign = new ArrayList<>();
        DBHandler db = new DBHandler(this, null, null, 1);
        for (int i = 0; i < sba.size(); i++){
            songsForAssign.add(retrievedSongList.get(sba.keyAt(i)));
        }
        db.createMultipleRankingSong(songsForAssign, rankingAssignTo);
        Toast.makeText(this, getString(R.string.activity_song_database_assign_rank_done_toast), Toast.LENGTH_LONG).show();
        backFromAssignRank();
    }

    public void backFromAssignRank(){
        refreshList(isSortArtist);
        listViewSongDatabase.setOnItemClickListener(null);
        menuItemAddSong.setVisible(true);
        menuItemAssignRank.setVisible(true);
        menuItemFinishChecking.setVisible(false);
        menuItemAssignRankBack.setVisible(false);
    }

    public void editSong(AdapterView.AdapterContextMenuInfo info){
        Intent intent = new Intent(this, EditSongActivity.class);
        Song songForEdit = retrievedSongList.get(info.position);
        intent.putExtra("songid", String.valueOf(songForEdit.get_id()));
        startActivity(intent);
    }

    public void displayDeleteConfirmation(final AdapterView.AdapterContextMenuInfo info){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("Delete Song");
        deleteDialog.setMessage(getString(R.string.activity_song_database_delete_song_dialog_message));
        deleteDialog.setIcon(android.R.drawable.ic_menu_delete);
        deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteSong(info);
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

    public void deleteSong(AdapterView.AdapterContextMenuInfo info){
        Song songForDelete = retrievedSongList.get(info.position);
        DBHandler db = new DBHandler(this, null, null, 1);
        db.deleteSong(songForDelete.get_name());
        Toast.makeText(this, "\"" + songForDelete.get_name() + "\" " + getString(R.string.activity_song_database_delete_song_toast_after), Toast.LENGTH_LONG).show();
        refreshList(isSortArtist);
    }

    public void searchInYoutube(AdapterView.AdapterContextMenuInfo info){
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage("com.google.android.youtube");
        Song selectedSong = retrievedSongList.get(info.position);
        String searchQuery = selectedSong.get_name() + " " + selectedSong.get_artist();
        intent.putExtra("query", searchQuery);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
