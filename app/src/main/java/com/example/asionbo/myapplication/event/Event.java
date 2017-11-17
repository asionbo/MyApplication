package com.example.asionbo.myapplication.event;

/**
 * Created by asionbo on 2017/10/13.
 */

public class Event {
    private int type;
    private Object post;

    public Event(int type) {
        this.type = type;
    }

    public Event(int type, Object post) {
        this.type = type;
        this.post = post;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getPost() {
        return post;
    }

    public void setPost(Object post) {
        this.post = post;
    }
}
