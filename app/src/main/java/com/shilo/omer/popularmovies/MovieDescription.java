package com.shilo.omer.popularmovies;

/**
 * Created by omer.shilo on 05/03/2017.
 */

public class MovieDescription {

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }

    public String getPosterPath() {
        return PosterPath;
    }
    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }

    public String Description;
    public String Title;
    public String PosterPath;


    public MovieDescription(String description, String title, String posterPath) {
        Description = description;
        Title = title;
        PosterPath = posterPath;
    }
}
