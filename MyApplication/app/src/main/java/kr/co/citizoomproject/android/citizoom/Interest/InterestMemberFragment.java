package kr.co.citizoomproject.android.citizoom.Interest;

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
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.Member.RepEntityObject;
import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-01.
 */
public class InterestMemberFragment extends Fragment {
    InterestLawMemberActivity owner;
    RecyclerView rv;
    static ImageView nothing;
    public InterestMemberFragment() {
    }

    public static InterestMemberFragment newInstance(int initValue) {
        InterestMemberFragment interestMemberFragment = new InterestMemberFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        interestMemberFragment.setArguments(bundle);
        return interestMemberFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.interest_member_fragment, container, false);
        owner = (InterestLawMemberActivity)getActivity();

        nothing = (ImageView) view.findViewById(R.id.nothing_interest_member);

        rv = (RecyclerView) view.findViewById(R.id.interest_member_recyclerview);

        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));
        new AsyncMemberJSONList().execute("");

        return view;
    }

    public class AsyncMemberJSONList extends AsyncTask<String, Integer, ArrayList<RepEntityObject>> {
        ProgressDialog dialog;

        @Override
        protected ArrayList<RepEntityObject> doInBackground(String... params) {
            try{
                //OKHttp3사용ㄴ
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_INTEREST_REP)
                        .build();
                //동기 방식
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                //responseBody.string();
                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONMemberRequestAllList(
                            new StringBuilder(responseBody.string()));
                }
            }catch (UnknownHostException une) {
                e("fileUpLoad", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("fileUpLoad", uee.toString());
            } catch (Exception e) {
                e("fileUpLoad", e.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(owner, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<RepEntityObject> result) {
            dialog.dismiss();
            if(result == null || result.size() <= 0) {
                nothing.setVisibility(View.VISIBLE);
                rv.setVisibility(View.INVISIBLE);
            }

            if (result != null && result.size() > 0) {
                InterestingRepJsonAdapter interestingRepJsonAdapter = new InterestingRepJsonAdapter(owner, result);
                interestingRepJsonAdapter.nothing = nothing;
                rv.setAdapter(interestingRepJsonAdapter);
                interestingRepJsonAdapter.notifyDataSetChanged();
            }
        }
    }
}
