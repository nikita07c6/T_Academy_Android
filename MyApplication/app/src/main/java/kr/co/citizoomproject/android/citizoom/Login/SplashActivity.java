package kr.co.citizoomproject.android.citizoom.Login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.Interest.InterestingRepJsonAdapter;
import kr.co.citizoomproject.android.citizoom.Join.JoinActivity;
import kr.co.citizoomproject.android.citizoom.Main.MainActivity;
import kr.co.citizoomproject.android.citizoom.Member.RepEntityObject;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-12.
 */
public class SplashActivity extends AppCompatActivity {
    private SplashView mSplashView;
    private MediaPlayer mMediaPlayer;

    Handler splashHandler;
    private FrameLayout frame;

    private MediaPlayer.OnPreparedListener videoPreparedListener = new MediaPlayer.OnPreparedListener(){
        public void onPrepared(MediaPlayer mp){
            if(mp != null && !mp.isPlaying()) {
                mp.start();
            }
        }
    };
    private MediaPlayer.OnCompletionListener videoCompletionListener = new MediaPlayer.OnCompletionListener(){
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
            //startMainActivity();
        }
    };

    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //사용자가 언제나 허락
            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    //사용자가 퍼미션을 OK했을 경우

                } else {


                    //사용자가 퍼미션을 거절했을 경우
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        checkPermission();

        frame = (FrameLayout) findViewById(R.id.frame);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        final String userId = PropertyManager.getInstance().getUserId();

        //페북에서 토큰을 발급받았는지 유무를 체크
        final String facebookToken = PropertyManager.getInstance().getFieldFacebookTokenKey();
        final String kakaoToken = PropertyManager.getInstance().getFieldKakaoAccessTokenKey();

        splashHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                Intent intent;

                // token 존재하면 무조건 token을 한번 보냄
                if (!TextUtils.isEmpty(facebookToken)) {
                    new FacebookTokenAsync(new FacebookTokenAsync.FacebookTokenAsyncTaskCompletion() {
                        @Override
                        public void onComplete() {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            SplashActivity.this.startActivity(intent);
                            SplashActivity.this.finish();
                        }
                    }).execute();

                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                /*if (!TextUtils.isEmpty(kakaoToken)) {
                    new KakaoTokenAsyncTask(new KakaoTokenAsyncTask.KakaoTokenAsyncTaskCompletion() {
                        @Override
                        public void onComplete() {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            SplashActivity.this.startActivity(intent);
                            SplashActivity.this.finish();
                        }
                    }).execute();

                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }*/

/*
                intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
*/


/*
                if(TextUtils.isEmpty(userId)){
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
*/

                finish();
            }
        };
    }


    @Override
    public void onResume() {
        super.onResume();
        splashHandler.sendEmptyMessageDelayed(0, 2000);

        mSplashView = new SplashView(this);
        frame.addView(mSplashView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void releaseMediaPlayer() {
        if(mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private class SplashView extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder holder;
        private Context context;

        private void initView() {
            holder = getHolder();
            holder.addCallback(this);
        }

        public SplashView(Context context) {
            super(context);
            this.context = context;
            initView();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if(mMediaPlayer == null){
                setVideoMediaPlayer(holder);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {  releaseMediaPlayer();  }

        public void setVideoMediaPlayer(SurfaceHolder holder){
            if(mMediaPlayer == null) mMediaPlayer = new MediaPlayer();
            else mMediaPlayer.reset();

            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);

            try {
                mMediaPlayer.setDataSource(context, uri);
                mMediaPlayer.setDisplay(holder);
                mMediaPlayer.setOnPreparedListener(videoPreparedListener);
                mMediaPlayer.setOnCompletionListener(videoCompletionListener);
                mMediaPlayer.prepare();
            } catch (Exception e) {
                //startMainActivity();
            }
        }
    }

}