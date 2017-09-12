package kr.co.citizoomproject.android.citizoom.Member;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NationalMemberFragment;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-19.
 */
public class LocalMemberDetailActivity extends AppCompatActivity {
    String SERVER_URL_REP_LIKE;
    public static String SERVER_URL_REP_NEWS,SERVER_URL_REP_INIT,SERVER_URL_REP_DETAIL;
    int repID;
    static String party_str, facebook_str, twitter_str, name_str, district_str, age_str, elect_str, image_str;
    ArrayList<String> committee;
    ImageView party, facebook, twitter;
    TextView name, district, profile, age, elect;
    CircleImageView image;
    ToggleButton interest_mark;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncMyRepJSONList().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.national_member_detail_activity);

        interest_mark = (ToggleButton) findViewById(R.id.interest_rep_Btn);


        party = (ImageView) findViewById(R.id.party_image);
        facebook = (ImageView) findViewById(R.id.facebook);
        twitter = (ImageView) findViewById(R.id.twitter);

        name = (TextView) findViewById(R.id.my_national_member_name);
        district = (TextView) findViewById(R.id.my_national_member_district);
        profile = (TextView) findViewById(R.id.my_national_member_ex);
        age = (TextView) findViewById(R.id.my_national_member_age);
        elect = (TextView) findViewById(R.id.my_national_member_elect_num);

        image = (CircleImageView) findViewById(R.id.my_national_member_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.national_member_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_back);

        ViewPager viewPager = (ViewPager) findViewById(R.id.member_detail_viewpager);
        if (viewPager != null) {
            setupNationalMemeberDetailViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.member_tabs);
        tabLayout.setupWithViewPager(viewPager);


        repID = NationalMemberFragment.REPID;
        Log.e("localREPID", String.valueOf(repID));

        interest_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                SERVER_URL_REP_LIKE = String.format("http://%s:%d/%s/%s/%s",
                        NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER,"rep",repID,"like");
                Log.e("SERVER_URL_REP_LIKE", SERVER_URL_REP_LIKE);

                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Response response = null;
                        boolean flag = false;

                        MultipartBody.Builder MB;
                        MB = new MultipartBody.Builder();
                        MB.setType(MultipartBody.FORM);

                        try {
                            MB.addFormDataPart("user_id", PropertyManager.getInstance().getUserId());

                            OkHttpClient client = MySingleton.sharedInstance().httpClient;

                            //요청 Body 세팅==> 그전 Query Parameter세팅과 같은 개념
                            RequestBody fileUploadBody = MB.build();

                            //요청 세팅
                            Request request = new Request.Builder()
                                    .url(SERVER_URL_REP_LIKE)
                                    .post(fileUploadBody) //반드시 post로
                                    .build();
                            //동기 방식
                            response = client.newCall(request).execute();

                            flag = response.isSuccessful();
                            int responseCode = response.code();
                            Log.e("response==>", String.valueOf(responseCode));
                            //응답 코드 200등등

                            if (flag) {
                                e("response결과", response.message()); //읃답에 대한 메세지(OK)
                                //e("response응답바디", response.body().string()); //json으로 변신 You can only call string() once.

                                StringBuilder buf = new StringBuilder(response.body().string());

                                JSONObject jsonObject = new JSONObject(buf.toString());
                                final int like = jsonObject.getInt("like");
                                Log.e("like 확인", String.valueOf(like));

                                view.post(new Runnable(){
                                    @Override
                                    public void run() {
                                        if(like==1){
                                            interest_mark.setChecked(true);
                                        } else {
                                            interest_mark.setChecked(false);
                                        }
                                    }
                                });
                            }

                        } catch (UnknownHostException une) {
                            e("즐겨찾기에러1", une.toString());
                        } catch (UnsupportedEncodingException uee) {
                            e("즐겨찾기에러2", uee.toString());
                        } catch (Exception e) {
                            e("즐겨찾기에러3", e.toString());
                        } finally {
                            if (response != null) {
                                response.close();
                            }
                        }
                    }
                }).start();


                Log.e("즐겨찾기 버튼 눌림", "true");
            }
        });


        SERVER_URL_REP_NEWS = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER,"rep",repID,"news");
        Log.e("지역NEWS", SERVER_URL_REP_NEWS);
        SERVER_URL_REP_INIT = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER,"rep",repID,"proposer");
        Log.e("지역대표발의법률안",SERVER_URL_REP_INIT);
        SERVER_URL_REP_DETAIL = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER,"rep",repID,"detail");

    }

    private void setupNationalMemeberDetailViewPager(ViewPager viewPager) {
        NationalMemberPagerAdapter nationalMemberPagerAdapter = new NationalMemberPagerAdapter(getSupportFragmentManager());
        nationalMemberPagerAdapter.appendFragment(NationalMemberDetailNewsFragment.newInstance(1));
        nationalMemberPagerAdapter.appendFragment(NationalMemberDetailInitLawFragment.newInstance(1));
        nationalMemberPagerAdapter.appendFragment(NationalMemberDetailInfoFragment.newInstance(1));
        viewPager.setAdapter(nationalMemberPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = currentTime;
            Toast.makeText(getApplicationContext(), "'뒤로' 버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class NationalMemberPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragment = new ArrayList<>();

        public NationalMemberPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment) {
            this.fragment.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragment.get(position);
        }

        @Override
        public int getCount() {
            return fragment.size();
        }

    }

    public class AsyncMyRepJSONList extends AsyncTask<String, Integer, RepEntityObject> {

        @Override
        protected RepEntityObject doInBackground(String... params) {
            Response response = null;
            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .header("user_id", PropertyManager.getInstance().getUserId())
                        .url(SERVER_URL_REP_DETAIL)
                        .build();
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
                e("의원상세1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("의원상세2", uee.toString());
            } catch (Exception e) {
                e("의원상세3", e.toString());
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
        protected void onPostExecute(RepEntityObject result) {

            if (result != null) {
                party_str = result.party;
                Log.e("상세정당이름", party_str);

                if (result.interFlag) {
                    interest_mark.setChecked(true);
                } else if (!result.interFlag) {
                    interest_mark.setChecked(false);
                }

                facebook_str = result.facebook_link;
                twitter_str = result.twitter_link;
                name_str = result.name;
                district_str = result.local_constituencies;
                committee = result.committee;
                age_str = result.birth_date;
                elect_str = result.election_number;
                image_str = result.picture;


                if (party_str.equals("국민의당")) {
                    party.setImageResource(R.drawable.kookminuidanglogo108);
                } else if (party_str.equals("무소속")) {
                    party.setImageResource(R.drawable.musosok108);
                } else if (party_str.equals("더불어민주당")) {
                    party.setImageResource(R.drawable.theminjulogo108);
                } else if (party_str.equals("새누리당")) {
                    party.setImageResource(R.drawable.senurilogo108);
                } else if (party_str.equals("정의당")) {
                    party.setImageResource(R.drawable.junguidanglogo108);
                }

                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook_str));
                        startActivity(intent);
                    }
                });

                twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter_str));
                        startActivity(intent);
                    }
                });

                name.setText(name_str + ", " + result.vote_rate + "%득표");
                district.setText(district_str);
                profile.setText(committee.get(0));
                elect.setText(elect_str);


                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy년MM월생");
                Date date = new Date();
                try {
                    date = inputFormat.parse(age_str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputText = outputFormat.format(date);

                age.setText(outputText);

                Glide.with(MyApplication.getMyContext()).load(image_str).into(image);
            }
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

}