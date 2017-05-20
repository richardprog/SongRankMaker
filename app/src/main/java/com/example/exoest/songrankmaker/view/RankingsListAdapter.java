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
import com.example.exoest.songrankmaker.controller.DBHandler;
import com.example.exoest.songrankmaker.model.Ranking;

import java.util.List;

/**
 * Created by exoest on 20/5/2017.
 */

public class RankingsListAdapter extends ArrayAdapter<Ranking> {
    public RankingsListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Ranking> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rankingsCustomView = inflater.inflate(R.layout.custom_row_rankings, parent, false);

        TextView textViewRankingName = (TextView) rankingsCustomView.findViewById(R.id.textViewRankingName);
        TextView textViewNumberOfSongs = (TextView) rankingsCustomView.findViewById(R.id.textViewNumberOfSongs);

        DBHandler db = new DBHandler(getContext(), null, null, 1);
        String rankingTitle = getItem(position).get_name();
        String numberOfSongs = getContext().getString(R.string.activity_custom_row_rankings_song_count_desc) + " " + String.valueOf(db.retrieveSongCountByRankingId(getItem(position).get_id()));

        textViewRankingName.setText(String.valueOf(rankingTitle));
        textViewNumberOfSongs.setText(numberOfSongs);
        return rankingsCustomView;
    }
}
