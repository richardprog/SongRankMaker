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
        return ranking;
    }

    public List<Ranking> retrieveAllRanking(boolean isSort){
        List<Ranking> rankingList = new ArrayList<Ranking>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_RANKING;
        if (isSort){
            query += " ORDER BY " + COLUMN_NAME;
        }
        query += ";";

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

    public void updateRankingById(int id, Ranking newRanking){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_RANKING +
                " SET " + COLUMN_NAME + " = \"" + newRanking.get_name() + "\" " +
                " WHERE " + COLUMN_ID + " = " + id + ";");
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

    public List<String> retrieveAllSongArtistSortDistinct(){
        List<String> songArtistList = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT DISTINCT " + COLUMN_ARTIST + " FROM " + TABLE_SONG +
                " ORDER BY " + COLUMN_ARTIST + ";";

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

    public void updateSongAsWholeById(int id, Song newSong){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_SONG +
                " SET " + COLUMN_NAME + " = \"" + newSong.get_name() + "\", " +
                COLUMN_ARTIST + " = \"" + newSong.get_artist() + "\"" +
                " WHERE " + COLUMN_ID + " = " + id + ";");
        db.execSQL("UPDATE " + TABLE_SONG +
                " SET " + COLUMN_ARTIST + " = \"" + newSong.get_artist() + "\"" +
                " WHERE " + COLUMN_ARTIST + " = " + id + ";");
        db.close();
    }

    public void updateCommonSongArtist(String oldSongArtist, String newSongArtist){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_SONG +
                " SET " + COLUMN_ARTIST + " = \"" + newSongArtist + "\"" +
                " WHERE " + COLUMN_ARTIST + " = \"" + oldSongArtist + "\";");
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

    public void createMultipleRankingSong(List<Song> songList, Ranking ranking){
        SQLiteDatabase db = getWritableDatabase();
        for (Song song : songList){
            if (!isSongExisted(ranking, song)){
                ContentValues values = new ContentValues();
                values.put(COLUMN_RANKINGID, ranking.get_id());
                values.put(COLUMN_SONGID, song.get_id());
                values.put(COLUMN_RANK, 0);
                db.insert(TABLE_RANKINGSONG, null, values);
            }
        }
        db.close();
    }

    public int retrieveSongCountByRankingId(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT COUNT(" + COLUMN_ID + ") AS NUM" +
                " FROM " + TABLE_RANKINGSONG +
                " WHERE " + COLUMN_RANKINGID + " = " + id + ";";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        int songCount = c.getInt(c.getColumnIndex("NUM"));
        db.close();
        return songCount;
    }

    public List<RankingSong> retrieveRankingSongByRankingId(int rankingId, boolean isRequestForUnranked){
        List<RankingSong> rankingSongList = new ArrayList<RankingSong>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + TABLE_SONG + "." + COLUMN_ID + ", " + TABLE_SONG + "." + COLUMN_NAME + ", " + TABLE_SONG + "." + COLUMN_ARTIST + ", " + TABLE_RANKINGSONG + "." + COLUMN_RANK +
                " FROM " + TABLE_RANKINGSONG +
                " INNER JOIN " + TABLE_SONG + " ON " + TABLE_RANKINGSONG + "." + COLUMN_SONGID + " = " + TABLE_SONG + "." + COLUMN_ID;

        if (!isRequestForUnranked)
            query += " WHERE " + COLUMN_RANKINGID + " = " + rankingId + " AND " + COLUMN_RANK + " != " + 0 +
                    " ORDER BY " + COLUMN_RANK + ";";
        else
            query += " WHERE " + COLUMN_RANKINGID + " = " + rankingId + " AND " + COLUMN_RANK + " = " + 0 + ";";


        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if (c.getString(c.getColumnIndex(COLUMN_ARTIST))!=null){
                RankingSong rankingSongSingle = new RankingSong(
                        null,
                        new Song(c.getInt(c.getColumnIndex(COLUMN_ID)), c.getString(c.getColumnIndex(COLUMN_NAME)), c.getString(c.getColumnIndex(COLUMN_ARTIST))),
                        c.getInt(c.getColumnIndex(COLUMN_RANK))
                );
                rankingSongList.add(rankingSongSingle);
                c.moveToNext();
            }
        }
        db.close();
        return rankingSongList;
    }

    public int getRankingSongCountByRankingId(int rankingId){
        int count = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT COUNT(" + COLUMN_ID + ") AS RSCOUNT" +
                " FROM " + TABLE_RANKINGSONG +
                " WHERE " + COLUMN_RANKINGID + " = " + rankingId + " AND " + COLUMN_RANK + " != " + 0 + ";";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
//            if (c.getString(c.getColumnIndex("RSCOUNT"))!=null)
            count = c.getInt(c.getColumnIndex("RSCOUNT"));
            c.moveToNext();
        }
        return count;
    }

    public boolean isSongExisted(Ranking ranking, Song song) {
        boolean isExisted;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_SONGID +
                " FROM " + TABLE_RANKINGSONG +
                " WHERE " + COLUMN_RANKINGID + " = " + ranking.get_id() +
                " AND " + COLUMN_SONGID + " = " + song.get_id();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c.isAfterLast())
            isExisted = false;
        else
            isExisted = true;
        return isExisted;
    }

    public void updateRankByRankingIdAndSongId(Ranking ranking, Song song, int newRank){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_RANKINGSONG +
                " SET " + COLUMN_RANK + " = " + String.valueOf(newRank) +
                " WHERE " + COLUMN_RANKINGID + " = " + ranking.get_id() +
                " AND " + COLUMN_SONGID + " = " + song.get_id() + ";");
        db.close();
    }

    public void deleteRankingSong(RankingSong rankingSong, boolean isRerankRequired){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_RANKINGSONG +
                " WHERE " + COLUMN_RANKINGID + " = " + rankingSong.get_ranking().get_id() +
                " AND " + COLUMN_SONGID + " = " + rankingSong.get_song().get_id() + ";");
        if (isRerankRequired){
            int count = getRankingSongCountByRankingId(rankingSong.get_ranking().get_id());
            for (int i = rankingSong.get_rank(); i <= count; i++){
                db.execSQL("UPDATE " + TABLE_RANKINGSONG +
                        " SET " + COLUMN_RANK + " = " + i +
                        " WHERE " + COLUMN_RANK + " = " + (i+1) + " AND " + COLUMN_RANKINGID + " = " + rankingSong.get_ranking().get_id() + ";");
            }
        }
        db.close();
    }
    // endregion
}
