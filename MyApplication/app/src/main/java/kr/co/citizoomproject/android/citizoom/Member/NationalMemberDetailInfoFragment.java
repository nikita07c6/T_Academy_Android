package kr.co.citizoomproject.android.citizoom.Member;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
public class NationalMemberDetailInfoFragment extends Fragment {
    public static int increment;
    Activity owner;
    TextView committee, career;
    ImageView call, email, facebook, twitter;
    String phone;
    String emailaddr;
    String facebook_link;
    String twitter_link;
    ArrayList<String> information = new ArrayList<String>();
    ArrayList<String> careerArr = new ArrayList<String>();
    Activity ownerActivity;

    public NationalMemberDetailInfoFragment() {
    }

    public static NationalMemberDetailInfoFragment newInstance(int initValue) {
        NationalMemberDetailInfoFragment nationalMemberDetailInfoFragment = new NationalMemberDetailInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        nationalMemberDetailInfoFragment.setArguments(bundle);
        return nationalMemberDetailInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.national_member_detail_info, container, false);

        Bundle initBundle = getArguments();
        increment += initBundle.getInt("value");
        ownerActivity = getActivity();

        if (ownerActivity instanceof NationalMemberDetailActivity) {
            owner = (NationalMemberDetailActivity) getActivity();
        }
        if (ownerActivity instanceof LocalMemberDetailActivity) {
            owner = (LocalMemberDetailActivity) getActivity();
        }
        committee = (TextView) view.findViewById(R.id.real_committee);
        career = (TextView) view.findViewById(R.id.real_career);
        call = (ImageView) view.findViewById(R.id.btn_call);
        email = (ImageView) view.findViewById(R.id.btn_email);
        facebook = (ImageView) view.findViewById(R.id.btn_facebook);
        twitter = (ImageView) view.findViewById(R.id.btn_twitter);


        new AsyncMyRepInfoJSONList().execute();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("plain/text");

                String[] tos = {emailaddr};
                it.putExtra(Intent.EXTRA_EMAIL, tos);
                it.putExtra(Intent.EXTRA_SUBJECT, "제목");
                it.putExtra(Intent.EXTRA_TEXT, "내용을 입력하세요.");
                startActivity(it);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook_link));
                startActivity(intent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter_link));
                startActivity(intent);
            }
        });



        return view;
    }

    public class AsyncMyRepInfoJSONList extends AsyncTask<String, Integer, RepEntityObject> {
        Request request;

        @Override
        protected RepEntityObject doInBackground(String... params) {
            Response response = null;
            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                if (ownerActivity instanceof NationalMemberDetailActivity) {
                    request = new Request.Builder()
                            .header("user_id", PropertyManager.getInstance().getUserId())
                            .url(NationalMemberDetailActivity.SERVER_URL_REP_DETAIL)
                            .build();
                }
                if (ownerActivity instanceof LocalMemberDetailActivity) {
                    request = new Request.Builder()
                            .header("user_id", PropertyManager.getInstance().getUserId())
                            .url(LocalMemberDetailActivity.SERVER_URL_REP_DETAIL)
                            .build();
                }

                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                //responseBody.string();
                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONMyRepRequestAllList(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("Rep_une", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("Rep_uee", uee.toString());
            } catch (Exception e) {
                e("Rep_e", e.toString());
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
        protected void onPostExecute(final RepEntityObject result) {

            if (result != null) {

                int committeeSize = result.committee.size();
                for(int i =0; i < committeeSize; i++) {
                    information.clear();
                    information.add(result.committee.get(i));
                }

                String text="";
                for (String details : information) {
                    text = text + details + "\n";
                }
                committee.setText(text);

                int careerSize = result.career_arr.size();
                for(int i =0; i < careerSize; i++) {
                    careerArr.clear();
                    careerArr.add(result.career_arr.get(i));
                }

                String text1 = "";
                for (String details : careerArr) {
                    text1 = text1 + details + "\n";
                }
                career.setText(text1);

                phone = result.phone;
                emailaddr = result.email;
                facebook_link = result.facebook_link;
                twitter_link = result.twitter_link;


            }
        }
    }

}