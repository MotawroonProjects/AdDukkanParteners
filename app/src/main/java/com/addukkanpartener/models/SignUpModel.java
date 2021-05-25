package com.addukkanpartener.models;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.addukkanpartener.BR;
import com.addukkanpartener.R;

import java.io.Serializable;

public class SignUpModel extends BaseObservable implements Serializable {
    private String country_id;
    private int specialize;
    private String name;
    private String phone_code;
    private String phone;
    private String email;
    private String center;
    private String address;
    private double lat ;
    private double lng;
    private String cv;
    private String password;
    private String image;
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_center = new ObservableField<>();
    public ObservableField<String> error_cv = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();

    public SignUpModel() {
        name ="";
        phone_code ="+966";
        phone = "";
        email ="";
        center ="";
        address="";
        cv="";
        lat =0.0;
        lng=0.0;
        country_id ="";
        specialize = 0;
        password = "";
        image="";
    }

    public boolean isDataValid(Context context) {
        if (!country_id.isEmpty()&&
                specialize!=0&&
                !name.isEmpty() &&
                !phone.isEmpty() &&
                !email.isEmpty()&&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()&&
                !center.isEmpty()&&
                !address.isEmpty()&&
                !cv.isEmpty()&&
                !password.isEmpty() &&
                password.length() >= 6
        ) {
            error_center.set(null);
            error_cv.set(null);
            error_email.set(null);
            error_name.set(null);
            error_phone.set(null);
            error_password.set(null);
            return true;
        } else {
            Log.e("ldldldl",country_id+" "+specialize+" "+name+" "+phone+" "+email+center+" "+address+" "+cv+" "+password);
            if (country_id.isEmpty()){
                Toast.makeText(context, R.string.ch_country, Toast.LENGTH_SHORT).show();
            }
            if (specialize==0){
                Toast.makeText(context, R.string.ch_spec, Toast.LENGTH_SHORT).show();
            }

            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);

            }

            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_required));
            } else {
                error_phone.set(null);

            }

            if (email.isEmpty()) {
                error_email.set(context.getString(R.string.field_required));
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                error_email.set(context.getString(R.string.inv_email));

            }else {
                error_email.set(null);

            }

            if (password.isEmpty()) {
                error_password.set(context.getString(R.string.field_required));
            } else if (password.length() < 6) {
                error_password.set(context.getString(R.string.password_short));

            } else {
                error_password.set(null);

            }

            if (center.isEmpty()) {
                error_center.set(context.getString(R.string.field_required));
            } else {
                error_center.set(null);

            }

            if (cv.isEmpty()) {
                error_cv.set(context.getString(R.string.field_required));
            } else {
                error_cv.set(null);

            }
            if (address.isEmpty()) {
                Toast.makeText(context, R.string.ch_location, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    //add-client
    public boolean isData2Valid(Context context) {
        if (!country_id.isEmpty()&&
                !name.isEmpty() &&
                !phone.isEmpty()
        ) {
            error_name.set(null);
            error_phone.set(null);
            return true;
        } else {
            if (country_id.isEmpty()){
                Toast.makeText(context, R.string.ch_country, Toast.LENGTH_SHORT).show();
            }


            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));
            } else {
                error_name.set(null);

            }

            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_required));
            } else {
                error_phone.set(null);

            }


            return false;
        }
    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public int getSpecialize() {
        return specialize;
    }

    public void setSpecialize(int specialize) {
        this.specialize = specialize;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
        notifyPropertyChanged(BR.center);

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Bindable
    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
        notifyPropertyChanged(BR.cv);

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
