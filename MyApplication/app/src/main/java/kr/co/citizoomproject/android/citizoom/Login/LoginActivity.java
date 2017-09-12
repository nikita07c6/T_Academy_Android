package kr.co.citizoomproject.android.citizoom.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import kr.co.citizoomproject.android.citizoom.Join.JoinActivity;
import kr.co.citizoomproject.android.citizoom.Main.MainActivity;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-23.
 */
public class LoginActivity extends Activity {

    private ImageView facebookLogin;
    private CallbackManager callbackManager;
    private ImageView login_button;
    private SessionCallback mKakaocallback;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //반드시 초기화 해야한다.
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.login_activity);

        facebookLogin = (ImageView) findViewById(R.id.facebook_login);

        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인을 하고자 하는 페북사용자의 role을 선언한다.
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "email", "user_friends"));
            }
        });

                /*
          Facebook 로그인대한 콜백이벤트 등록(반드시 Main Thread에서 선언해야 한다.)
         */
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            public void onSuccess(LoginResult loginResult) {
                //성공하면 토큰정보를 이용해 다양한 값을 설정또는 알아낼수 있다
                        /*final AccessToken token = AccessToken.getCurrentAccessToken();
                        token.getUserId();
                        token.getLastRefresh();
                        token.getApplicationId();
                        token.getExpires();*/

                //해당 토큰을 통해 발급받은 사용자 ID 또는 Token값을
                //가져온다.
                String userId = loginResult.getAccessToken().getUserId();
                Log.e("fbUserId", String.valueOf(userId));

                String accessToken;

                accessToken = loginResult.getAccessToken().getToken();
                Log.e("accessToken", String.valueOf(accessToken));

                //Preference에 저장한다.
                PropertyManager.getInstance().setFacebookId(userId);
                PropertyManager.getInstance().setFacebookToken(accessToken);
                PropertyManager.getInstance().setUserId(userId);

                new FacebookTokenAsync(new FacebookTokenAsync.FacebookTokenAsyncTaskCompletion() {
                    @Override
                    public void onComplete() {
                        new AsyncNewJSONList().execute();
                        /*Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();*/
                    }
                }).execute();

                //MainActivity로 전달한다.
//                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
//                intent.putExtra("token", accessToken);
//                startActivity(intent);
//                finish();
            }

            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
            }
        });
//**********************************************************************************************************************************************************************

        login_button = (ImageView) findViewById(R.id.kakaotalk_login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 카카오 로그인 요청
                isKakaoLogin();
                //// TODO: 2016-08-26  
                Log.e("카카오로그인","눌림");
            }
        });


    }

    public class AsyncNewJSONList extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            try {
                //OKHttp3사용ㄴ
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NEW_USER)
                        .build();
                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                //responseBody.string();
                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    StringBuilder buf = new StringBuilder(response.body().string());

                    JSONObject jsonObject = new JSONObject(buf.toString());
                    String msg = jsonObject.getString("msg");
                    Log.i("msg",msg);
                    return msg;
                }
            } catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent;
            if(result.equals("User")) {
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (result.equals("New")){
                intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        }

    }



    private void isKakaoLogin() {
        // 카카오 세션을 오픈한다
        mKakaocallback = new SessionCallback();
        com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN,LoginActivity.this);

        String accessToken = Session.getCurrentSession().getAccessToken();
        String refreshToken = Session.getCurrentSession().getRefreshToken();
        Log.e("kakaoAccessToken", accessToken);
        Log.e("kakaoRefreshToken", refreshToken);

        PropertyManager.getInstance().setFieldKakaoAccessTokenKey(accessToken);
        PropertyManager.getInstance().setFieldKakaoRefreshTokenKey(refreshToken);

        new KakaoTokenAsyncTask(this, accessToken, new KakaoTokenAsyncTask.KakaoTokenAsyncTaskCompletion() {
            @Override
            public void onComplete() {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
        }).execute();


    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.d("TAG" , "세션 오픈됨");
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴

            KakaorequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Log.d("TAG" , exception.getMessage());
            }
        }
    }
    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void KakaorequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG" , "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("TAG" , "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                String userId = String.valueOf(userProfile.getId());
                PropertyManager.getInstance().setUserId(userId);
                Log.e("kakaoId", userId);
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //페북의 Activity Callback을 담당하는 메소드
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void getHashKey() {
        try {
            //FaceBook에 등록된 현재 애플리케이션의 서명을 통한 SHA 암호화 값(Key Hash)을 얻어온다.
            PackageInfo info = getPackageManager().getPackageInfo(
                    "kr.co.citizoomproject.android.citizoom",  //Facebook 개발자에 등록된 내 애플리케이션의 Package Name
                    PackageManager.GET_SIGNATURES); //내 팩키지를 이용한 서명을 만드기 위함
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("Key Hash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("FBLoginError", "NameNotFound", e);
        } catch (NoSuchAlgorithmException e) {
            Log.e("FBLoginError", "NoSuchAlgorithm", e);
        }

    }
}
