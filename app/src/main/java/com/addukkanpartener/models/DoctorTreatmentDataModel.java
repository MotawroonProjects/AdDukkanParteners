package com.addukkanpartener.models;

import java.io.Serializable;
import java.util.List;

public class DoctorTreatmentDataModel extends ResponseModel implements Serializable {
    private List<DoctorTreatmentModel> data;

    public List<DoctorTreatmentModel> getData() {
        return data;
    }
}
