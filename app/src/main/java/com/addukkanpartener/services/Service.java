package com.addukkanpartener.services;

import com.addukkanpartener.models.CountryDataModel;
import com.addukkanpartener.models.PlaceGeocodeData;
import com.addukkanpartener.models.PlaceMapDetailsData;
import com.addukkanpartener.models.SpecialDataModel;
import com.addukkanpartener.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone") String email,
                          @Field("password") String password);
    @GET("api/setting-country")
    Call<CountryDataModel> getCountries(@Header("lang") String lang);
    @GET("api/specializations")
    Call<SpecialDataModel> getSpecial(@Header("lang") String lang);




}