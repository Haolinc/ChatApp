package com.example.chatapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {
    String id, name, documentID;

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDocumentID() {
        return documentID;
    }

    public UserData(String id, String name, String documentID) {
        this.id = id;
        this.name = name;
        this.documentID = documentID;
    }

    //parcel constructor  ****MUST HAVE****
    public UserData(Parcel in){
        id = in.readString();
        name = in.readString();
        documentID = in.readString();
    }

    //could be ignored
    @Override
    public int describeContents() {
        return 0;
    }

    //the data to pass into parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //id, latestText, targetName, targetDocumentID
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(documentID);
    }

    //parcel creator
    public static final Parcelable.Creator<UserData> CREATOR
            = new Parcelable.Creator<UserData>() {
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
}
