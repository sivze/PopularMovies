package me.sivze.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Siva on 4/21/2016.
 */
public class TrailerModel implements Parcelable{
    public String key;
    public String name;
    public String site;
    public String type;

    @Override
    public String toString() {
        return "Trailer{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeString(this.type);
    }

    public TrailerModel() {
    }

    protected TrailerModel(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<TrailerModel> CREATOR = new Parcelable.Creator<TrailerModel>() {
        public TrailerModel createFromParcel(Parcel source) {
            return new TrailerModel(source);
        }

        public TrailerModel[] newArray(int size) {
            return new TrailerModel[size];
        }
    };
}
