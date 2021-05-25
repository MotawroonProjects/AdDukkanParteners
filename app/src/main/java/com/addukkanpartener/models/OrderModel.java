package com.addukkanpartener.models;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private int id;
    private int user_id;
    private int doctor_id;
    private String public_prescriotion_id;
    private String code;
    private String image;
    private String status;
    private String note;
    private double total_price;
    private String created_at;
    private String updated_at;
    private UserModel.User doctor_fk;
    private UserModel.User user_fk;
    private List<PrescriptionDetailsFk> prescription_details_fk;

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

    public double getTotal_price() {
        return total_price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel.User getDoctor_fk() {
        return doctor_fk;
    }

    public UserModel.User getUser_fk() {
        return user_fk;
    }

    public List<PrescriptionDetailsFk> getPrescription_details_fk() {
        return prescription_details_fk;
    }

    public static class PrescriptionDetailsFk implements Serializable{
        private int id;
        private int prescription_id;
        private int product_id;
        private int amount;
        private double price;
        private String status;
        private String created_at;
        private String updated_at;
        private TreatmentModel product_data;

        public int getId() {
            return id;
        }

        public int getPrescription_id() {
            return prescription_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public int getAmount() {
            return amount;
        }

        public double getPrice() {
            return price;
        }

        public String getStatus() {
            return status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public TreatmentModel getProduct_data() {
            return product_data;
        }
    }
}
