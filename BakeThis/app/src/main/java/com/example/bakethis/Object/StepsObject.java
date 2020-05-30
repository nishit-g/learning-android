package com.example.bakethis.Object;

import android.os.Parcel;
import android.os.Parcelable;

public class StepsObject implements Parcelable {
    private int id;
    private String shortDesc;
    private String desc;
    private String videoUrl;
    private String thumbnailUrl;

    public StepsObject() {
    }

    protected StepsObject(Parcel in) {
        id = in.readInt();
        shortDesc = in.readString();
        desc = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    public static final Creator<StepsObject> CREATOR = new Creator<StepsObject>() {
        @Override
        public StepsObject createFromParcel(Parcel in) {
            return new StepsObject(in);
        }

        @Override
        public StepsObject[] newArray(int size) {
            return new StepsObject[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDesc);
        dest.writeString(desc);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }
}
