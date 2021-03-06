package es.iessaladillo.pedrojoya.pr194.di;

import android.content.Context;

import com.ashokvarma.gander.GanderInterceptor;

import java.util.concurrent.TimeUnit;

import es.iessaladillo.pedrojoya.pr194.base.TagCallFactory;
import es.iessaladillo.pedrojoya.pr194.data.Repository;
import es.iessaladillo.pedrojoya.pr194.data.RepositoryImpl;
import es.iessaladillo.pedrojoya.pr194.data.mapper.StudentMapper;
import es.iessaladillo.pedrojoya.pr194.data.remote.ApiServiceImpl;
import es.iessaladillo.pedrojoya.pr194.ui.main.MainFragmentViewModelFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Injector {

    private Injector() { }

    private static OkHttpClient okHttpClient;

    public static MainFragmentViewModelFactory provideMainFragmentViewModelFactory(Context context) {
        return new MainFragmentViewModelFactory(getRepository(context));
    }

    private static Repository getRepository(Context context) {
        return RepositoryImpl.getInstance(getApiService(context), provideStudentMapper());
    }

    private static StudentMapper provideStudentMapper() {
        return new StudentMapper();
    }

    private static ApiServiceImpl getApiService(Context context) {
        return ApiServiceImpl.getInstance(provideApi(context), provideOkHttpClient(context));
    }

    private static ApiServiceImpl.Api provideApi(Context context) {
        OkHttpClient okHttpClient = provideOkHttpClient(context);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://my-json-server.typicode.com/pedrojoya/jsonserver/")
            .client(okHttpClient)
            .callFactory(new TagCallFactory(okHttpClient))
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        return retrofit.create(ApiServiceImpl.Api.class);
    }

    private static GanderInterceptor provideGanderInterceptor(Context context) {
        return new GanderInterceptor(context).showNotification(true);
    }

    private static OkHttpClient provideOkHttpClient(Context context) {
        if (okHttpClient == null) {
            synchronized (Injector.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(provideGanderInterceptor(context))
                        .callTimeout(5, TimeUnit.SECONDS)
                        .build();
                }
            }
        }
        return okHttpClient;
    }

}
