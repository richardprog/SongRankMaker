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
import com.example.exoest.songrankmaker.model.RankingSong;

import java.util.List;

/**
 * Created by exoest on 23/5/2017.
 */

public class ViewRankingRearrangeListAdapter extends ArrayAdapter<RankingSong> {
    public ViewRankingRearrangeListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<RankingSong> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.custom_row_view_ranking, parent, false);

            holder = new ViewHolder();
            holder.textViewViewRankingRankNumber = (TextView) view.findViewById(R.id.textViewViewRankingRankNumber);
            holder.textViewViewRankingSongTitle = (TextView) view.findViewById(R.id.textViewViewRankingSongTitle);
            holder.textViewViewRankingSongArtist = (TextView) view.findViewById(R.id.textViewViewRankingSongArtist);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//        LayoutInflater inflater = LayoutInflater.from(getContext());
////        View viewRankingCustomView = inflater.inflate(R.layout.custom_row_view_ranking, parent, false);
//
//        convertView = inflater.inflate(R.layout.custom_row_view_ranking, parent, false);
//
//        TextView textViewViewRankingSongTitle = (TextView) convertView.findViewById(R.id.textViewViewRankingSongTitle);
//        TextView textViewViewRankingSongArtist = (TextView) convertView.findViewById(R.id.textViewViewRankingSongArtist);
        holder.textViewViewRankingRankNumber.setText("");
        holder.textViewViewRankingSongTitle.setText(getItem(position).get_song().get_name());
        holder.textViewViewRankingSongArtist.setText(getItem(position).get_song().get_artist());
        if (getItem(position).get_tempRank() != null)
            holder.textViewViewRankingRankNumber.setText(getItem(position).get_tempRank());

        return view;
    }

    public void setTempRankUponClick(int position, int rankNumber){
        getItem(position).set_tempRank(String.valueOf(rankNumber));
    }

    private static class ViewHolder{
        public TextView textViewViewRankingSongTitle;
        public TextView textViewViewRankingSongArtist;
        public TextView textViewViewRankingRankNumber;
    }
}

