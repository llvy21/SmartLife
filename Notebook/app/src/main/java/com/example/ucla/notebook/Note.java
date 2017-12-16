package com.example.ucla.notebook;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by ucla on 2017/11/5.
 */

public class Note extends DataSupport{

    private String title;
    private String content;

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
