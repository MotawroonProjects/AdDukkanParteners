package com.addukkanpartener.models;

import java.io.Serializable;
import java.util.List;

public class ChartDataModel extends ResponseModel implements Serializable {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private List<Chart> chart;
        private double percentage_purchase_prescriptions;

        public List<Chart> getChart() {
            return chart;
        }

        public double getPercentage_purchase_prescriptions() {
            return percentage_purchase_prescriptions;
        }
    }

    public static class Chart implements Serializable {
        private String month;
        private double clients;

        public String getMonth() {
            return month;
        }

        public double getClients() {
            return clients;
        }
    }

}
