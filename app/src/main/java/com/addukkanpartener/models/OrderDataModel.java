package com.addukkanpartener.models;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel extends ResponseModel implements Serializable {
    private List<OrderModel> data;

    public List<OrderModel> getData() {
        return data;
    }
}
