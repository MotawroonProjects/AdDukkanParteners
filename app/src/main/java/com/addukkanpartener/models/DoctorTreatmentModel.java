package com.addukkanpartener.models;

import java.io.Serializable;

public class DoctorTreatmentModel implements Serializable {
    private int id;
    private int product_id;
    private int doctor_id;
    private int product_company_id;
    private TreatmentModel product_data;

    public int getId() {
        return id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public int getProduct_company_id() {
        return product_company_id;
    }

    public TreatmentModel getProduct_data() {
        return product_data;
    }
}
