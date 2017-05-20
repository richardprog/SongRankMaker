package com.example.exoest.songrankmaker.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exoest.songrankmaker.R;
import com.example.exoest.songrankmaker.controller.DBHandler;
import com.example.exoest.songrankmaker.model.Ranking;

public class AddRankingActivity extends AppCompatActivity {
    private EditText editTextRankingName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ranking);

        editTextRankingName = (EditText) findViewById(R.id.editTextRankingName);

    }

    public void onClickButtonAddRanking(View view){
        String rankingName = editTextRankingName.getText().toString();
        DBHandler db = new DBHandler(this, null, null, 1);
        db.createRanking(new Ranking(rankingName));
        Toast.makeText(this, "\"" + rankingName + "\" " + getString(R.string.activity_add_ranking_button_add_ranking_toast), Toast.LENGTH_LONG).show();
        finish();
    }
}
