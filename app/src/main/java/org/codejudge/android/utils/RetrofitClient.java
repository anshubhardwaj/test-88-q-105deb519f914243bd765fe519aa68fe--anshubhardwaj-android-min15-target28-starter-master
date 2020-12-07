package org.codejudge.android.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


        private static Retrofit retrofitone = null;


        public static Retrofit getClient() {
            if (retrofitone==null) {
                retrofitone = new Retrofit.Builder()
                        .baseUrl(ServiceConstant.Base_Url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(provideOkHttpClient())
                        .build();
            }
            return retrofitone;
        }


        private static OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

            okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);

            okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);

            okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);

        okhttpClientBuilder.addInterceptor(logging);

            return okhttpClientBuilder.build();
        }
    }

