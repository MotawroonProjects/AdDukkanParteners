package com.addukkanpartener.models;

import java.io.Serializable;
import java.util.List;

public class AllUserModel extends ResponseModel implements Serializable {
    private List<UserModel.User> data;
    private int count;

    public List<UserModel.User> getData() {
        return data;
    }


    public int getCount() {
        return count;
    }
}
