package com.addukkanpartener.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.addukkanpartener.BR;
import com.addukkanpartener.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddPrescriptionModel extends BaseObservable implements Serializable {
    private int client_id;
    private int doctor_id;
    private String country_code;
    private String note;
    private List<ItemModel> products_list;

    public AddPrescriptionModel() {
        client_id = 0;
        doctor_id = 0;
        country_code = "";
        note= "";
        products_list = new ArrayList<>();
    }

    public boolean isDataValid(Context context){
        if (client_id!=0&&
                products_list.size()>0)
        {
            return true;
        }else {
            if (client_id==0){
                Toast.makeText(context, context.getString(R.string.ch_client), Toast.LENGTH_SHORT).show();
            }

            if (products_list.size()==0){
                Toast.makeText(context, R.string.ch_presc, Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public List<ItemModel> getProducts_list() {
        return products_list;
    }

    public void setProducts_list(List<ItemModel> products_list) {
        this.products_list = products_list;
    }
    @Bindable
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        notifyPropertyChanged(BR.note);
    }

    public double getTotal(){
        double total = 0.0;
        for (ItemModel itemModel:products_list){
            total += itemModel.amount*itemModel.price;
        }
        return total;
    }

    public List<ItemModel> addItem(ItemModel itemModel){
        int item_pos = getItemPos(itemModel.product_id);
        if (item_pos!=-1){
            products_list.set(item_pos,itemModel);
        }else {
            products_list.add(itemModel);
        }

        return products_list;
    }
    public void  deleteItem(ItemModel itemModel){
        int item_pos = getItemPos(itemModel.product_id);
        if (item_pos!=-1){
            products_list.remove(item_pos);
        }

    }
    private int getItemPos(int id){
        int pos = -1;
        for (int index=0;index<products_list.size();index++){
            if (products_list.get(index).getProduct_id()==id){
                pos = index;
                return pos;
            }
        }
        return pos;
    }
    public static class ItemModel implements Serializable{
        private int product_id;
        private int amount;
        private String name;
        private double price;

        public ItemModel(int product_id, int amount, String name, double price) {
            this.product_id = product_id;
            this.amount = amount;
            this.name = name;
            this.price = price;
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

        public String getName() {
            return name;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
