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
    int selectedBg = ContextCompat.getColor(getContext(), R.color.color_custom_row_song_database_darkblue);
    int nonselectedBg = ContextCompat.getColor(getContext(), R.color.color_custom_row_song_database_lightblue);
    public SongDatabaseListAdapter(@NonNull Context context, @LayoutRes int resource, List<Song> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.custom_row_song_database, parent, false);

            holder = new ViewHolder();
            holder.textViewCustomRowSongTitle = (TextView) view.findViewById(R.id.textViewCustomRowSongTitle);
            holder.textViewCustomRowSongArtist = (TextView) view.findViewById(R.id.textViewCustomRowSongArtist);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        view.setBackgroundColor(nonselectedBg);
        holder.textViewCustomRowSongTitle.setText(getItem(position).get_name());
        holder.textViewCustomRowSongArtist.setText(getItem(position).get_artist());
        if (getItem(position).isSelected())
            view.setBackgroundColor(selectedBg);
        else
            view.setBackgroundColor(nonselectedBg);

        return view;
    }

    public void changeSelectionUponSelected(int position){
        getItem(position).setSelected(!getItem(position).isSelected());
    }

    public static class ViewHolder{
        public TextView textViewCustomRowSongTitle;
        public TextView textViewCustomRowSongArtist;
    }
}
