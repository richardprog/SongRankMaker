package com.example.exoest.songrankmaker.model;

/**
 * Created by exoest on 17/5/2017.
 */

public class Song {
    private int _id;
    private String _name;
    private String _artist;

    public Song() {
    }

    public Song(String _name, String _artist) {
        this._name = _name;
        this._artist = _artist;
    }

    public Song(int _id, String _name, String _artist) {
        this._id = _id;
        this._name = _name;
        this._artist = _artist;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_artist() {
        return _artist;
    }

    public void set_artist(String _artist) {
        this._artist = _artist;
    }
}
