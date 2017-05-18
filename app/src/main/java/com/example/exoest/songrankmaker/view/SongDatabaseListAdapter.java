package com.example.exoest.songrankmaker.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.exoest.songrankmaker.R;
import com.example.exoest.songrankmaker.model.Song;

import java.util.List;

/**
 * Created by exoest on 18/5/2017.
 */

public class SongDatabaseListAdapter extends ArrayAdapter<Song> {
    public SongDatabaseListAdapter(@NonNull Context context, @LayoutRes int resource, List<Song> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View songDatabaseCustomView = inflater.inflate(R.layout.custom_row_song_database, parent, false);

        TextView textViewSongTitle = (TextView) songDatabaseCustomView.findViewById(R.id.textViewSongTitle);
        TextView textViewSongArtist = (TextView) songDatabaseCustomView.findViewById(R.id.textViewSongArtist);

        String songTitle = getItem(position).get_name();
        String songArtist = getItem(position).get_artist();

        textViewSongTitle.setText(String.valueOf(songTitle));
        textViewSongArtist.setText(songArtist);
        return songDatabaseCustomView;
    }
}
