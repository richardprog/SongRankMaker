package com.example.exoest.songrankmaker.model;

/**
 * Created by exoest on 17/5/2017.
 */

public class RankingSong {
    private int _id;
    private Ranking _ranking;
    private Song _song;
    private int _rank;
    private String _tempRank;

    public RankingSong() {
    }

    public RankingSong(Ranking _ranking, Song _song, int _rank) {
        this._ranking = _ranking;
        this._song = _song;
        this._rank = _rank;
    }

    public RankingSong(int _id, Ranking _ranking, Song _song, int _rank) {
        this._id = _id;
        this._ranking = _ranking;
        this._song = _song;
        this._rank = _rank;
    }

    public int get_id() {
        return _id;
    }

    public Ranking get_ranking() {
        return _ranking;
    }

    public Song get_song() {
        return _song;
    }

    public int get_rank() {
        return _rank;
    }

    public void set_ranking(Ranking _ranking) {
        this._ranking = _ranking;
    }

    public void set_song(Song _song) {
        this._song = _song;
    }

    public void set_rank(int _rank) {
        this._rank = _rank;
    }

    public String get_tempRank() {
        return _tempRank;
    }

    public void set_tempRank(String _tempRank) {
        this._tempRank = _tempRank;
    }
}