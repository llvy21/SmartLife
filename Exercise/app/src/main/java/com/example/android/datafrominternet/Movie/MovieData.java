package com.example.android.datafrominternet.Movie;

/**
 * Created by ucla on 2018/2/5.
 */

public class MovieData {

    public String getMovieName() {
        return movieName ;
    }

    public String getCast() {
        return cast;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getRemark() {return remark;}

    public String getDirector(){return director;}

    public String getKind(){return kind;}

    public String getDate(){return  date;}

    public String getCinema(){return  cinema;}


    String movieName = null;
    String cast = null;
    String img_url = null;
    String remark = null;
    String director = null;
    String kind = null;
    String cinema = null;
    String date = null;

    public MovieData(String movieName, String cast, String img_url, String remark,String director,String kind,String cinema,String date) {
        this.movieName = movieName;
        this.cast = cast;
        this.img_url = img_url;
        this.remark = remark;
        this.director = director;
        this.kind = kind;
        this.cinema = cinema;
        this.date = date;
    }


}
