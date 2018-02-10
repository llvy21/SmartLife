package com.example.android.datafrominternet.Notebook;

import org.litepal.crud.DataSupport;

/**
 * Created by ucla on 2017/11/5.
 */

public class Note extends DataSupport{

    private String title;

    private String content;

    private String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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
