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
import com.example.exoest.songrankmaker.model.RankingSong;

import java.util.List;

/**
 * Created by exoest on 23/5/2017.
 */

public class ViewRankingListAdapter extends ArrayAdapter<RankingSong> {
    public ViewRankingListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<RankingSong> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View viewRankingCustomView = inflater.inflate(R.layout.custom_row_view_ranking, parent, false);

        TextView textViewViewRankingRankNumber = (TextView) viewRankingCustomView.findViewById(R.id.textViewViewRankingRankNumber);
        TextView textViewViewRankingSongTitle = (TextView) viewRankingCustomView.findViewById(R.id.textViewViewRankingSongTitle);
        TextView textViewViewRankingSongArtist = (TextView) viewRankingCustomView.findViewById(R.id.textViewViewRankingSongArtist);

        textViewViewRankingRankNumber.setText(String.valueOf(getItem(position).get_rank()));
        textViewViewRankingSongTitle.setText(getItem(position).get_song().get_name());
        textViewViewRankingSongArtist.setText(getItem(position).get_song().get_artist());

        if ((position >= 0) && (position < 3))
            viewRankingCustomView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_custom_row_song_database_darkblue));

        return viewRankingCustomView;
    }
}