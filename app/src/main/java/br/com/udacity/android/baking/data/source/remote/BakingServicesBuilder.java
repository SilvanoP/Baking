package br.com.udacity.android.baking.data.source.remote;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.readystatesoftware.chuck.ChuckInterceptor;

import br.com.udacity.android.baking.BuildConfig;
import br.com.udacity.android.baking.R;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BakingServicesBuilder {

    private static Retrofit mRetrofit;

    private static void initRetrofit(Context context) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


        if (BuildConfig.DEBUG) {
            // add stetho interceptor. See: chrome://inspect/#devices
            httpClient.addNetworkInterceptor(new StethoInterceptor());
            httpClient.addInterceptor(new ChuckInterceptor(context))
                    .build();
        }

        mRetrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baking_api_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

    }

    public static BakingServices build(Context context) {
        if (mRetrofit == null) {
            initRetrofit(context);
        }
        return mRetrofit.create(BakingServices.class);
    }

}
