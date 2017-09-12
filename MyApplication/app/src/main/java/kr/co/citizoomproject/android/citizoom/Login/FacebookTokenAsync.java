package kr.co.citizoomproject.android.citizoom.Login;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import kr.co.citizoomproject.android.citizoom.Join.JoinActivity;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-26.
 */
public class FacebookTokenAsync extends AsyncTask<Void, Void, String> {

    private FacebookTokenAsyncTaskCompletion completion;

    interface FacebookTokenAsyncTaskCompletion {
        public void onComplete();
    }

    private LoginActivity loginActivity;
    boolean flag;

    public FacebookTokenAsync(FacebookTokenAsyncTaskCompletion completion) {
        this.completion = completion;
    }

    @Override
    protected String doInBackground(Void... params) {
        Response response = null;
        FormBody fd = null;

        try {
            fd = new FormBody.Builder()
                    .add("access_token", String.valueOf(PropertyManager.getInstance().getFieldFacebookTokenKey()))
                    .build();

            OkHttpClient client = MySingleton.sharedInstance().httpClient;

            Request request = new Request.Builder()
                    .url(NetworkDefineConstant.SERVER_URL_FACEBOOK_TOKEN)
                    .post(fd)
                    .build();

            //동기 방식
            response = client.newCall(request).execute();

            flag = response.isSuccessful();
            //응답 코드 200등등
            int responseCode = response.code();
            Log.e("response==>", String.valueOf(responseCode));


            if (flag) {
                e("response결과", response.message()); //읃답에 대한 메세지(OK)
                //e("response응답바디", response.body().string()); //json으로 변신

                StringBuilder buf = new StringBuilder(response.body().string());

                JSONObject jsonObject = new JSONObject(buf.toString());

//                    String facebookID = jsonObject.getString("fbId");
//                    Log.e("facebookid", facebookID);
//                    PropertyManager.getInstance().setUserId(facebookID);
                return response.body().string();

            }

        } catch (UnknownHostException une) {
            e("facebookLogin1", une.toString());
        } catch (UnsupportedEncodingException uee) {
            e("facebookLogin2", uee.toString());
        } catch (Exception e) {
            e.printStackTrace();
            e("facebookLogin3", e.toString());
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (this.completion != null) {
            this.completion.onComplete();
        }


    }
}
