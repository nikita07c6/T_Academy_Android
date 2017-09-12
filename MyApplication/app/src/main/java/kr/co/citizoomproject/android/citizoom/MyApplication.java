package kr.co.citizoomproject.android.citizoom;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.kakao.auth.KakaoSDK;
import com.tsengvn.typekit.Typekit;

import kr.co.citizoomproject.android.citizoom.Login.KakaoSDKAdapter;

/**
 * Created by ccei on 2016-07-07.
 */
public class MyApplication extends Application {
    private static Context mContext;

    public static Context getMyContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "NanumBarunGothic.ttf"))
                .addBold(Typekit.createFromAsset(this, "NanumBarunGothicBold.ttf"));

        mInstance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    private static MyApplication mInstance;
    private static volatile Activity currentActivity = null;

    public static Activity getCurrentActivity() {
        Log.d("TAG", "++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        MyApplication.currentActivity = currentActivity;
    }

    /**
     * singleton
     * @return singleton
     */
    public static MyApplication getGlobalApplicationContext() {
        if(mInstance == null)
            throw new IllegalStateException("this application does not inherit GlobalApplication");
        return mInstance;
    }

}

