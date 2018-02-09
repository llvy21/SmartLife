package com.example.android.datafrominternet.Movie;

/**
 * Created by ucla on 2018/2/5.
 */

public class MovieData {

    public String getMovieName() {
        return movieName;
    }

    public String getCast() {
        return cast;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getRemark() {
        return remark;
    }

    String movieName = null;
    String cast = null;
    String img_url = null;
    String remark = null;

    public MovieData(String movieName, String cast, String img_url, String remark) {
        this.movieName = movieName;
        this.cast = cast;
        this.img_url = img_url;
        this.remark = remark;
    }


}
