package com.infinityleaks.arphonecomparator.http;

import com.infinityleaks.arphonecomparator.model.SpecsModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface HttpService {

    @Streaming
    @GET("phones/{id}/sfb")
    Call<ResponseBody> getPhoneModelById(@Path("id") String id);

    @Streaming
    @GET("phones/model/")
    Call<ResponseBody> getPhoneModelTest();

    @Streaming
    @GET("phones/{name}/sfb")
    Call<ResponseBody> getPhoneModelByName(@Path("name") String name);

    @GET("phones/{id}")
    Call<SpecsModel> getPhoneSpecsById(@Path("id") String id);


}
