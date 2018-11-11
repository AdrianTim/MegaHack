package com.infinityleaks.arphonecomparator.dependency;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infinityleaks.arphonecomparator.module.SharedPreferencesManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MainModule {

    public static final String BASE_URL = "http://192.168.43.203/api/";

    private Context mContext;

    public MainModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    @Singleton
    SharedPreferencesManager provideSharedPrefs() {
        return new SharedPreferencesManager(mContext);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }

}
