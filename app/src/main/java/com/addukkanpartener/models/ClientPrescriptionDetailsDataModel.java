package com.addukkanpartener.models;

import java.io.Serializable;
import java.util.List;

public class ClientPrescriptionDetailsDataModel extends ResponseModel implements Serializable {
  private List<ClientPrescriptionDetailsModel> data;

    public List<ClientPrescriptionDetailsModel> getData() {
        return data;
    }
}
