package com.example.exoest.songrankmaker.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.exoest.songrankmaker.R;
import com.example.exoest.songrankmaker.controller.DBHandler;
import com.example.exoest.songrankmaker.model.Ranking;
import com.example.exoest.songrankmaker.model.RankingSong;

import java.util.List;

public class ViewRankingActivity extends AppCompatActivity {
    ListView listViewViewRanking;
    List<RankingSong> retrievedRankingSongList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ranking);

        Bundle bundle = getIntent().getExtras();
        DBHandler db = new DBHandler(this, null, null, 1);
        Ranking rankingForView = db.retrieveRankingById(Integer.parseInt(bundle.getString("viewRankingId")));
        setTitle(rankingForView.get_name());

        listViewViewRanking = (ListView) findViewById(R.id.listViewViewRanking);
        retrievedRankingSongList = db.retrieveRankingSongByRankingIdWithSortedRank(rankingForView.get_id());

        refreshListView();
    }

    public void refreshListView(){
        if (!retrievedRankingSongList.isEmpty()){
            ListAdapter listAdapter = new ViewRankingListAdapter(this, R.layout.custom_row_view_ranking, retrievedRankingSongList);
            listViewViewRanking.setAdapter(listAdapter);
//            registerForContextMenu(findViewById(R.id.listViewViewRanking));
//            listViewViewRanking.setOnItemClickListener(
//                    new AdapterView.OnItemClickListener(){
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            listViewViewRanking.showContextMenuForChild(view);
//                        }
//                    }
//            );
        }
    }
}
