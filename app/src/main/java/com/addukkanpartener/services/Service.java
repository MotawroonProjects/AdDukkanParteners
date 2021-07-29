package com.addukkanpartener.services;

import com.addukkanpartener.models.AddPrescriptionModel;
import com.addukkanpartener.models.AllUserModel;
import com.addukkanpartener.models.ChartDataModel;
import com.addukkanpartener.models.ClientPrescriptionDetailsDataModel;
import com.addukkanpartener.models.CompanyDataModel;
import com.addukkanpartener.models.CountryDataModel;
import com.addukkanpartener.models.NotificationCountModel;
import com.addukkanpartener.models.NotificationDataModel;
import com.addukkanpartener.models.DoctorTreatmentDataModel;
import com.addukkanpartener.models.MessageDataModel;
import com.addukkanpartener.models.OrderDataModel;
import com.addukkanpartener.models.PlaceGeocodeData;
import com.addukkanpartener.models.PlaceMapDetailsData;
import com.addukkanpartener.models.ResponseModel;
import com.addukkanpartener.models.ResponseModel;
import com.addukkanpartener.models.RoomDataModel;
import com.addukkanpartener.models.SettingDataModel;
import com.addukkanpartener.models.SingleMessageDataModel;
import com.addukkanpartener.models.SingleOrderDataModel;
import com.addukkanpartener.models.SpecialDataModel;
import com.addukkanpartener.models.TreatmentDataModel;
import com.addukkanpartener.models.UserModel;
import com.google.android.gms.common.api.GoogleApiClient;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
    @POST("api/login-doctor")
    Call<UserModel> login(@Field("phone_email") String phone_email,
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
    Call<UserModel> signUpwithImage(@Part("name") RequestBody name,
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

    @FormUrlEncoded
    @POST("api/update-doctor-profile")
    Call<UserModel> updateWithoutImage(@Header("Authorization") String bearer_token,
                                       @Field("user_id") int user_id,
                                       @Field("name") String name,
                                       @Field("phone_code") String phone_code,
                                       @Field("phone") String phone,
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
    @POST("api/update-doctor-profile")
    Call<UserModel> updateWithImage(@Header("Authorization") String bearer_token,
                                    @Part("user_id") RequestBody user_id,
                                    @Part("name") RequestBody name,
                                    @Part("phone_code") RequestBody phone_code,
                                    @Part("phone") RequestBody phone,
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

    @GET("api/count-unread")
    Call<NotificationCountModel> getNotificationCount(@Header("Authorization") String bearer_token,
                                                      @Query("user_id") int user_id
    );

    @GET("api/my-notification")
    Call<NotificationDataModel> getNotifications(@Header("Authorization") String bearer_token,
                                                 @Query("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("api/delete-notification")
    Call<ResponseModel> deleteNotification(@Header("Authorization") String bearer_token,
                                           @Field("notification_id") int notification_id);

    @GET("api/companies")
    Call<CompanyDataModel> getCompany(@Header("lang") String lang,
                                      @Query("search_key") String search_key);

    @GET("api/medicine")
    Call<TreatmentDataModel> getTreatments(@Header("lang") String lang,
                                           @Query("search_name") String search_name,
                                           @Query("country_code") String country_code,
                                           @Query("company_id") String company_id
    );

    @GET("api/medicine")
    Call<TreatmentDataModel> getTreatments2(@Header("lang") String lang,
                                            @Query("search_name") String search_name,
                                            @Query("country_code") String country_code
    );

    @FormUrlEncoded
    @POST("api/add-medicine")
    Call<ResponseModel> addTreatment(@Header("Authorization") String user_token,
                                     @Field("doctor_id") int doctor_id,
                                     @Field("product_id") int product_id,
                                     @Field("product_company_id") int product_company_id
    );


    @GET("api/doctor-medicine")
    Call<DoctorTreatmentDataModel> getMyTreatment(@Header("Authorization") String user_token,
                                                  @Header("lang") String lang,
                                                  @Query("doctor_id") int doctor_id
    );

    @FormUrlEncoded
    @POST("api/delete-medicine")
    Call<ResponseModel> deleteTreatment(@Header("Authorization") String user_token,
                                        @Field("doctor_id") int doctor_id,
                                        @Field("product_id") int product_id);


    @GET("api/user-rooms")
    Call<RoomDataModel> getRoom(@Header("Authorization") String user_token,
                                @Query("user_id") int user_id
    );

    @GET("api/room-messages")
    Call<MessageDataModel> getChatMessages(@Header("Authorization") String bearer_token,
                                           @Query(value = "pagination") String pagination,
                                           @Query(value = "per_page") int per_page,
                                           @Query(value = "page") int page,
                                           @Query(value = "room_id") int room_id,
                                           @Query(value = "user_id") int user_id

    );

    @FormUrlEncoded
    @POST("api/send-chat-message")
    Call<SingleMessageDataModel> sendChatMessage(@Header("Authorization") String bearer_token,
                                                 @Field("user_room_id") int room_id,
                                                 @Field("from_user_id") int from_user_id,
                                                 @Field("to_user_id") int to_user_id,
                                                 @Field("type") String type,
                                                 @Field("message") String message


    );

    @Multipart
    @POST("api/send-chat-message")
    Call<SingleMessageDataModel> sendChatAttachment(@Header("Authorization") String user_token,
                                                    @Part("user_room_id") RequestBody room_id,
                                                    @Part("from_user_id") RequestBody from_user_id,
                                                    @Part("to_user_id") RequestBody to_user_id,
                                                    @Part("type") RequestBody message_type,
                                                    @Part MultipartBody.Part attachment
    );


    @FormUrlEncoded
    @POST("api/add-clinent")
    Call<UserModel> addClient(@Header("Authorization") String bearer_token,
                              @Field("doctor_id") String doctor_id,

                              @Field("name") String name,
                              @Field("phone_code") String phone_code,
                              @Field("phone") String phone,
                              @Field("password") String password,
                              @Field("email") String email,
                              @Field("country_code") String country_code


    );

    @POST("api/create-prescription")
    Call<SingleOrderDataModel> addOrder(@Header("Authorization") String bearer_token,
                                        @Body AddPrescriptionModel model);


    @GET("api/setting")
    Call<SettingDataModel> getSetting(@Header("lang") String lang
    );

    @FormUrlEncoded
    @POST("api/contact-us")
    Call<ResponseModel> contactUs(@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("phone") String phone,
                                  @Field("message") String message


    );

    @GET("api/show-user-prescriptions")
    Call<ClientPrescriptionDetailsDataModel> getClientPrescription(@Header("Authorization") String bearer_token,
                                                                   @Query(value = "lang") String lang,
                                                                   @Query(value = "client_id") int client_id

    );

    @GET("api/show-one-prescription")
    Call<SingleOrderDataModel> getClientPrescriptionDetails(@Header("Authorization") String bearer_token,
                                                            @Query(value = "lang") String lang,
                                                            @Query(value = "country_code") String country_code,
                                                            @Query(value = "id") int id

    );

    @FormUrlEncoded
    @POST("api/update-firebase")
    Call<ResponseModel> updateFirebaseToken(@Header("Authorization") String token,
                                            @Field("user_id") int user_id,
                                            @Field("phone_token") String phone_token,
                                            @Field("software_type") String software_type

    );

    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseModel> logout(@Header("Authorization") String user_token,
                               @Field("phone_token") String phone_token,
                               @Field("user_id") int user_id,
                               @Field("software_type") String software_type
    );


    @GET("api/home-doctor")
    Call<ChartDataModel> getCharts(@Header("Authorization") String bearer_token,
                                   @Query(value = "doctor_id") int doctor_id

    );


}