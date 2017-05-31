package com.example.exoest.songrankmaker.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exoest.songrankmaker.R;
import com.example.exoest.songrankmaker.controller.DBHandler;
import com.example.exoest.songrankmaker.model.Song;

public class EditSongActivity extends AppCompatActivity {
    private EditText editTextEditSongTitle;
    private EditText editTextEditSongArtist;
    private int songId = 0;
    private String oldSongTitle = "";
    private String oldArtist = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_song);

        Bundle bundle = getIntent().getExtras();
        songId = Integer.parseInt(bundle.getString("songid"));

        editTextEditSongTitle = (EditText) findViewById(R.id.editTextEditSongTitle);
        editTextEditSongArtist = (EditText) findViewById(R.id.editTextEditSongArtist);

        DBHandler db = new DBHandler(this, null, null, 1);
        Song song = db.retrieveSongById(songId);
        oldSongTitle = song.get_name();
        oldArtist = song.get_artist();

        editTextEditSongTitle.setText(song.get_name());
        editTextEditSongArtist.setText(song.get_artist());
    }

    public void onClickButtonEditSong(View view){
        String newSongTitle = editTextEditSongTitle.getText().toString();
        String newSongArtist = editTextEditSongArtist.getText().toString();

        if (!newSongTitle.equals("") && !newSongArtist.equals("")){
            DBHandler db = new DBHandler(this, null, null, 1);
            Song newSong = new Song(newSongTitle, newSongArtist);

            db.updateSongAsWholeById(songId, newSong);
            db.updateCommonSongArtist(oldArtist, newSong.get_artist());

            Toast.makeText(this, "\"" + oldSongTitle + "\" info has been updated to \"" + newSongTitle + "\".", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.activity_add_song_button_edit_song_null_toast), Toast.LENGTH_LONG).show();
        }

    }
}
