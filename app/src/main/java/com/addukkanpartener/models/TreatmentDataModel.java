package com.addukkanpartener.models;

import java.io.Serializable;
import java.util.List;

public class TreatmentDataModel extends ResponseModel implements Serializable {
    private List<TreatmentModel> data;

    public List<TreatmentModel> getData() {
        return data;
    }
}
