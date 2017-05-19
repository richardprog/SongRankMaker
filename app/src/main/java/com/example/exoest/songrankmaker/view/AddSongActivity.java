package com.example.exoest.songrankmaker.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.exoest.songrankmaker.R;
import com.example.exoest.songrankmaker.controller.DBHandler;
import com.example.exoest.songrankmaker.model.Song;

import java.util.ArrayList;
import java.util.List;

public class AddSongActivity extends AppCompatActivity {
    private EditText editTextSongTitle;
    private EditText editTextSongArtist;
    Spinner spinnerExistingArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        editTextSongTitle = (EditText) findViewById(R.id.editTextSongTitle);
        editTextSongArtist = (EditText) findViewById(R.id.editTextSongArtist);
        spinnerExistingArtist = (Spinner) findViewById(R.id.spinnerExistingArtist);

        final List<String> spinnerList = new ArrayList<String>();
        DBHandler db = new DBHandler(this, null, null, 1);
        List<String> songList = db.retrieveAllSongArtistDistinct(true);

        spinnerList.add(getString(R.string.activity_add_song_spinner_existing_artist_default));
        for (String songArtistEach : songList){
            spinnerList.add(songArtistEach);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinnerExistingArtist.setAdapter(adapter);

        spinnerExistingArtist.setOnItemSelectedListener(
                new Spinner.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedArtist = spinnerExistingArtist.getItemAtPosition(position).toString();
                        if (!selectedArtist.equals(getString(R.string.activity_add_song_spinner_existing_artist_default)))
                            editTextSongArtist.setText(selectedArtist);
                        else
                            editTextSongArtist.setText("");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

    public void onClickButtonAddSong(View view){
        String songTitle = editTextSongTitle.getText().toString();
        String songArtist = editTextSongArtist.getText().toString();

        DBHandler db = new DBHandler(this, null, null, 1);
        Song newSong = new Song(songTitle, songArtist);
        db.createSong(newSong);
        Toast.makeText(this, "\"" + songTitle + "\" has been added to the list.", Toast.LENGTH_LONG).show();
        finish();
    }
}
