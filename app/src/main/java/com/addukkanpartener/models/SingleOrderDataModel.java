package com.addukkanpartener.models;

import java.io.Serializable;
import java.util.List;

public class SingleOrderDataModel extends ResponseModel implements Serializable {
    private OrderModel data;

    public OrderModel getData() {
        return data;
    }
}
