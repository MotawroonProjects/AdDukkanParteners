package com.addukkanpartener.models;

import java.io.Serializable;
import java.util.List;

public class AllUserModel implements Serializable {
    private List<UserModel.User> data;
    private int status;
    private int count;

    public List<UserModel.User> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }
}
