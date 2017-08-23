package br.com.udacity.baking.recipedetail;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class RecipeStep implements Parcelable, Comparable<RecipeStep> {

    private Long id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public RecipeStep() {}

    public RecipeStep(Parcel parcel) {
        id = parcel.readLong();
        shortDescription = parcel.readString();
        description = parcel.readString();
        videoURL = parcel.readString();
        thumbnailURL = parcel.readString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public int compareTo(@NonNull RecipeStep recipeStep) {
        if (this.id > recipeStep.getId()) {
            return 1;
        } else if (this.id < recipeStep.getId()) {
            return -1;
        }
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }

    public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>() {
        @Override
        public RecipeStep createFromParcel(Parcel parcel) {
            return new RecipeStep(parcel);
        }

        @Override
        public RecipeStep[] newArray(int i) {
            return new RecipeStep[i];
        }
    };
}
