package com.shilo.omer.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by omer.shilo on 05/03/2017.
 */

public class MovieDescription implements Parcelable {

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

    public MovieDescription(Parcel in){
        this.Description = in.readString();
        this.Title = in.readString();
        this.PosterPath = in.readString();
        this.Id = in.readString();
        this.UserRating = in.readString();
        this.ReleaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Description);
        dest.writeString(Title);
        dest.writeString(PosterPath);
        dest.writeString(Id);
        dest.writeString(UserRating);
        dest.writeString(ReleaseDate);
    }

    static final Parcelable.Creator<MovieDescription> CREATOR =
            new Parcelable.Creator<MovieDescription>(){
              public MovieDescription createFromParcel(Parcel in){
                  return new MovieDescription(in);
              }

              public MovieDescription[] newArray(int size){
                  return new MovieDescription[size];
              }
            };
}
