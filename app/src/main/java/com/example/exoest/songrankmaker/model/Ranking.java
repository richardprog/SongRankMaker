package com.example.exoest.songrankmaker.model;

/**
 * Created by exoest on 17/5/2017.
 */

public class Ranking {
    private int _id;
    private String _name;

    public Ranking() {
    }

    public Ranking(String _name) {
        this._name = _name;
    }

    public Ranking(int _id, String _name) {
        this._id = _id;
        this._name = _name;
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
}
