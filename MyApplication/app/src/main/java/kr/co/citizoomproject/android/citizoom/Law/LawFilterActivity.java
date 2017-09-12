package kr.co.citizoomproject.android.citizoom.Law;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import kr.co.citizoomproject.android.citizoom.Main.MainActivity;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.R;
import kr.co.citizoomproject.android.citizoom.Setting.InterestCommitteeListAdapter;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-01.
 */
public class LawFilterActivity extends AppCompatActivity {
    ArrayList<String> committee = new ArrayList<>();
    ArrayList<String> month = new ArrayList<>();
    String year, proposer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.law_filter_activity);

        ImageView back = (ImageView) findViewById(R.id.law_filter_close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NavUtils.navigateUpFromSameTask(this);
                setResult(RESULT_CANCELED);
                finish();
            }
        });


        final ToggleButton goverment = (ToggleButton) findViewById(R.id.goverment);
        final ToggleButton member = (ToggleButton) findViewById(R.id.member);

        goverment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proposer = "정부";
                if (member.isChecked()) {
                    member.setChecked(false);
                }
            }
        });

        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proposer = "의원";
                if (goverment.isChecked()) {
                    goverment.setChecked(false);
                }
            }
        });


        final ToggleButton six = (ToggleButton) findViewById(R.id.six);
        final ToggleButton seven = (ToggleButton) findViewById(R.id.seven);
        final ToggleButton eight = (ToggleButton) findViewById(R.id.eight);
        final ToggleButton nine = (ToggleButton) findViewById(R.id.nine);

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = "2016";
                seven.setChecked(false);
                eight.setChecked(false);
                nine.setChecked(false);
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = "2017";
                six.setChecked(false);
                eight.setChecked(false);
                nine.setChecked(false);
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = "2018";
                seven.setChecked(false);
                six.setChecked(false);
                nine.setChecked(false);
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = "2019";
                seven.setChecked(false);
                eight.setChecked(false);
                six.setChecked(false);
            }
        });

        final ToggleButton january = (ToggleButton) findViewById(R.id.january);
        final ToggleButton february = (ToggleButton) findViewById(R.id.february);
        final ToggleButton march = (ToggleButton) findViewById(R.id.march);
        final ToggleButton april = (ToggleButton) findViewById(R.id.april);
        final ToggleButton may = (ToggleButton) findViewById(R.id.may);
        final ToggleButton june = (ToggleButton) findViewById(R.id.june);
        final ToggleButton july = (ToggleButton) findViewById(R.id.july);
        final ToggleButton august = (ToggleButton) findViewById(R.id.august);
        final ToggleButton september = (ToggleButton) findViewById(R.id.september);
        final ToggleButton october = (ToggleButton) findViewById(R.id.october);
        final ToggleButton november = (ToggleButton) findViewById(R.id.november);
        final ToggleButton december = (ToggleButton) findViewById(R.id.december);

        january.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (january.isChecked()) {
                    month.add("1");
                } else {
                    month.remove("1");
                }
            }
        });

        february.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (february.isChecked()) {
                    month.add("2");
                } else {
                    month.remove("2");
                }
            }
        });

        march.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (march.isChecked()) {
                    month.add("3");
                } else {
                    month.remove("3");
                }
            }
        });

        april.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (april.isChecked()) {
                    month.add("4");
                } else {
                    month.remove("4");
                }
            }
        });

        may.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (may.isChecked()) {
                    month.add("5");
                } else {
                    month.remove("6");
                }
            }
        });

        june.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (june.isChecked()) {
                    month.add("6");
                } else {
                    month.remove("6");
                }
            }
        });

        july.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (july.isChecked()) {
                    month.add("7");
                } else {
                    month.remove("7");
                }
            }
        });

        august.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (august.isChecked()) {
                    month.add("8");
                } else {
                    month.remove("8");
                }
            }
        });

        september.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (september.isChecked()) {
                    month.add("9");
                } else {
                    month.remove("9");
                }
            }
        });

        october.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (october.isChecked()) {
                    month.add("10");
                } else {
                    month.remove("10");
                }
            }
        });

        november.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (november.isChecked()) {
                    month.add("11");
                } else {
                    month.remove("11");
                }
            }
        });

        december.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (december.isChecked()) {
                    month.add("12");
                } else {
                    month.remove("12");
                }
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
        final ToggleButton womenfamily = (ToggleButton) findViewById(R.id.womemfamily);

        agricultureforestry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if (womenfamily.isChecked()) {
                    committee.add("여성가족");
                } else {
                    committee.remove("여성가족");
                }

            }
        });

        ImageView reset = (ImageView) findViewById(R.id.law_filter_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                committee.clear();
                month.clear();
                year = null;
                proposer = null;

                goverment.setChecked(false);
                member.setChecked(false);

                six.setChecked(false);
                seven.setChecked(false);
                eight.setChecked(false);
                nine.setChecked(false);

                january.setChecked(false);
                february.setChecked(false);
                march.setChecked(false);
                april.setChecked(false);
                may.setChecked(false);
                june.setChecked(false);
                july.setChecked(false);
                august.setChecked(false);
                september.setChecked(false);
                october.setChecked(false);
                november.setChecked(false);
                december.setChecked(false);

                agricultureforestry.setChecked(false);
                assemblyoperation.setChecked(false);
                defense.setChecked(false);
                educationculture.setChecked(false);
                environmentlabor.setChecked(false);
                foreign.setChecked(false);
                futurecreation.setChecked(false);
                healthwelfare.setChecked(false);
                industrycommercial.setChecked(false);
                information.setChecked(false);
                law.setChecked(false);
                politicalaffairs.setChecked(false);
                safety.setChecked(false);
                strategyfinance.setChecked(false);
                traffic.setChecked(false);
                womenfamily.setChecked(false);
            }
        });

        ToggleButton check = (ToggleButton) findViewById(R.id.filter_check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!goverment.isChecked() && !member.isChecked()) {
                    Toast.makeText(LawFilterActivity.this, "발의자를 선택해주세요",Toast.LENGTH_SHORT).show();
                }
                if(!six.isChecked() && !seven.isChecked() && !eight.isChecked() && !nine.isChecked()) {
                    Toast.makeText(LawFilterActivity.this, "발의년도를 선택해주세요",Toast.LENGTH_SHORT).show();
                }
                if(!january.isChecked() && !february.isChecked() && !march.isChecked() && !april.isChecked()
                        && !may.isChecked() && !june.isChecked() && !july.isChecked() && !august.isChecked()
                        && !september.isChecked() && !october.isChecked() && !november.isChecked() && !december.isChecked()) {
                    Toast.makeText(LawFilterActivity.this, "발의 날짜를 선택해주세요",Toast.LENGTH_SHORT).show();
                }
                if(!agricultureforestry.isChecked() && !assemblyoperation.isChecked() && !defense.isChecked()
                        && !educationculture.isChecked() && !environmentlabor.isChecked() && !foreign.isChecked()
                        && !futurecreation.isChecked() && !healthwelfare.isChecked() && !industrycommercial.isChecked()
                        && !information.isChecked() && !law.isChecked() && !politicalaffairs.isChecked()
                        && !safety.isChecked() && !strategyfinance.isChecked() && !traffic.isChecked()
                        && !womenfamily.isChecked()){
                    Toast.makeText(LawFilterActivity.this, "상임위원회를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                if((goverment.isChecked() || member.isChecked())&&(six.isChecked() || seven.isChecked() || eight.isChecked() || nine.isChecked())
                        &&(january.isChecked() || february.isChecked() || march.isChecked() || april.isChecked()
                        || may.isChecked() || june.isChecked() || july.isChecked() || august.isChecked()
                        || september.isChecked() || october.isChecked() || november.isChecked() || december.isChecked())
                        &&(agricultureforestry.isChecked() || assemblyoperation.isChecked() || defense.isChecked()
                        || educationculture.isChecked() || environmentlabor.isChecked() || foreign.isChecked()
                        || futurecreation.isChecked() || healthwelfare.isChecked() || industrycommercial.isChecked()
                        || information.isChecked() || law.isChecked() || politicalaffairs.isChecked()
                        || safety.isChecked() || strategyfinance.isChecked() || traffic.isChecked()
                        || womenfamily.isChecked())){

                    Intent intent = new Intent();

                    intent.putExtra("proposer",proposer);
                    intent.putExtra("year", year);
                    intent.putStringArrayListExtra("month", month);
                    intent.putStringArrayListExtra("topic", committee);

                    setResult(RESULT_OK,intent);
                    LawFragment.filterFlag = 1;
                    LawFragment.pageValue = 1;
                    finish();

                }

            }
        });

    } // onCreate


}
