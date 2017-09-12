package kr.co.citizoomproject.android.citizoom;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

/**
 * Created by ccei on 2016-08-22.
 */
public class MySingleton {
    private static MySingleton _instance = null;
    public OkHttpClient httpClient;

    private static OkHttpClient simpleInstance;

    private MySingleton() {
        // For Persistent Cookie - https://github.com/franmontiel/PersistentCookieJar
        CookieHandler cookieHandler = new CookieManager();
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieHandler);
        httpClient = new OkHttpClient.Builder().cookieJar(cookieJar)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }
    private MySingleton(int  dummy){
         simpleInstance = new OkHttpClient.Builder()
                         .connectTimeout(20, TimeUnit.SECONDS)
                         .readTimeout(10, TimeUnit.SECONDS)
                         .build();
    }
    public static OkHttpClient getSimpleInstance(){
         if( simpleInstance != null){
             return simpleInstance;
         }else{
             new MySingleton(1);
         }
        return simpleInstance;
    }
    public static MySingleton sharedInstance() {
        if (_instance == null) {
            _instance = new MySingleton();
        }
        return _instance;
    }
}