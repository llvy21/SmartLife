package com.example.android.datafrominternet.Notebook;

import cn.bmob.v3.BmobObject;

/**
 * Created by ucla on 2018/2/10.
 */

public class NoteData extends BmobObject {

    private String title;

    private String content;


    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
