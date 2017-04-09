package ru.dmitriyivanov.producthunt.ProductHuntApi;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Developer - WALKER
 * Date - 09.04.2017
 * Project - ProductHunt
 */

public class ApiFactory {
    private static OkHttpClient sHttpClient;
    private static Retrofit sRetrofit;

    private static Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder().addHeader("Authorization", ApiConf.api_token).build();
            return chain.proceed(request);
        }
    };

    static {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(headerInterceptor);
        sHttpClient = httpClientBuilder.build();
    }

    @NonNull
    public static ProductHuntService getProductHuntService() {
        return getRetrofit().create(ProductHuntService.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        if(sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiConf.api_host)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(sHttpClient)
                    .build();
        }
        return sRetrofit;
    }
}
