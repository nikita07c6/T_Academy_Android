package kr.co.citizoomproject.android.citizoom.Member;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
public class NationalMemberDetailInitLawFragment extends Fragment {
    public static int increment;
    public static RecyclerView rv;
    public static int totalCount;
    static TextView total;
    Activity owner;
    Activity ownerActivity;

    public NationalMemberDetailInitLawFragment() {
    }

    public static NationalMemberDetailInitLawFragment newInstance(int initValue) {
        NationalMemberDetailInitLawFragment nationalMemberDetailInitLawFragment = new NationalMemberDetailInitLawFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        nationalMemberDetailInitLawFragment.setArguments(bundle);
        return nationalMemberDetailInitLawFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.national_member_detail_init_law_fragment, container, false);
        total = (TextView) view.findViewById(R.id.init_law_count);

        Bundle initBundle = getArguments();
        increment += initBundle.getInt("value");

        rv = (RecyclerView) view.findViewById(R.id.national_member_detail_init_law);

        ownerActivity = getActivity();

        if (ownerActivity instanceof NationalMemberDetailActivity) {
            owner = (NationalMemberDetailActivity) getActivity();
        }
        if (ownerActivity instanceof LocalMemberDetailActivity) {
            owner = (LocalMemberDetailActivity) getActivity();
        }
        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));

        new AsyncRepInitLawJSONList().execute();

        return view;
    }


    public class AsyncRepInitLawJSONList extends AsyncTask<String, Integer, ArrayList<RepInitLawObject>> {
        Request request;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<RepInitLawObject> doInBackground(String... params) {
            Response response = null;
            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;


                if (ownerActivity instanceof NationalMemberDetailActivity) {
                    request = new Request.Builder()
                            .url(NationalMemberDetailActivity.SERVER_URL_REP_INIT)
                            .build();
                }
                if (ownerActivity instanceof LocalMemberDetailActivity) {
                    request = new Request.Builder()
                            .url(LocalMemberDetailActivity.SERVER_URL_REP_INIT)
                            .build();
                }

                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();

                if (flag) {
                    return ParseDataParseHandler.getJSONRepInitLawRequestList
                            (new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("111", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("222", uee.toString());
            } catch (Exception e) {
                e("333", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return null;

        }


        @Override
        protected void onPostExecute(ArrayList<RepInitLawObject> result) {
            if (result != null && result.size() > 0) {
                total.setText(String.valueOf(totalCount));
                RepDetailInitLawAdapter repDetailInitLawAdapter = new RepDetailInitLawAdapter(owner, result);
                rv.setAdapter(repDetailInitLawAdapter);
                repDetailInitLawAdapter.notifyDataSetChanged();
            }
        }
    }

}