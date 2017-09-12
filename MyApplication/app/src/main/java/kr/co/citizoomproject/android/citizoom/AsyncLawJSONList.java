package kr.co.citizoomproject.android.citizoom;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.Interest.InterestLawFragment;
import kr.co.citizoomproject.android.citizoom.Interest.InterestLawJSONReqListAdapter;
import kr.co.citizoomproject.android.citizoom.Law.LawEntityObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-24.
 * 관심법안 리스트 호출
 */
public class AsyncLawJSONList extends AsyncTask<String, Integer, ArrayList<LawEntityObject>> {

    public ImageView nothing;
    public InterestLawJSONReqListAdapter adapter;

    @Override
    protected ArrayList<LawEntityObject> doInBackground(String... params) {
        Response response = null;
        try {
            //OKHttp3사용ㄴ
            OkHttpClient client = MySingleton.sharedInstance().httpClient;

            Request request = new Request.Builder()
                    .url(NetworkDefineConstant.SERVER_URL_INTEREST_LAW)
                    .build();
            //동기 방식
            response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();

            boolean flag = response.isSuccessful();
            //응답 코드 200등등
            //int responseCode = response.code();
            if (flag) {
                return ParseDataParseHandler.getJSONInterestLawRequestAllList(
                        new StringBuilder(responseBody.string()));
            }
        } catch (UnknownHostException une) {
            e("fileUpLoad", une.toString());
        } catch (UnsupportedEncodingException uee) {
            e("fileUpLoad", uee.toString());
        } catch (Exception e) {
            e("fileUpLoad", e.toString());
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
    protected void onPostExecute(ArrayList<LawEntityObject> result) {

        if (result == null || result.size() <= 0) {
            if (nothing != null) {
                nothing.setVisibility(View.VISIBLE);
            }
        }

        if (result != null && result.size() > 0) {
            adapter.lawEntityObjects = result;
            adapter.notifyDataSetChanged();

/*            InterestLawJSONReqListAdapter interestLawJSONReqListAdapter = new InterestLawJSONReqListAdapter(InterestLawFragment.owner, result);
            recyclerView.setAdapter(interestLawJSONReqListAdapter);
            //recyclerView.swapAdapter(interestLawJSONReqListAdapter, false);
            interestLawJSONReqListAdapter.notifyDataSetChanged();*/
        }
    }
}
