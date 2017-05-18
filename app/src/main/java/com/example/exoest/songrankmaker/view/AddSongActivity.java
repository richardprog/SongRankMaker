package com.example.exoest.songrankmaker.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exoest.songrankmaker.R;
import com.example.exoest.songrankmaker.controller.DBHandler;
import com.example.exoest.songrankmaker.model.Song;

public class AddSongActivity extends AppCompatActivity {
    private EditText editTextSongTitle;
    private EditText editTextSongArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        editTextSongTitle = (EditText) findViewById(R.id.editTextSongTitle);
        editTextSongArtist = (EditText) findViewById(R.id.editTextSongArtist);
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
