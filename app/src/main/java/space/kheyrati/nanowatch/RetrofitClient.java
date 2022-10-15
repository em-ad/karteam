package space.kheyrati.nanowatch;

import com.google.gson.GsonBuilder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private final KheyratiApi kheyratiApi;
    public static final String KHEYRATI_BASE_URL = "https://karteam.kheyrati.space/";
    public static final String API_CONTENT_TYPE_VALUE = "application/json";
    public static final String API_CONTENT_TYPE = "Content-Type";

    private RetrofitClient() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(createInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KHEYRATI_BASE_URL)
                .addConverterFactory(createGsonFactory())
                .client(httpClient)
                .build();

        kheyratiApi = retrofit.create(KheyratiApi.class);
    }

    private Converter.Factory createGsonFactory() {
        return GsonConverterFactory.create(new GsonBuilder().setLenient().create());
    }

    private Interceptor createInterceptor() {
        return chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.header(API_CONTENT_TYPE, API_CONTENT_TYPE_VALUE);
            return chain.proceed(requestBuilder.build());
        };
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public KheyratiApi getKheyratiApi() {
        return kheyratiApi;
    }
}
