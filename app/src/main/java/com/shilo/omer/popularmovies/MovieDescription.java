package com.shilo.omer.popularmovies;

/**
 * Created by omer.shilo on 05/03/2017.
 */

public class MovieDescription {

    public String Description;
    public String Title;
    public String PosterPath;
    public String Id;
    public String UserRating;
    public String ReleaseDate;


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

    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getUserRating() {
        return UserRating;
    }
    public void setUserRating(String userRating) {
        UserRating = userRating;
    }

    public MovieDescription(String posterPath) {
        PosterPath = posterPath;
    }

    public MovieDescription(String description, String title, String posterPath, String id, String releaseDate, String userRating) {
        Description = description;
        Title = title;
        PosterPath = posterPath;
        Id = id;
        ReleaseDate = releaseDate;
        UserRating = userRating;
    }
}
