package kr.co.citizoomproject.android.citizoom.Join;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.Main.MainActivity;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-22.
 */
public class JoinSettingFilterActivity extends AppCompatActivity {
    ArrayList<String> committee = new ArrayList<>();
    String genderStr, ageStr, nickStr, addrStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_filter);

        final ToggleButton male = (ToggleButton) findViewById(R.id.male);
        final ToggleButton female = (ToggleButton) findViewById(R.id.female);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderStr = "male";
                if (female.isChecked()) {
                    female.setChecked(false);
                }
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderStr = "female";
                if (male.isChecked()) {
                    male.setChecked(false);
                }
            }
        });

        final ToggleButton twenty = (ToggleButton) findViewById(R.id.twenty);
        final ToggleButton thirty = (ToggleButton) findViewById(R.id.thirty);
        final ToggleButton fourty = (ToggleButton) findViewById(R.id.fourty);
        final ToggleButton fifty = (ToggleButton) findViewById(R.id.fifty);
        final ToggleButton sixty = (ToggleButton) findViewById(R.id.sixty);

        twenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ageStr = "20";
                thirty.setChecked(false);
                fourty.setChecked(false);
                fifty.setChecked(false);
                sixty.setChecked(false);
            }
        });

        thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ageStr = "30";
                twenty.setChecked(false);
                fourty.setChecked(false);
                fifty.setChecked(false);
                sixty.setChecked(false);
            }
        });

        fourty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ageStr = "40";
                thirty.setChecked(false);
                twenty.setChecked(false);
                fifty.setChecked(false);
                sixty.setChecked(false);
            }
        });

        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ageStr = "50";
                thirty.setChecked(false);
                fourty.setChecked(false);
                twenty.setChecked(false);
                sixty.setChecked(false);
            }
        });

        sixty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ageStr = "60";
                thirty.setChecked(false);
                fourty.setChecked(false);
                fifty.setChecked(false);
                twenty.setChecked(false);
            }
        });

        final ToggleButton agricultureforestry = (ToggleButton) findViewById(R.id.agricultureforestry);
        final ToggleButton assemblyoperation = (ToggleButton) findViewById(R.id.assemblyoperation);
        final ToggleButton defense = (ToggleButton) findViewById(R.id.defense);
        final ToggleButton educationculture = (ToggleButton) findViewById(R.id.educationculture);
        final ToggleButton environmentlabor = (ToggleButton) findViewById(R.id.environmentlabor);
        final ToggleButton foreign = (ToggleButton) findViewById(R.id.foreign);
        final ToggleButton futurecreation = (ToggleButton) findViewById(R.id.futurecreation);
        final ToggleButton healthwelfare = (ToggleButton) findViewById(R.id.healthwelfare);
        final ToggleButton industrycommercial = (ToggleButton) findViewById(R.id.industrycommercial);
        final ToggleButton information = (ToggleButton) findViewById(R.id.information);
        final ToggleButton law = (ToggleButton) findViewById(R.id.law);
        final ToggleButton politicalaffairs = (ToggleButton) findViewById(R.id.politicalaffairs);
        final ToggleButton safety = (ToggleButton) findViewById(R.id.safety);
        final ToggleButton strategyfinance = (ToggleButton) findViewById(R.id.strategyfinance);
        final ToggleButton traffic = (ToggleButton) findViewById(R.id.traffic);
        final ToggleButton womenfamily = (ToggleButton) findViewById(R.id.womenfamily);

        final ToggleButton joinComplete = (ToggleButton) findViewById(R.id.filter_check);

        agricultureforestry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (agricultureforestry.isChecked()) {
                    committee.add("농림식품해양수산");
                } else {
                    committee.remove("농림식품해양수산");
                }
            }
        });

        assemblyoperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (assemblyoperation.isChecked()) {
                    committee.add("국회운영");
                } else {
                    committee.remove("국회운영");
                }

            }
        });

        defense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (defense.isChecked()) {
                    committee.add("국방");
                } else {
                    committee.remove("국방");
                }

            }
        });

        educationculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (educationculture.isChecked()) {
                    committee.add("교육문화체육관광");
                } else {
                    committee.remove("교육문화체육관광");
                }

            }
        });

        environmentlabor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (environmentlabor.isChecked()) {
                    committee.add("환경노동");
                } else {
                    committee.remove("환경노동");
                }

            }
        });

        foreign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (foreign.isChecked()) {
                    committee.add("외교통일");
                } else {
                    committee.remove("외교통일");
                }

            }
        });

        futurecreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (futurecreation.isChecked()) {
                    committee.add("미래창조과학방송통신");
                } else {
                    committee.remove("미래창조과학방송통신");
                }

            }
        });

        healthwelfare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (healthwelfare.isChecked()) {
                    committee.add("보건복지");
                } else {
                    committee.remove("보건복지");
                }

            }
        });

        industrycommercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (industrycommercial.isChecked()) {
                    committee.add("산업통상자원");
                } else {
                    committee.remove("산업통상자원");
                }

            }
        });

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (information.isChecked()) {
                    committee.add("정보");
                } else {
                    committee.remove("정보");
                }

            }
        });

        law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (law.isChecked()) {
                    committee.add("법제사법");
                } else {
                    committee.remove("법제사법");
                }

            }
        });

        politicalaffairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (politicalaffairs.isChecked()) {
                    committee.add("정무");
                } else {
                    committee.remove("정무");
                }

            }
        });

        safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (safety.isChecked()) {
                    committee.add("안전행정");
                } else {
                    committee.remove("안전행정");
                }

            }
        });

        strategyfinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (strategyfinance.isChecked()) {
                    committee.add("기획재정");
                } else {
                    committee.remove("기획재정");
                }

            }
        });

        traffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (traffic.isChecked()) {
                    committee.add("국토교통");
                } else {
                    committee.remove("국토교통");
                }

            }
        });

        womenfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (womenfamily.isChecked()) {
                    committee.add("여성가족");
                } else {
                    committee.remove("여성가족");
                }

            }
        });

        Intent getintent = getIntent();
        nickStr = getintent.getStringExtra("nickname");
        addrStr = getintent.getStringExtra("location");

        joinComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinComplete.setChecked(true);
                if (committee.size() < 1) {
                    Toast.makeText(JoinSettingFilterActivity.this, "관심 상임위원회는 하나 이상 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(JoinSettingFilterActivity.this, MainActivity.class);
                    new LoginAsync().execute();
                    startActivity(intent);
                }
            }
        });
    }

    public class LoginAsync extends AsyncTask<Void, Void, String> {
        boolean flag;

        @Override
        protected String doInBackground(Void... params) {
            Response response = null;
            FormBody fd = null;

            try {
                // Initialize Builder (not RequestBody)
                FormBody.Builder builder = new FormBody.Builder();

                // Add Params to Builder
                int length = committee.size();
                for (int i = 0; i < length; i++) {
                    builder.add("topic", committee.get(i));
                }

                builder.add("gender", genderStr);
                builder.add("age", ageStr);
                builder.add("nickname", nickStr);
                builder.add("location", addrStr);

                // Create RequestBody
                fd = builder.build();

                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_LOGIN)
                        .post(fd)
                        .build();

                //동기 방식
                response = client.newCall(request).execute();

                flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                Log.e("response==>", String.valueOf(responseCode));


                if (flag) {
                    e("response결과", response.message()); //읃답에 대한 메세지(OK)
                    //e("response응답바디", response.body().string()); //json으로 변신
                }
                return response.body().string();
            } catch (UnknownHostException une) {
                e("회원가입1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("회원가입2", uee.toString());
            } catch (Exception e) {
                e("회원가입3", e.toString());
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
