package com.example.exoest.songrankmaker.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
    private static String prevSong = "";
    private int darkBlue = ContextCompat.getColor(getContext(), R.color.color_custom_row_song_database_darkblue);
    private int lightBlue = ContextCompat.getColor(getContext(), R.color.color_custom_row_song_database_lightblue);
    private int currentBlue;

    public SongDatabaseListAdapter(@NonNull Context context, @LayoutRes int resource, List<Song> objects) {
        super(context, resource, objects);
        currentBlue = lightBlue;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View songDatabaseCustomView;
//        if (position == 0)
//            currentBlue = ContextCompat.getColor(getContext(), R.color.black);
//        if (convertView == null){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        songDatabaseCustomView = inflater.inflate(R.layout.custom_row_song_database, parent, false);
//        } else
//        songDatabaseCustomView = convertView;

        TextView textViewCustomRowSongTitle = (TextView) songDatabaseCustomView.findViewById(R.id.textViewCustomRowSongTitle);
        TextView textViewCustomRowSongArtist = (TextView) songDatabaseCustomView.findViewById(R.id.textViewCustomRowSongArtist);

        String songTitle = getItem(position).get_name();
        String songArtist = getItem(position).get_artist();

        textViewCustomRowSongTitle.setText(songTitle);
        textViewCustomRowSongArtist.setText(songArtist);

        // temporarily put aside due to list recycling
//        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("sortArtist", false)) {
//            if (position != 0) {
//                if (!getItem(position).get_artist().equals(getItem(position - 1).get_artist())) {
//                    if (currentBlue == darkBlue)
//                        currentBlue = lightBlue;
//                    else
//                        currentBlue = darkBlue;
//                }
//            }
//        }
//        songDatabaseCustomView.setBackgroundColor(currentBlue);

        return songDatabaseCustomView;
    }
}
