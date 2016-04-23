package me.sivze.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Siva on 4/21/2016.
 */
public class ReviewModel implements Parcelable{
    public String author;
    public String content;
    public String url;

    @Override
    public String toString() {
        return "Review{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }

    public ReviewModel() {
    }

    protected ReviewModel(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<ReviewModel> CREATOR = new Parcelable.Creator<ReviewModel>() {
        public ReviewModel createFromParcel(Parcel source) {
            return new ReviewModel(source);
        }

        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };
}
