package kr.co.citizoomproject.android.citizoom;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.Interest.InterestingRepJsonAdapter;
import kr.co.citizoomproject.android.citizoom.Member.RepEntityObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-24.
 */
public class AsyncMemberJSONList extends AsyncTask<String, Integer, ArrayList<RepEntityObject>> {
    public InterestingRepJsonAdapter adapter;

    public ImageView nothing;

    public AsyncMemberJSONList(InterestingRepJsonAdapter adapter, ImageView nothing) {
        this.adapter = adapter;
        this.nothing = nothing;
    }

    @Override
    protected ArrayList<RepEntityObject> doInBackground(String... params) {
        Response response = null;
        try {
            //OKHttp3사용ㄴ
            OkHttpClient client = MySingleton.sharedInstance().httpClient;

            Request request = new Request.Builder()
                    .url(NetworkDefineConstant.SERVER_URL_INTEREST_REP)
                    .build();
            //동기 방식
            response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            //responseBody.string();
            boolean flag = response.isSuccessful();
            //응답 코드 200등등
            int responseCode = response.code();
            if (flag) {
                return ParseDataParseHandler.getJSONMemberRequestAllList(
                        new StringBuilder(responseBody.string()));
            }
        } catch (UnknownHostException une) {
            e("관심의원1", une.toString());
        } catch (UnsupportedEncodingException uee) {
            e("관심의원2", uee.toString());
        } catch (Exception e) {
            e("관심의원3", e.toString());
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
    protected void onPostExecute(ArrayList<RepEntityObject> result) {
        if(result == null || result.size() <= 0) {
            if (nothing != null) {
                nothing.setVisibility(View.VISIBLE);
            }
        }

        if (result != null && result.size() > 0 && nothing != null) {
            nothing.setVisibility(View.GONE);
            adapter.repEntityObjects = result;
            adapter.notifyDataSetChanged();

/*
            InterestingRepJsonReqAdapter interestingRepJsonReqAdapter = new InterestingRepJsonReqAdapter(owner, result);
            interestingRepJsonReqAdapter.notifyDataSetChanged();
            rv.setAdapter(interestingRepJsonReqAdapter);
*/
        }
    }
}