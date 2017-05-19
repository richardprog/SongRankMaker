package com.example.exoest.songrankmaker.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.exoest.songrankmaker.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickButtonSongDatabase(View view){
        Intent intent = new Intent(this, SongDatabaseActivity.class);
        startActivity(intent);
    }

    public void onClickButtonRanking(View view){
        Intent intent = new Intent(this, SongDatabaseActivity.class);
        startActivity(intent);
    }

    public void onClickButtonSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
