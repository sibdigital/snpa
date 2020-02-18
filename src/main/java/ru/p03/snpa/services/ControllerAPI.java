package ru.p03.snpa.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ControllerAPI {

    public ControllerAPI() {
    }

    public static ServiceAPI getApi(String BASE_URL) {
        Gson gson = new GsonBuilder()
                .setDateFormat("dd.MM.yyyy")
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(200, TimeUnit.SECONDS)
                .connectTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit.create(ServiceAPI.class);

    }


}
