package com.omworldgame.genaiandroidgameexam.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PlaceData implements Serializable, Parcelable {
    private static final long serialVersionUID = 1L;

    private int placeNumber;
    private String placeName;
    private int eventCharNum;

    public PlaceData(int placeNumber, String placeName, int camera, int eventCharNum) {
        this.placeNumber = placeNumber;
        this.placeName = placeName;
        this.eventCharNum = eventCharNum;
    }

    public PlaceData(PlaceData place) {
        this.placeNumber = place.getPlaceNumber();
        this.placeName = place.getPlaceName();;
        this.eventCharNum = place.getEventCharNum();
    }

    public int getPlaceNumber() { return this.placeNumber; }
    public void setPlaceNumber(int placeNumber) { this.placeNumber = placeNumber; }
    public String getPlaceName() { return this.placeName; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }
    public int getEventCharNum() { return this.eventCharNum; }
    public void setEventCharNum(int eventCharNum) { this.eventCharNum = eventCharNum; }

    @Override
    public int describeContents() {
        return 0;
    }

    public PlaceData(Parcel in) {
        this.placeNumber = in.readInt();
        this.placeName = in.readString();
        this.eventCharNum = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.placeNumber);
        parcel.writeString(this.placeName);
        parcel.writeInt(this.eventCharNum);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public PlaceData createFromParcel(Parcel parcel) {
            return new PlaceData(parcel);
        }

        @Override
        public PlaceData[] newArray(int size) {
            return new PlaceData[size];
        }
    };
}
