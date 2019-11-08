package com.trdevendran.assignmentpsi.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trdevendran.assignmentpsi.BuildConfig;
import com.trdevendran.assignmentpsi.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class generates an instance of API service interface with retrofit builder
 * It configures to show the logs of Request body for the developer's observation
 */
public class RetrofitBuilder {

    public static EndPointService getInstance() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(EndPointService.class);
    }
}