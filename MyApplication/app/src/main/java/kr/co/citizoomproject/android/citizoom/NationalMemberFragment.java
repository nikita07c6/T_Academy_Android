package kr.co.citizoomproject.android.citizoom;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.citizoomproject.android.citizoom.Interest.InterestingRepJsonAdapter;
import kr.co.citizoomproject.android.citizoom.Main.MainActivity;
import kr.co.citizoomproject.android.citizoom.Member.LocalMemberDetailActivity;
import kr.co.citizoomproject.android.citizoom.Member.LocalRepEntityObject;
import kr.co.citizoomproject.android.citizoom.Member.RepEntityObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-28.
 */
public class NationalMemberFragment extends Fragment {
    static MainActivity owner;
    static String party_str, facebook_str, twitter_str, name_str, profile_str, district_str, age_str, elect_str, image_str;
    public static int REPID;
    RecyclerView rv;
    ImageView party;
    ImageView facebook;
    ImageView twitter;
    ImageView nothing;
    TextView name, district, profile, age, elect;
    CircleImageView image;
    Bundle bundle;

    ArrayList<RepEntityObject> repEntityObjects = new ArrayList<>();
    private InterestingRepJsonAdapter interestingRepJsonAdapter;

    public NationalMemberFragment() {
    }

    public static NationalMemberFragment newInstance(int initValue) {
        NationalMemberFragment nationalMemberFragment = new NationalMemberFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        nationalMemberFragment.setArguments(bundle);
        return nationalMemberFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncMemberJSONList(interestingRepJsonAdapter, nothing).execute("");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.my_national_member_fragment, container, false);
        owner = (MainActivity) getActivity();

        nothing = (ImageView) view.findViewById(R.id.nothing_interest_member);

        party = (ImageView) view.findViewById(R.id.party_image);
        facebook = (ImageView) view.findViewById(R.id.facebook);
        twitter = (ImageView) view.findViewById(R.id.twitter);

        name = (TextView) view.findViewById(R.id.my_national_member_name);
        district = (TextView) view.findViewById(R.id.my_national_member_district);
        profile = (TextView) view.findViewById(R.id.my_national_member_ex);
        age = (TextView) view.findViewById(R.id.my_national_member_age);
        elect = (TextView) view.findViewById(R.id.my_national_member_elect_num);

        image = (CircleImageView) view.findViewById(R.id.my_national_member_image);

        CardView myMember = (CardView) view.findViewById(R.id.my_national_member);
        myMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LocalMemberDetailActivity.class);
                startActivity(intent);
            }
        });

        interestingRepJsonAdapter = new InterestingRepJsonAdapter(this.getActivity(), repEntityObjects);

        rv = (RecyclerView) view.findViewById(R.id.member_recyclerview);

        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));
        rv.setAdapter(interestingRepJsonAdapter);
        // 관심의원 받는 Async

        // 내 지역구 의원 받는 Async
        new AsyncMyRepJSONList(party, facebook, twitter, name, district, profile, age, elect, image).execute();

        return view;
    }


    public class AsyncMyRepJSONList extends AsyncTask<String, Integer, LocalRepEntityObject> {
        ImageView party;
        ImageView facebook;
        ImageView twitter;
        TextView name;
        TextView district;
        TextView profile;
        TextView age;
        TextView elect;
        CircleImageView image;

        public AsyncMyRepJSONList(ImageView party, ImageView facebook,
                                  ImageView twitter, TextView name, TextView district, TextView profile,
                                  TextView age, TextView elect, CircleImageView image) {
            this.party = party;
            this.facebook = facebook;
            this.twitter = twitter;
            this.name = name;
            this.district = district;
            this.profile = profile;
            this.age = age;
            this.elect = elect;
            this.image = image;
        }

        @Override
        protected LocalRepEntityObject doInBackground(String... params) {
            Response response = null;
            try {
                //OKHttp3사용ㄴ
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_MY_REP)
                        .build();
                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                //responseBody.string();
                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONMyRepRequestList(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("지역의원1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("지역의원2", uee.toString());
            } catch (Exception e) {
                e("지역의원3", e.toString());
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
        protected void onPostExecute(LocalRepEntityObject result) {

            if (result == null) {
                Toast.makeText(MyApplication.getMyContext(), "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }

            if (result != null) {

                REPID = result.rep_id;

                party_str = result.party;
                Log.e("정당이름", party_str);
                facebook_str = result.facebook_link;
                twitter_str = result.twitter_link;
                name_str = result.name;
                district_str = result.local_constituencies;
                profile_str = result.committee;
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
                profile.setText(profile_str);
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


}
