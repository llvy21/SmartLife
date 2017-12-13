package com.example.ucla.notebook;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by ucla on 2017/11/5.
 */

public class User extends DataSupport implements Serializable{
    private String title;
    private String content;

    public User(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }
}
