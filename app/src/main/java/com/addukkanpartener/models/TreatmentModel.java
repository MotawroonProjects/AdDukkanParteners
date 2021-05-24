package com.addukkanpartener.models;

import java.io.Serializable;

public class TreatmentModel implements Serializable {
    private int id;
    private int vendor_id;
    private String brand_id;
    private int product_company_id;
    private String main_image;
    private double rate;
    private int total_stock;
    private int sell_count;
    private int view_count;
    private String have_offer;
    private String offer_type;
    private double offer_value;
    private double offer_min;
    private double offer_bonus;
    private String is_published;
    private ProductTransFk product_trans_fk;

    public int getId() {
        return id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public int getProduct_company_id() {
        return product_company_id;
    }

    public String getMain_image() {
        return main_image;
    }

    public double getRate() {
        return rate;
    }

    public int getTotal_stock() {
        return total_stock;
    }

    public int getSell_count() {
        return sell_count;
    }

    public int getView_count() {
        return view_count;
    }

    public String getHave_offer() {
        return have_offer;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public double getOffer_value() {
        return offer_value;
    }

    public double getOffer_min() {
        return offer_min;
    }

    public double getOffer_bonus() {
        return offer_bonus;
    }

    public String getIs_published() {
        return is_published;
    }

    public ProductTransFk getProduct_trans_fk() {
        return product_trans_fk;
    }

    public static class ProductTransFk implements Serializable{
        private int id;
        private int product_id;
        private String title;
        private String description;
        private String details;
        private String lang;

        public int getId() {
            return id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getDetails() {
            return details;
        }

        public String getLang() {
            return lang;
        }
    }
}
