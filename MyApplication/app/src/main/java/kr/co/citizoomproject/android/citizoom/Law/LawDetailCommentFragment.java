package kr.co.citizoomproject.android.citizoom.Law;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-29.
 */
public class LawDetailCommentFragment extends Fragment {
    static int spinner;
    static LawDetailActivity owner;
    static RecyclerView rv;
    static TextView totalComment;
    public static int commentTotal;

    public LawDetailCommentFragment() {
    }

    public static LawDetailCommentFragment newInstance(int initValue) {
        LawDetailCommentFragment lawDetailCommentFragment = new LawDetailCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        lawDetailCommentFragment.setArguments(bundle);
        return lawDetailCommentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.law_detail_comment_fragment, container, false);
        owner = (LawDetailActivity) getActivity();

        totalComment = (TextView)view.findViewById(R.id.law_comment_count);

        final Spinner commentStandard;
        ArrayAdapter<CharSequence> adapterComment;

        commentStandard = (Spinner) view.findViewById(R.id.user_comment_standard);
        adapterComment = ArrayAdapter.createFromResource(MyApplication.getMyContext(), R.array.user_comment_standard, R.layout.spinner_layout);
        adapterComment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        commentStandard.setAdapter(adapterComment);

        final String[] selItem = new String[1];
        commentStandard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selItem[0] = (String) commentStandard.getSelectedItem();

                if(selItem[0].equals("최신순")){
                    LawDetailCommentFragment.spinner = 1;
                    new AsyncLawNewCommentJSONList(LawDetailCommentFragment.spinner,"default").execute();
                } else if(selItem[0].equals("인기순")){
                    LawDetailCommentFragment.spinner = 2;
                    new AsyncLawNewCommentJSONList(LawDetailCommentFragment.spinner,"default").execute();
                }
                Log.e("spinner", selItem[0]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final ToggleButton allBtn = (ToggleButton) view.findViewById(R.id.all);
        final ToggleButton agreeBtn = (ToggleButton) view.findViewById(R.id.agree);
        final ToggleButton disagreeBtn = (ToggleButton) view.findViewById(R.id.disagree);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agreeBtn.isChecked() || disagreeBtn.isChecked()) {
                    agreeBtn.setChecked(false);
                    disagreeBtn.setChecked(false);
                }
                new AsyncLawNewCommentJSONList(LawDetailCommentFragment.spinner,"default").execute();
            }
        });

        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allBtn.isChecked() || disagreeBtn.isChecked()){
                    allBtn.setChecked(false);
                    disagreeBtn.setChecked(false);
                }
                new AsyncLawNewCommentJSONList(LawDetailCommentFragment.spinner,"agree").execute();
            }
        });

        disagreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allBtn.isChecked() || agreeBtn.isChecked()){
                    allBtn.setChecked(false);
                    agreeBtn.setChecked(false);
                }
                new AsyncLawNewCommentJSONList(LawDetailCommentFragment.spinner,"disagree").execute();
            }
        });

        rv = (RecyclerView) view.findViewById(R.id.law_comment_recycler);
        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));

        new AsyncLawNewCommentJSONList(1,"default").execute();

        return view;
    }

    public static class AsyncLawNewCommentJSONList extends AsyncTask<String, Integer, ArrayList<LawCommentEntityObject>> {
        String AgreeDisagree;
        int sort;

        public AsyncLawNewCommentJSONList(int integer, String string) {
            this.sort = integer;
            this.AgreeDisagree = string;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<LawCommentEntityObject> doInBackground(String... params) {
            Response response = null;
            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .header("user_id", PropertyManager.getInstance().getUserId())
                        .url(LawDetailActivity.SERVER_URL_DETAIL_LAW+"comment?sort="+sort)
                        .build();

                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();

                if (flag && (AgreeDisagree.equalsIgnoreCase("default"))){
                    return ParseDataParseHandler.getJSONLawCommentRequestAllList(new StringBuilder(responseBody.string()));
                } else if (flag && (AgreeDisagree.equalsIgnoreCase("agree"))) {
                    return ParseDataParseHandler.getJSONLawCommentRequestLikeList(new StringBuilder(responseBody.string()));
                } else if (flag && (AgreeDisagree.equalsIgnoreCase("disagree"))) {
                    return ParseDataParseHandler.getJSONLawCommentRequestDislikeList(new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("fileUpLoad", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("fileUpLoad", uee.toString());
            } catch (Exception e) {
                e("fileUpLoad", e.toString());
            } finally {
                if(response != null){
                    response.close();
                }
            }
            return null;

        }


        @Override
        protected void onPostExecute(ArrayList<LawCommentEntityObject> result) {

            if (result != null && result.size() > 0) {
                totalComment.setText(String.valueOf(commentTotal));
                LawCommentJSONAdapter lawCommentJSONAdapter = new LawCommentJSONAdapter(owner, result);
                rv.setAdapter(lawCommentJSONAdapter);
                lawCommentJSONAdapter.notifyDataSetChanged();

            }
        }
    }

}