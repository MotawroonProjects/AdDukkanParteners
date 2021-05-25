package com.addukkanpartener.models;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private int id;
    private int from_user_id;
    private int to_user_id;
    private int user_room_id;
    private String type;
    private String message;
    private String voice;
    private String image;
    private String created_at;
    private String updated_at;
    private UserModel.User from_user;
    private UserModel.User to_user;
    private boolean isLoaded = false;
    private int progress = 0;
    private int max_duration =0;
    private boolean isImageLoaded = false;

    public int getId() {
        return id;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public int getUser_room_id() {
        return user_room_id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Object getVoice() {
        return voice;
    }

    public String getImage() {
        return image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel.User getFrom_user() {
        return from_user;
    }

    public UserModel.User getTo_user() {
        return to_user;
    }


    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMax_duration() {
        return max_duration;
    }

    public void setMax_duration(int max_duration) {
        this.max_duration = max_duration;
    }

    public boolean isImageLoaded() {
        return isImageLoaded;
    }

    public void setImageLoaded(boolean imageLoaded) {
        isImageLoaded = imageLoaded;
    }
}