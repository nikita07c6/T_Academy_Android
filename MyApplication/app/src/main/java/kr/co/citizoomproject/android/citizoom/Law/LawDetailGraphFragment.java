package kr.co.citizoomproject.android.citizoom.Law;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

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
 * Created by ccei on 2016-07-29.
 */
public class LawDetailGraphFragment extends Fragment {
    static LawDetailActivity owner;
    private TextView total_count;
    private TextView selected_count;
    private LinearLayout mper20, mper30, mper40, mper50, mper60;
    private LinearLayout fper20, fper30, fper40, fper50, fper60;
    private TextView male_count, female_count;

    public LawDetailGraphFragment() {
    }

    public static LawDetailGraphFragment newInstance(int initValue) {
        LawDetailGraphFragment lawDetailGraphFragment = new LawDetailGraphFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        lawDetailGraphFragment.setArguments(bundle);
        return lawDetailGraphFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncLawGraphJSONList(1).execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.law_detail_graph, container, false);

        final ImageView agree =  (ImageView) view.findViewById(R.id.law_detail_agree);
        final ImageView disagree = (ImageView) view.findViewById(R.id.law_detail_disagree);
        final RelativeLayout agreeRelative = (RelativeLayout) view.findViewById(R.id.agree_relative);

        final ImageView opinion = (ImageView) view.findViewById(R.id.opinion);
        final ImageView text_opinion = (ImageView) view.findViewById(R.id.text_opinion);
        total_count = (TextView) view.findViewById(R.id.total_count);
        selected_count = (TextView) view.findViewById(R.id.selected_count);

        male_count = (TextView) view.findViewById(R.id.male_count);
        female_count = (TextView) view.findViewById(R.id.female_count);

        mper20 = (LinearLayout) view.findViewById(R.id.mper20);
        mper30 = (LinearLayout) view.findViewById(R.id.mper30);
        mper40 = (LinearLayout) view.findViewById(R.id.mper40);
        mper50 = (LinearLayout) view.findViewById(R.id.mper50);
        mper60 = (LinearLayout) view.findViewById(R.id.mper60);

        fper20 = (LinearLayout) view.findViewById(R.id.fper20);
        fper30 = (LinearLayout) view.findViewById(R.id.fper30);
        fper40 = (LinearLayout) view.findViewById(R.id.fper40);
        fper50 = (LinearLayout) view.findViewById(R.id.fper50);
        fper60 = (LinearLayout) view.findViewById(R.id.fper60);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opinion.setImageResource(R.drawable.graph_btn_yes);
                text_opinion.setImageResource(R.drawable.text_result_yes);
                String strColor = "#699dd3";
                total_count.setTextColor(Color.parseColor(strColor));
                selected_count.setTextColor(Color.parseColor(strColor));
                if (disagree.isShown()) {
                    agree.setImageResource(R.drawable.graph_btn_like_on);
                    disagree.setVisibility(View.GONE);
                } else {
                    agree.setImageResource(R.drawable.graph_btn_like);
                    disagree.setVisibility(View.VISIBLE);
                }
                new AsyncLawGraphJSONList(1).execute();
            }
        });

        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opinion.setImageResource(R.drawable.graph_btn_no);
                text_opinion.setImageResource(R.drawable.text_result);
                String strColor = "#ed7374";
                total_count.setTextColor(Color.parseColor(strColor));
                selected_count.setTextColor(Color.parseColor(strColor));
                if (agree.isShown()) {
                    disagree.setImageResource(R.drawable.graph_btn_hate_on);
                    agreeRelative.setVisibility(View.GONE);
                    agree.setVisibility(View.GONE);
                } else {
                    disagree.setImageResource(R.drawable.graph_btn_hate_off);
                    agreeRelative.setVisibility(View.VISIBLE);
                    agree.setVisibility(View.VISIBLE);
                }
                new AsyncLawGraphJSONList(2).execute();
            }
        });

        return view;
    }

    public class AsyncLawGraphJSONList extends AsyncTask<String, Integer, LawGraphGetObject> {
        int flag;

        public AsyncLawGraphJSONList(int flag) {
            this.flag = flag;
        }

        @Override
        protected LawGraphGetObject doInBackground(String... params) {
            Response response = null;
            try {
                //OKHttp3사용ㄴ
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_LAW + "/" + LawDetailActivity.lawID + "/graph")
                        .build();


                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                //int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONLawGraphRequest(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("법안상세그래프1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("법안상세그래프2", uee.toString());
            } catch (Exception e) {
                e("법안상세그래프3", e.toString());
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
        protected void onPostExecute(LawGraphGetObject result) {
            if(result != null) {
                total_count.setText(String.valueOf(result.total));
                if (flag == 1){
                    selected_count.setText(String.valueOf(result.agree_cnt));
                    int male = result.man_agree.twenty + result.man_agree.thirty
                            + result.man_agree.forty + result.man_agree.fifty + result.man_agree.sixty;
                    int male_agree_percent = (int) (((double)male / (double)result.total) * (double)100);
                    male_count.setText(male_agree_percent + "%");

                    int female = result.woman_agree.twenty + result.woman_agree.thirty
                            + result.woman_agree.forty + result.woman_agree.fifty + result.woman_agree.sixty;
                    int female_agree_percent = (int) (((double)female / (double)result.total) * (double)100);
                    female_count.setText(female_agree_percent + "%");

                    if(result.man_agree.twenty==0 && result.man_agree.thirty==0 && result.man_agree.forty==0 && result.man_agree.fifty==0 && result.man_agree.sixty ==0 ){
                        mper20.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        mper30.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        mper40.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        mper50.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        mper60.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                    } else {
                        mper20.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.man_agree.twenty));
                        mper30.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.man_agree.thirty));
                        mper40.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.man_agree.forty));
                        mper50.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.man_agree.fifty));
                        mper60.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.man_agree.sixty));
                    }

                    if(result.woman_agree.twenty==0 && result.woman_agree.thirty==0 && result.woman_agree.forty==0 && result.woman_agree.fifty==0 && result.woman_agree.sixty ==0 ){
                        fper20.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        fper30.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        fper40.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        fper50.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        fper60.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));

                    } else {
                        fper20.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.woman_agree.twenty));
                        fper30.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.woman_agree.thirty));
                        fper40.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.woman_agree.forty));
                        fper50.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.woman_agree.fifty));
                        fper60.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.woman_agree.sixty));
                    }
                }
                if (flag == 2){
                    selected_count.setText(String.valueOf(result.disagree_cnt));
                    int male = result.man_disagree.twenty + result.man_disagree.thirty
                            + result.man_disagree.forty + result.man_disagree.fifty + result.man_disagree.sixty;
                    int male_disagree_percent = (int) (((double)male / (double)result.total) * (double)100);
                    male_count.setText(male_disagree_percent + "%");

                    int female = result.woman_disagree.twenty + result.woman_disagree.thirty
                            + result.woman_disagree.forty + result.woman_disagree.fifty + result.woman_disagree.sixty;
                    int female_disagree_percent = (int) (((double)female / (double)result.total) * (double)100);
                    female_count.setText(female_disagree_percent + "%");

                    if(result.man_disagree.twenty==0 && result.man_disagree.thirty==0 && result.man_disagree.forty==0 && result.man_disagree.fifty==0 && result.man_disagree.sixty ==0 ){
                        mper20.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        mper30.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        mper40.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        mper50.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        mper60.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                    } else {
                        mper20.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.man_disagree.twenty));
                        mper30.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.man_disagree.thirty));
                        mper40.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.man_disagree.forty));
                        mper50.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.man_disagree.fifty));
                        mper60.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.man_disagree.sixty));

                    }

                    if(result.woman_disagree.twenty==0 && result.woman_disagree.thirty==0 && result.woman_disagree.forty==0 && result.woman_disagree.fifty==0 && result.woman_disagree.sixty ==0 ){
                        fper20.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        fper30.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        fper40.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        fper50.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));
                        fper60.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)1));

                    } else {
                        fper20.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.woman_disagree.twenty));
                        fper30.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.woman_disagree.thirty));
                        fper40.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.woman_disagree.forty));
                        fper50.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.woman_disagree.fifty));
                        fper60.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,(float)result.woman_disagree.sixty));
                    }
                }


            }

        }
    }


}
