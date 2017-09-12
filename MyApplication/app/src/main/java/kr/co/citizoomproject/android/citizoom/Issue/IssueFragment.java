package kr.co.citizoomproject.android.citizoom.Issue;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

import kr.co.citizoomproject.android.citizoom.AsyncLawJSONList;
import kr.co.citizoomproject.android.citizoom.Interest.InterestLawJSONReqListAdapter;
import kr.co.citizoomproject.android.citizoom.Main.MainActivity;
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
 * Created by ccei on 2016-07-28.
 */
public class IssueFragment extends Fragment {
    public static RecyclerView rv;
    static MainActivity owner;
    RecyclerView rvTop;
    ArrayList<IssueGetObject> issues = new ArrayList<>();
    ImageView nothing;
    private IssueRecyclerAdapter issueRecyclerAdapter;

    public IssueFragment() {
    }

    public static IssueFragment newInstance(int initValue) {
        IssueFragment issueFragment = new IssueFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        issueFragment.setArguments(bundle);
        return issueFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncIssueJsonList(nothing, issueRecyclerAdapter).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.issue_fragment, container, false);

        owner = (MainActivity) getActivity();
        nothing = (ImageView) view.findViewById(R.id.no_issue);

        issueRecyclerAdapter = new IssueRecyclerAdapter(owner, issues);

        rv = (RecyclerView) view.findViewById(R.id.issue_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));
        rv.setAdapter(issueRecyclerAdapter);

        return view;
    }

    public class AsyncIssueJsonList extends AsyncTask<String, Integer, ArrayList<IssueGetObject>> {
        ProgressDialog dialog;
        public ImageView nothing;
        public IssueRecyclerAdapter adapter;

        public AsyncIssueJsonList(ImageView nothing, IssueRecyclerAdapter adapter) {
            this.nothing = nothing;
            this.adapter = adapter;
        }

        @Override
        protected ArrayList<IssueGetObject> doInBackground(String... params) {
            Response response = null;

            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        //.header("user_id", PropertyManager.getInstance().getFaceBookId())
                        .url(NetworkDefineConstant.SERVER_URL_ISSUE_INSERT)
                        .build();

                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                //int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONIssueRequestAllList(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("이슈받기1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("이슈받기2", uee.toString());
            } catch (Exception e) {
                e("이슈받기3", e.toString());
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
            dialog = ProgressDialog.show(owner, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<IssueGetObject> result) {
            dialog.dismiss();

            if (result == null || result.size() <= 0) {
                nothing.setVisibility(View.VISIBLE);
            }

            if (result != null && result.size() > 0) {
                //nothing.setVisibility(View.GONE);
                issueRecyclerAdapter.issueGetObjects = result;
                issueRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }


}