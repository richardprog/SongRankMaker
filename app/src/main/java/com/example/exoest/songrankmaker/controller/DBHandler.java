package com.example.exoest.songrankmaker.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.exoest.songrankmaker.model.Ranking;
import com.example.exoest.songrankmaker.model.RankingSong;
import com.example.exoest.songrankmaker.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by exoest on 17/5/2017.
 */

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ranking.db";
    public static final String TABLE_RANKING = "RANKING";
    public static final String TABLE_SONG = "SONG";
    public static final String TABLE_RANKINGSONG = "RANKINGSONG";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_ARTIST = "ARTIST";
    public static final String COLUMN_RANKINGID = "RANKINGID";
    public static final String COLUMN_SONGID = "SONGID";
    public static final String COLUMN_RANK = "RANK";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryRanking = "CREATE TABLE " + TABLE_RANKING + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " VARCHAR(50) " +
                ");";
        String querySong = "CREATE TABLE " + TABLE_SONG + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " VARCHAR(50), " +
                COLUMN_ARTIST + " VARCHAR(50) " +
                ");";
        String queryRankingSong = "CREATE TABLE " + TABLE_RANKINGSONG + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_RANKINGID + " INTEGER, " +
                COLUMN_SONGID + " INTEGER, " +
                COLUMN_RANK + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_RANKINGID + ") REFERENCES " + TABLE_RANKING + "(" + COLUMN_ID + ")," +
                "FOREIGN KEY(" + COLUMN_SONGID + ") REFERENCES " + TABLE_SONG + "(" + COLUMN_ID + ")" +
                ");";
        db.execSQL(queryRanking);
        db.execSQL(querySong);
        db.execSQL(queryRankingSong);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RANKINGSONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RANKING);
    }

    // region Ranking CRUD
    public void createRanking(Ranking ranking){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, ranking.get_name());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_RANKING, null, values);
        db.close();
    }

    public Ranking retrieveRankingById(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_RANKING + " WHERE " + COLUMN_ID + " = " + id + ";";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        Ranking ranking = new Ranking(c.getInt(c.getColumnIndex(COLUMN_ID)),
                c.getString(c.getColumnIndex(COLUMN_NAME)));
        db.close();
        return ranking;
    }

    public Ranking retrieveRankingByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_RANKING + " WHERE " + COLUMN_NAME + " = \"" + name + "\";";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        Ranking ranking = new Ranking(c.getInt(c.getColumnIndex(COLUMN_ID)),
                c.getString(c.getColumnIndex(COLUMN_NAME)));
        db.close();
        return ranking;
    }

    public List<Ranking> retrieveAllRanking(){
        List<Ranking> rankingList = new ArrayList<Ranking>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_RANKING + ";";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_NAME))!=null){
                Ranking r = new Ranking(c.getInt(c.getColumnIndex(COLUMN_ID)),
                        c.getString(c.getColumnIndex(COLUMN_NAME)));
                rankingList.add(r);
                c.moveToNext();
            }
        }
        db.close();
        return rankingList;
    }

    public void updateRankingByName(String name, Ranking newRanking){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_RANKING +
                " SET " + COLUMN_NAME + " = \"" + newRanking.get_name() + "\" " +
                " WHERE " + COLUMN_NAME + " = \"" + name + "\";");
        db.close();
    }

    public void deleteRanking(String name){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_RANKING + " WHERE " + COLUMN_NAME + "=\"" + name + "\";");
        db.close();
    }

    public void deleteRankingAndReferencedRecord(String name){
        SQLiteDatabase db = getWritableDatabase();
        Ranking r = retrieveRankingByName(name);
        db.execSQL("DELETE FROM " + TABLE_RANKINGSONG + " WHERE " + COLUMN_RANKINGID + " = " + r.get_id() + ";");
        db.execSQL("DELETE FROM " + TABLE_RANKING + " WHERE " + COLUMN_NAME + " = \"" + name + "\";");
        db.close();
    }
    // endregion

    // region Song CRUD
    public void createSong(Song song){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, song.get_name());
        values.put(COLUMN_ARTIST, song.get_artist());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_SONG, null, values);
        db.close();
    }

    public Song retrieveSongById(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SONG + " WHERE " + COLUMN_ID + " = " + id + ";";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        Song song = new Song(c.getInt(c.getColumnIndex(COLUMN_ID)),
                c.getString(c.getColumnIndex(COLUMN_NAME)),
                c.getString(c.getColumnIndex(COLUMN_ARTIST)));
        db.close();
        return song;
    }

    public Song retrieveSongByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SONG + " WHERE " + COLUMN_NAME + " = \"" + name + "\";";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        Song song = new Song(c.getInt(c.getColumnIndex(COLUMN_ID)),
                c.getString(c.getColumnIndex(COLUMN_NAME)),
                c.getString(c.getColumnIndex(COLUMN_ARTIST)));
        db.close();
        return song;
    }

    public List<Song> retrieveAllSong(boolean isSort){
        List<Song> songList = new ArrayList<Song>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SONG;
        if (isSort){
            query += " ORDER BY " + COLUMN_ARTIST + ", " + COLUMN_NAME;
        }
        query += ";";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_NAME))!=null){
                Song s = new Song(c.getInt(c.getColumnIndex(COLUMN_ID)),
                        c.getString(c.getColumnIndex(COLUMN_NAME)),
                        c.getString(c.getColumnIndex(COLUMN_ARTIST)));
                songList.add(s);
                c.moveToNext();
            }
        }
        db.close();
        return songList;
    }

    public List<String> retrieveAllSongArtistDistinct(boolean isArtistSort){
        List<String> songArtistList = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT DISTINCT " + COLUMN_ARTIST + " FROM " + TABLE_SONG;
        if (isArtistSort)
            query += " ORDER BY " + COLUMN_ARTIST;
        query += ";";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_ARTIST))!=null){
                songArtistList.add(c.getString(c.getColumnIndex(COLUMN_ARTIST)));
                c.moveToNext();
            }
        }
        db.close();
        return songArtistList;
    }

    public void updateSongByName(String name, Song newSong){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_SONG +
                " SET " + COLUMN_NAME + " = \"" + newSong.get_name() + "\" " +
                " WHERE " + COLUMN_NAME + " = \"" + name + "\";");
        db.close();
    }

    public void updateSongAsWholeById(int id, Song newSong){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_SONG +
                " SET " + COLUMN_NAME + " = \"" + newSong.get_name() + "\", " +
                COLUMN_ARTIST + " = \"" + newSong.get_artist() + "\"" +
                " WHERE " + COLUMN_ID + " = " + id + ";");
        db.close();
    }

    public void deleteSong(String name){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SONG + " WHERE " + COLUMN_NAME + "=\"" + name + "\";");
        db.close();
    }

    public void deleteSongAndReferencedRecord(String name){
        SQLiteDatabase db = getWritableDatabase();
        Song s = retrieveSongByName(name);
        db.execSQL("DELETE FROM " + TABLE_RANKINGSONG + " WHERE " + COLUMN_SONGID + " = " + s.get_id() + ";");
        db.execSQL("DELETE FROM " + TABLE_SONG + " WHERE " + COLUMN_NAME + " = \"" + name + "\";");
        db.close();
    }
    // endregion

    // region RankingSong CRUD
    public void createRankingSong(RankingSong rankingSong){
        ContentValues values = new ContentValues();
        values.put(COLUMN_RANKINGID, rankingSong.get_ranking().get_id());
        values.put(COLUMN_SONGID, rankingSong.get_song().get_id());
        values.put(COLUMN_RANK, rankingSong.get_rank());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_RANKINGSONG, null, values);
        db.close();
    }

//    public RankingSong retrieveRankingSongById(int id){
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_RANKINGSONG + " WHERE " + COLUMN_ID + " = " + id + ";";
//
//        Cursor c = db.rawQuery(query, null);
//        c.moveToFirst();
//
//        RankingSong rankingSong = new RankingSong(c.getInt(c.getColumnIndex(COLUMN_ID)),
//                retrieveRankingById(c.getInt(c.getColumnIndex(COLUMN_RANKINGID))),
//                retrieveSongById(c.getInt(c.getColumnIndex(COLUMN_SONGID))),
//                c.getInt(c.getColumnIndex(COLUMN_RANK)));
//        return rankingSong;
//    }

//    public RankingSong retrieveRankingSongByName(String name){
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_RANKINGSONG + " WHERE " + COLUMN_NAME + " = \"" + name + "\";";
//
//        Cursor c = db.rawQuery(query, null);
//        c.moveToFirst();
//
//        RankingSong rankingSong = new RankingSong(c.getInt(c.getColumnIndex(COLUMN_ID)),
//                retrieveRankingById(c.getInt(c.getColumnIndex(COLUMN_RANKINGID))),
//                retrieveSongById(c.getInt(c.getColumnIndex(COLUMN_SONGID))),
//                c.getInt(c.getColumnIndex(COLUMN_RANK)));
//        return rankingSong;
//    }

//    public List<RankingSong> retrieveAllRankingSong(){
//        List<RankingSong> rankingSongList = new ArrayList<RankingSong>();
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_RANKINGSONG + ";";
//
//        Cursor c = db.rawQuery(query, null);
//        c.moveToFirst();
//
//        while(!c.isAfterLast()){
//            if (c.getString(c.getColumnIndex(COLUMN_RANKINGID))!=null){
//                RankingSong rs = new RankingSong(c.getInt(c.getColumnIndex(COLUMN_ID)),
//                        retrieveRankingById(c.getInt(c.getColumnIndex(COLUMN_RANKINGID))),
//                        retrieveSongById(c.getInt(c.getColumnIndex(COLUMN_SONGID))),
//                        c.getInt(c.getColumnIndex(COLUMN_RANK)));
//                rankingSongList.add(rs);
//                c.moveToNext();
//            }
//        }
//        db.close();
//        return rankingSongList;
//    }

    public void updateRankByRankingIdAndSongId(Ranking ranking, Song song, int newRank){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_RANKINGSONG +
                " SET " + COLUMN_RANK + " = " + newRank +
                " WHERE " + COLUMN_RANKINGID + " = " + ranking.get_id() +
                " AND " + COLUMN_SONGID + " = " + song.get_id() + ";");
        db.close();
    }

    public void deleteRankingSong(Ranking ranking, Song song){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_RANKINGSONG +
                " WHERE " + COLUMN_RANKINGID + " = " + ranking.get_id() +
                " AND " + COLUMN_SONGID + " = " + song.get_id() + ";");
        db.close();
    }
    // endregion
}
