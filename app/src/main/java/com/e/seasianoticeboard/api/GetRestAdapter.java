package com.e.seasianoticeboard.api;

import com.e.seasianoticeboard.api.GitHubService;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class GetRestAdapter {
//     private static final String LOCAL = "https://stgsd.appsndevs.com/ISEASIa/api/";

     private static final String LOCAL = "http://iseasia.appsndevs.com/api/";

    private static final String HOST_URL = LOCAL;
    private static final String APPLICATION_JSON = "application/json";

    private GetRestAdapter() {
    }

    public static GitHubService getRestAdapter(boolean isAuthRequired) {
        GitHubService retrofitInterface = null;
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder();
        builder.readTimeout(90000, TimeUnit.SECONDS);
        builder.connectTimeout(30000, TimeUnit.SECONDS);
        //Print Api Logs
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.interceptors().add(logging);
        OkHttpClient okHttpClient=new OkHttpClient();


//        PrefStore sharedPref = new PrefStore(App.app);
//        final String token = sharedPref.getString(PreferenceKeys.LOGIN_TOKEN);

        if (isAuthRequired) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().
                          //  addHeader("Authorization", "Bearer" + " " + token).
                            addHeader("Content-Type", APPLICATION_JSON).
                            addHeader("accept", APPLICATION_JSON).build();
                    return chain.proceed(request);
                }
            });
        } else {
            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Content-Type", APPLICATION_JSON)
                            .addHeader("accept", APPLICATION_JSON).build();
                    return chain.proceed(request);
                }
            });
        }

        retrofitInterface = new Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build()
                .create(GitHubService.class);
        return retrofitInterface;
    }
}
