package com.example.exoest.songrankmaker.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.exoest.songrankmaker.R;
import com.example.exoest.songrankmaker.controller.DBHandler;
import com.example.exoest.songrankmaker.model.Ranking;
import com.example.exoest.songrankmaker.model.RankingSong;
import com.example.exoest.songrankmaker.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongDatabaseActivity extends AppCompatActivity {
    ListView listViewSongDatabase;
    List<Song> retrievedSongList = new ArrayList<Song>();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemAddSong:
                Intent intent = new Intent(this, AddSongActivity.class);
                startActivity(intent);
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
            case R.id.menuItemAssignRank:
                displayAssignToRankingDialog(info);
                return true;
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
            ListAdapter songDatabaseAdapter = new SongDatabaseListAdapter(this, R.layout.custom_row_song_database, retrievedSongList);
//            final ListView listViewSongDatabase = (ListView) findViewById(R.id.listViewSongDatabase);
            listViewSongDatabase.setAdapter(songDatabaseAdapter);
            registerForContextMenu(findViewById(R.id.listViewSongDatabase));
            listViewSongDatabase.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            listViewSongDatabase.showContextMenuForChild(view);
                        }
                    }
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList(isSortArtist);
    }

    public void displayAssignToRankingDialog(final AdapterView.AdapterContextMenuInfo info){
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

        assignToRankingDialog.setPositiveButton("Assign", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (spinnerRanking.getSelectedItemPosition() != 0){
                    assignToRanking(info, rankingList.get(spinnerRanking.getSelectedItemPosition()-1));
                    dialog.dismiss();
                } else {
                    Toast.makeText(SongDatabaseActivity.this, "Please select a ranking.", Toast.LENGTH_LONG).show();
                }

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

    public void assignToRanking(AdapterView.AdapterContextMenuInfo info, Ranking rankingAssignTo){
        Song songAssignFor = retrievedSongList.get(info.position);
        DBHandler db = new DBHandler(this, null, null, 1);
        RankingSong newRankingSong = new RankingSong(rankingAssignTo, songAssignFor, 0);
        db.createRankingSong(newRankingSong);
        Toast.makeText(this, "\"" + songAssignFor.get_name() + "\" has been assigned to \"" + rankingAssignTo.get_name() + "\".", Toast.LENGTH_LONG).show();
        refreshList(isSortArtist);
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
        Toast.makeText(this, "\"" + songForDelete.get_name() + "\" has been deleted.", Toast.LENGTH_LONG).show();
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
