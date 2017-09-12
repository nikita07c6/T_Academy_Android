package kr.co.citizoomproject.android.citizoom.Member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-29.
 */
public class NationalMemberDetailNewsFragment extends Fragment {
    public static int increment;
    static RecyclerView rv;
    Activity owner;
    Activity ownerActivity;
    String ownerAcitivty;

    public NationalMemberDetailNewsFragment() {
    }

    public static NationalMemberDetailNewsFragment newInstance(int initValue) {
        NationalMemberDetailNewsFragment nationalMemberDetailNewsFragment = new NationalMemberDetailNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        nationalMemberDetailNewsFragment.setArguments(bundle);
        return nationalMemberDetailNewsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.national_member_detail_news, container, false);

        Bundle initBundle = getArguments();
        increment += initBundle.getInt("value");

        ownerActivity = getActivity();

        if (ownerActivity instanceof NationalMemberDetailActivity) {
            owner = (NationalMemberDetailActivity) getActivity();
        }
        if (ownerActivity instanceof LocalMemberDetailActivity) {
            owner = (LocalMemberDetailActivity) getActivity();
        }

        rv = (RecyclerView) view.findViewById(R.id.member_news);
        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));


        new AsyncRepNewsJSONList().execute();

        return view;
    }


    public class AsyncRepNewsJSONList extends AsyncTask<String, Integer, ArrayList<RepNewsEntityObject>> {
        ProgressDialog dialog;
        Request request;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(owner, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected ArrayList<RepNewsEntityObject> doInBackground(String... params) {
            Response response = null;
            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                if (ownerActivity instanceof NationalMemberDetailActivity) {
                    request = new Request.Builder()
                            .url(NationalMemberDetailActivity.SERVER_URL_REP_NEWS)
                    .build();
                }
                if (ownerActivity instanceof LocalMemberDetailActivity) {
                    request = new Request.Builder()
                            .url(LocalMemberDetailActivity.SERVER_URL_REP_NEWS)
                    .build();
                }

                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();

                if (flag) {
                    return ParseDataParseHandler.getJSONRepNewsRequestList(new StringBuilder(responseBody.string()));
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
        protected void onPostExecute(ArrayList<RepNewsEntityObject> result) {
            dialog.dismiss();

            if (result != null && result.size() > 0) {
                RepDetailNewsAdapter repDetailNewsAdapter = new RepDetailNewsAdapter(owner, result);
                rv.setAdapter(repDetailNewsAdapter);
                repDetailNewsAdapter.notifyDataSetChanged();
            }
        }
    }

}