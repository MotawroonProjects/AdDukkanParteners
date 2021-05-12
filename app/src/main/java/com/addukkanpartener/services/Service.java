package com.addukkanpartener.services;

import com.addukkanpartener.models.AllUserModel;
import com.addukkanpartener.models.CountryDataModel;
import com.addukkanpartener.models.PlaceGeocodeData;
import com.addukkanpartener.models.PlaceMapDetailsData;
import com.addukkanpartener.models.SpecialDataModel;
import com.addukkanpartener.models.UserModel;
import com.google.android.gms.common.api.GoogleApiClient;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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


    @FormUrlEncoded
    @POST("api/doctor-register")
    Call<UserModel> signUp(@Field("name") String name,
                           @Field("phone_code") String phone_code,
                           @Field("phone") String phone,
                           @Field("password") String password,
                           @Field("software_type") String software_type,
                           @Field("country_code") String country_code,
                           @Field("address") String address,
                           @Field("hospital_place") String hospital_place,
                           @Field("latitude") double latitude,
                           @Field("longitude") double longitude,
                           @Field("specialization_id") int specialization_id,
                           @Field("email") String email,
                           @Field("about_user") String about_user


    );

    @Multipart
    @POST("api/doctor-register")
    Call<UserModel> signUpwithImage(
            @Part("name") RequestBody name,
            @Part("phone_code") RequestBody phone_code,
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password,
            @Part("software_type") RequestBody software_type,
            @Part("country_code") RequestBody country_code,
            @Part("address") RequestBody address,
            @Part("hospital_place") RequestBody hospital_place,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("specialization_id") RequestBody specialization_id,
            @Part("email") RequestBody email,
            @Part("about_user") RequestBody about_user,
            @Part MultipartBody.Part logo


    );

    @GET("api/clients")
    Call<AllUserModel> getPatient(@Query("search_key") String search_key,
                                  @Query("pagination") String pagination);

}