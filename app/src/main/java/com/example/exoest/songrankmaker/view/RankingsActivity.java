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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.exoest.songrankmaker.R;
import com.example.exoest.songrankmaker.controller.DBHandler;
import com.example.exoest.songrankmaker.model.Ranking;

import java.util.ArrayList;
import java.util.List;

public class RankingsActivity extends AppCompatActivity {
    ListView listViewRankings;
    List<Ranking> retrievedRankingList = new ArrayList<Ranking>();
    private static boolean isRankingNameSort = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        listViewRankings = (ListView) findViewById(R.id.listViewRankings);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isRankingNameSort = sharedPreferences.getBoolean("sortRankingName", false);

        refreshRankingList(isRankingNameSort);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rankings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemAddRanking:
                Intent intent = new Intent(this, AddRankingActivity.class);
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
        inflater.inflate(R.menu.menu_float_rankings, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menuItemShowRanking:
                showRanking(info);
                return true;
            case R.id.menuItemEditRankingName:
                displayEditDialog(info);
                return true;
            case R.id.menuItemDeleteRanking:
                displayDeleteConfirmation(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void refreshRankingList(boolean isNameSort){
        DBHandler db = new DBHandler(this, null, null, 1);
        retrievedRankingList =  db.retrieveAllRanking(isNameSort);
        if (!retrievedRankingList.isEmpty()){
            ListAdapter rankingsAdapter = new RankingsListAdapter(this, R.layout.custom_row_rankings, retrievedRankingList);
            listViewRankings.setAdapter(rankingsAdapter);
            registerForContextMenu(findViewById(R.id.listViewRankings));
            listViewRankings.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            listViewRankings.showContextMenuForChild(view);
                        }
                    }
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRankingList(isRankingNameSort);
    }

    public void showRanking(AdapterView.AdapterContextMenuInfo info){
        Intent intent = new Intent(this, ViewRankingActivity.class);
        intent.putExtra("viewRankingId", String.valueOf(retrievedRankingList.get(info.position).get_id()));
        startActivity(intent);
    }

    public void displayEditDialog(final AdapterView.AdapterContextMenuInfo info){
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
        editDialog.setTitle("Edit Ranking Name");
        editDialog.setMessage("Please enter new ranking name.");

        final EditText editTextInput = new EditText(this);
        editTextInput.setText(retrievedRankingList.get(info.position).get_name());
        editTextInput.selectAll();
        editTextInput.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        editDialog.setView(editTextInput);

        editDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editRankingName(info, editTextInput.getText().toString());
                dialog.dismiss();
            }
        });
        editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog createDialog = editDialog.create();
        createDialog.show();
    }

    public void editRankingName(AdapterView.AdapterContextMenuInfo info, String newRankingName){
        Ranking rankingForEdit = retrievedRankingList.get(info.position);
        DBHandler db = new DBHandler(this, null, null, 1);
        Ranking newRanking = new Ranking(newRankingName);
        db.updateRankingById(rankingForEdit.get_id(), newRanking);
        Toast.makeText(this, "\"" + rankingForEdit.get_name() + "\" info has been updated to \"" + newRankingName + "\".", Toast.LENGTH_LONG).show();
        refreshRankingList(isRankingNameSort);
    }

    public void displayDeleteConfirmation(final AdapterView.AdapterContextMenuInfo info){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("Delete Ranking");
        deleteDialog.setMessage(getString(R.string.activity_rankings_delete_ranking_dialog_message));
        deleteDialog.setIcon(android.R.drawable.ic_menu_delete);
        deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteRanking(info);
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

    public void deleteRanking(AdapterView.AdapterContextMenuInfo info){
        Ranking rankingForDelete = retrievedRankingList.get(info.position);
        DBHandler db = new DBHandler(this, null, null, 1);
        db.deleteRanking(rankingForDelete.get_name());
        Toast.makeText(this, "\"" + rankingForDelete.get_name() + "\" has been deleted.", Toast.LENGTH_LONG).show();
        refreshRankingList(isRankingNameSort);
    }
}
