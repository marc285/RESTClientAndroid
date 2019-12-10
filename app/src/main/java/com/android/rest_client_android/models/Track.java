package com.android.rest_client_android.models;

public class Track {
    private String id;
    private String title;
    private String singer;

    public Track(){
        //Empty Constructor
    }

    public Track(String title, String singer) { //id is not set here (This builder is used in the POST method)
        this.title = title;
        this.singer = singer;
    }

    public Track(String id, String title, String singer){ //This builder is used in the PUT (edit) method: id for identifying which Track to be modified, the other fields are the new updated fields
        this.id = id;
        this.title = title;
        this.singer = singer;
    }

    public String getId() { return this.id; }
    public String getTitle() {
        return this.title;
    }
    public String getSinger() {
        return this.singer;
    }
}
