package com.addukkanpartener.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User data;
    private int status;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public static class User implements Serializable {
        private int id;
        private String name;
        private String email;
        private String phone;
        private String logo;
        private String fireBaseToken;
        private double total;
        private List<Value> values;

        public User() {
        }


        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getImage() {
            return logo;
        }


        public String getFireBaseToken() {
            return fireBaseToken;
        }

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }

        public double getTotal() {
            return total;
        }

        public List<Value> getValues() {
            return values;
        }
    }

    public static class Value implements Serializable{
        private int id;
        private int deal_id;
        private int user_id;
        private int describe_id;
        private int percent;
        private int value;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public int getDeal_id() {
            return deal_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getDescribe_id() {
            return describe_id;
        }

        public int getPercent() {
            return percent;
        }

        public int getValue() {
            return value;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
    public int getStatus() {
        return status;
    }





}
