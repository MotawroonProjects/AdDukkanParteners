package com.addukkanpartener.models;

import java.io.Serializable;

public class ClientPrescriptionDetailsModel implements Serializable {
    private int id;
    private int user_id;
    private int doctor_id;
    private String public_prescriotion_id;
    private String code;
    private String image;
    private String status;
    private String note;
    private int total_price;
    private String created_at;
    private String updated_at;
    private UserModel.User user_fk;


    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public String getPublic_prescriotion_id() {
        return public_prescriotion_id;
    }

    public String getCode() {
        return code;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public int getTotal_price() {
        return total_price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel.User getUser_fk() {
        return user_fk;
    }
}
