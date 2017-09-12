package kr.co.citizoomproject.android.citizoom.Law;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.citizoomproject.android.citizoom.MyApplication;
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
 * Created by ccei on 2016-07-26.
 */
public class LawSearchActivity extends AppCompatActivity {
    static RecyclerView rv;
    static int pageValue = 1;
    public ArrayList<String> lawTitle = new ArrayList<>();
    SwipyRefreshLayout refreshLayout;
    Handler handler = new Handler(Looper.getMainLooper());
    LawJSONReqListAdapter lawJSONReqListAdapter;
    ArrayAdapter<String> arrayAdapter = null;
    AutoCompleteTextView autoEdit;
    ImageView nothing;

    String changeURL;
    String changeWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_law);

        nothing = (ImageView) findViewById(R.id.nothing_law);

        Toolbar toolbar = (Toolbar) findViewById(R.id.law_search_toolbar);
        setSupportActionBar(toolbar);

        autoEdit = (AutoCompleteTextView) findViewById(R.id.autoedit);

        autoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeWord = String.valueOf(editable);
            }
        });

        ImageView search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncLawJSONList3(changeWord).execute();
            }
        });


        rv = (RecyclerView) findViewById(R.id.search_law_recyler);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        refreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipeLayout);

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

        new AsyncLawJSONList().execute("");

    } // onCreate

    public class AsyncLawJSONList extends AsyncTask<String, Integer, ArrayList<LawEntityObject>> {

        @Override
        protected ArrayList<LawEntityObject> doInBackground(String... params) {
            Response response = null;
            String targetURL = String.format(NetworkDefineConstant.SERVER_URL_LAW + (pageValue++));
            Log.i("tag : ", targetURL);

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
                    return ParseDataParseHandler.getJSONSearchLawAllList(
                            new StringBuilder(responseBody.string()), lawTitle);
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
            if (result == null || result.size() <= 0){
                nothing.setVisibility(View.VISIBLE);
            }

            if (result != null && result.size() > 0) {
                LawJSONReqListAdapter lawJSONReqListAdapter = new LawJSONReqListAdapter(LawSearchActivity.this, result);
                rv.setAdapter(lawJSONReqListAdapter);
                lawJSONReqListAdapter.notifyDataSetChanged();
            }

            arrayAdapter = new ArrayAdapter<String>(MyApplication.getMyContext(), R.layout.autocomplete_item, lawTitle);
            autoEdit.setAdapter(arrayAdapter);

        }
    }


    public class AsyncLawJSONList2 extends AsyncTask<String, Integer, ArrayList<LawEntityObject>> {
        ProgressDialog dialog;

        @Override
        protected ArrayList<LawEntityObject> doInBackground(String... params) {
            Response response = null;
            String targetURL = String.format(NetworkDefineConstant.SERVER_URL_LAW + (pageValue++));
            Log.i("tag : ", targetURL);

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
                    return ParseDataParseHandler.getJSONSearchLawAllList(
                            new StringBuilder(responseBody.string()), lawTitle);
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
            dialog = ProgressDialog.show(LawSearchActivity.this, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<LawEntityObject> result) {
            dialog.dismiss();

            if (result != null && result.size() > 0) {
                lawJSONReqListAdapter.addItemsData(result);
                lawJSONReqListAdapter.notifyDataSetChanged();
                rv.setAdapter(lawJSONReqListAdapter);
            }

            arrayAdapter = new ArrayAdapter<String>(MyApplication.getMyContext(), R.layout.autocomplete_item, lawTitle);
            autoEdit.setAdapter(arrayAdapter);
            Log.e("lawArrayList222", lawTitle.get(0));

        }
    }

    public class AsyncLawJSONList3 extends AsyncTask<String, Integer, ArrayList<LawEntityObject>> {
        ProgressDialog dialog;

        CharSequence sequence;

        public AsyncLawJSONList3(CharSequence sequence) {
            this.sequence = sequence;
        }

        @Override
        protected ArrayList<LawEntityObject> doInBackground(String... params) {
            Response response = null;
            changeURL = "http://52.78.101.62:3000/laws/search?text=" + sequence;
            Log.i("tag : ", changeURL);

            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .header("user_id", PropertyManager.getInstance().getUserId())
                        .url(changeURL)
                        .build();
                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                //int responseCode = response.code();
                if (flag) {
                    lawTitle.clear();
                    return ParseDataParseHandler.getJSONSearchLawAllList(
                            new StringBuilder(responseBody.string()), lawTitle);
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
            dialog = ProgressDialog.show(LawSearchActivity.this, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<LawEntityObject> result) {
            dialog.dismiss();
            if (result.size() == 0 || result == null) {
                nothing.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
            Log.e("결과사이즈", String.valueOf(result.size()));

            if (result != null && result.size() > 0) {
                nothing.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);

                lawJSONReqListAdapter.addItemsData(result);
                lawJSONReqListAdapter.notifyDataSetChanged();
                rv.setAdapter(lawJSONReqListAdapter);

                arrayAdapter = new ArrayAdapter<String>(MyApplication.getMyContext(), R.layout.autocomplete_item, lawTitle);
                autoEdit.setAdapter(arrayAdapter);
                Log.e("lawArrayList333", lawTitle.get(0));
            }


        }
    }

}
