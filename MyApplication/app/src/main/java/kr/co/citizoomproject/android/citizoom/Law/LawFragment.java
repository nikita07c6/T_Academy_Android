package kr.co.citizoomproject.android.citizoom.Law;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.Main.MainActivity;
import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.NetworkDialog;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-27.
 */
public class LawFragment extends Fragment {
    public static RecyclerView rv;
    static int filterFlag = 0;
    static MainActivity owner;
    static int pageValue = 1;
    public RecyclerView rvTop;
    SwipyRefreshLayout refreshLayout;
    ArrayList<LawEntityObject> lawlist = new ArrayList();
    LawJSONReqListAdapter lawJSONReqListAdapter = new LawJSONReqListAdapter(this.getActivity(), lawlist);
    Handler handler = new Handler(Looper.getMainLooper());
    ImageView nothing_search;
    ArrayList<String> committee = new ArrayList<>();
    ArrayList<String> month = new ArrayList<>();
    String year, proposer;
    private String filterString;
    private TextView filter_str;

    public LawFragment() {
    }

    public static LawFragment newInstance(int initValue) {
        LawFragment lawFragment = new LawFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        lawFragment.setArguments(bundle);
        return lawFragment;
    }

    public void refreshLaw(Bundle bundle) {
        this.proposer = bundle.getString("proposer");
        this.year = bundle.getString("year");
        this.month = bundle.getStringArrayList("month");
        this.committee = bundle.getStringArrayList("committee");
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncLawJSONList().execute("");
        //new AsyncLawTopJSONList().execute();
        Log.i("부르니", "ture");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.law_fragment, container, false);
        owner = (MainActivity) getActivity();

        ImageView nothing_search = (ImageView) view.findViewById(R.id.nothing_search);

        filter_str = (TextView) view.findViewById(R.id.filter_contents);


        ImageView law_search = (ImageView) view.findViewById(R.id.law_search_Btn);
        law_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LawSearchActivity.class);
                startActivity(intent);
            }
        });

        ImageView law_filter = (ImageView) view.findViewById(R.id.filter_Btn);
        law_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LawFilterActivity.class);
                startActivityForResult(intent, MainActivity.FILTER_SELECTED);

            }
        });

        rvTop = (RecyclerView) view.findViewById(R.id.law_top);
        rvTop.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));
        rvTop.setAdapter(lawJSONReqListAdapter);

        rv = (RecyclerView) view.findViewById(R.id.law_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));
        rv.setAdapter(lawJSONReqListAdapter);

        refreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipeLayout);

        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);


        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    int k = pageValue;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AsyncLawJSONList().execute();
                            refreshLayout.setRefreshing(false);
                        }
                    }, 2000);
                    refreshLayout.setRefreshing(true);
                    pageValue = k;
                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AsyncLawJSONList2().execute();
                            refreshLayout.setRefreshing(false);
                        }
                    }, 2000);
                    refreshLayout.setRefreshing(true);
                }
            }

        });


        lawJSONReqListAdapter = new LawJSONReqListAdapter(null, new ArrayList<LawEntityObject>());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AsyncLawJSONList().execute("");
    }

    public class AsyncLawJSONList extends AsyncTask<String, Integer, ArrayList<LawEntityObject>> {
        ProgressDialog dialog;
        //NetworkDialog netwrokDialog = new NetworkDialog(owner.getApplicationContext());
        private String targetURL;

        @Override
        protected ArrayList<LawEntityObject> doInBackground(String... params) {
            Response response = null;
            if (filterFlag == 1) {
                targetURL = String.format(NetworkDefineConstant.SERVER_URL_LAW + "/filter?page=" + (pageValue++));
                int clength = committee.size();
                for (int i = 0; i < clength; i++) {
                    targetURL += "&committee=" + committee.get(i);
                }

                int mlength = month.size();
                for (int i = 0; i < mlength; i++) {
                    targetURL += "&month=" + month.get(i);
                }

                targetURL += "&year=" + year;
                targetURL += "&publish=" + proposer;

                Log.i("tag : ", targetURL);

            }
            if (filterFlag == 0) {
                targetURL = String.format(NetworkDefineConstant.SERVER_URL_LAW + "?page=" + (pageValue++));
                Log.i("tag : ", targetURL);
            }

            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .header("user_id", PropertyManager.getInstance().getUserId())
                        .url(targetURL)
                        .build();
                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                //int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONLawRequestAllList(
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
            //netwrokDialog.show();
            dialog = ProgressDialog.show(owner, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<LawEntityObject> result) {
            //netwrokDialog.dismiss();
            dialog.dismiss();
            if (result == null || (filterFlag == 1 &&result.size() <= 0)) {
                filterString = "필터를 선택해주세요.";
                filter_str.setText(filterString);
                Toast.makeText(getContext(), "필터 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            }

            if (result != null && result.size() > 0) {
                lawJSONReqListAdapter.addItemsData(result);
                lawJSONReqListAdapter.notifyDataSetChanged();
                rv.setAdapter(lawJSONReqListAdapter);

                if (!TextUtils.isEmpty(year)){
                    filterString = proposer;

                    filterString += " / " + year + "년";

                    int mlength = month.size();
                    for (int i = 0; i < mlength; i++) {
                        filterString += " / " + month.get(i) + "월";
                    }

                    int clength = committee.size();
                    for (int i = 0; i < clength; i++) {
                        filterString += " / " + committee.get(i);
                    }

                    filter_str.setText(filterString);

                }

            } // if result있을 때

        }
    } // Async

    public class AsyncLawJSONList2 extends AsyncTask<String, Integer, ArrayList<LawEntityObject>> {
        ProgressDialog dialog;
        private String targetURL;

        @Override
        protected ArrayList<LawEntityObject> doInBackground(String... params) {
            Response response = null;
            if (filterFlag == 1) {
                targetURL = String.format(NetworkDefineConstant.SERVER_URL_LAW + "/filter?page=" + (pageValue++));
                int clength = committee.size();
                for (int i = 0; i < clength; i++) {
                    targetURL += "&committee=" + committee.get(i);
                }

                int mlength = month.size();
                for (int i = 0; i < mlength; i++) {
                    targetURL += "&month=" + month.get(i);
                }

                targetURL += "&year=" + year;
                targetURL += "&publish=" + proposer;

                Log.i("tag : ", targetURL);

            }
            if (filterFlag == 0) {
                targetURL = String.format(NetworkDefineConstant.SERVER_URL_LAW + "?page=" + (pageValue++));
                Log.i("tag : ", targetURL);
            }
            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .header("user_id", PropertyManager.getInstance().getUserId())
                        .url(targetURL)
                        .build();
                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                //int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONLawRequestAllList(
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
            dialog = ProgressDialog.show(owner, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<LawEntityObject> result) {
            dialog.dismiss();

            if (result != null && result.size() > 0) {
                lawJSONReqListAdapter.addItemsData(result);
                lawJSONReqListAdapter.notifyDataSetChanged();
                rv.setAdapter(lawJSONReqListAdapter);
            }
        }
    }

    public class AsyncLawTopJSONList extends AsyncTask<String, Integer, ArrayList<LawEntityObject>> {
        ProgressDialog dialog;

        @Override
        protected ArrayList<LawEntityObject> doInBackground(String... params) {
            Response response = null;

            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .header("user_id", PropertyManager.getInstance().getUserId())
                        .url(NetworkDefineConstant.SERVER_URL_LAW_TOP)
                        .build();
                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                //int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONLawRequestAllList(
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
            dialog = ProgressDialog.show(owner, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<LawEntityObject> result) {
            dialog.dismiss();

            if (result != null && result.size() > 0) {
                lawJSONReqListAdapter.addItemsData(result);
                lawJSONReqListAdapter.notifyDataSetChanged();
                rvTop.setAdapter(lawJSONReqListAdapter);
            }
        }
    }

}
