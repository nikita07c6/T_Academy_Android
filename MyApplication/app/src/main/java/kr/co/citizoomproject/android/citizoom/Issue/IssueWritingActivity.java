package kr.co.citizoomproject.android.citizoom.Issue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-27.
 */
public class IssueWritingActivity extends Activity {

    static int insertCount;
    LinearLayout second, third, fourth;
    ImageView plusIV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_writing);

        ImageView close = (ImageView) findViewById(R.id.issue_writing_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final EditText question = (EditText) findViewById(R.id.issue_question);
        final TextView textCount = (TextView) findViewById(R.id.question_count);

        question.addTextChangedListener(new TextWatcher() {
            String strCur;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strCur = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                textCount.setText(s.length() + "/100");
                if (s.length() >= 100) {
                    textCount.setTextColor(Color.RED);
                } else {
                    int colorHint = getResources().getColor(R.color.texthintColor);
                    textCount.setTextColor(colorHint);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final EditText answer = (EditText) findViewById(R.id.answer_choice1);
        final TextView ansCount = (TextView) findViewById(R.id.answer_count1);

        answer.addTextChangedListener(new TextWatcher() {
            String strCur;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strCur = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    ansCount.setVisibility(View.INVISIBLE);
                } else {
                    ansCount.setVisibility(View.VISIBLE);
                }
                int temp = 25 - s.length();
                ansCount.setText(String.valueOf(temp));
                if (s.length() >= 25) {
                    ansCount.setTextColor(Color.RED);
                } else {
                    int colorHint = getResources().getColor(R.color.texthintColor);
                    ansCount.setTextColor(colorHint);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final EditText answer1 = (EditText) findViewById(R.id.answer_choice2);
        final TextView ansCount1 = (TextView) findViewById(R.id.answer_count2);

        answer1.addTextChangedListener(new TextWatcher() {
            String strCur;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strCur = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    ansCount1.setVisibility(View.INVISIBLE);
                } else {
                    ansCount1.setVisibility(View.VISIBLE);
                }
                int temp = 25 - s.length();
                ansCount1.setText(String.valueOf(temp));
                if (s.length() >= 25) {
                    ansCount1.setTextColor(Color.RED);
                } else {
                    int colorHint = getResources().getColor(R.color.texthintColor);
                    ansCount1.setTextColor(colorHint);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final EditText answer2 = (EditText) findViewById(R.id.answer_choice3);
        final TextView ansCount2 = (TextView) findViewById(R.id.answer_count3);

        answer2.addTextChangedListener(new TextWatcher() {
            String strCur;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strCur = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    ansCount2.setVisibility(View.INVISIBLE);
                } else {
                    ansCount2.setVisibility(View.VISIBLE);
                }
                int temp = 25 - s.length();
                ansCount2.setText(String.valueOf(temp));
                if (s.length() >= 25) {
                    ansCount2.setTextColor(Color.RED);
                } else {
                    int colorHint = getResources().getColor(R.color.texthintColor);
                    ansCount2.setTextColor(colorHint);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        final EditText answer3 = (EditText) findViewById(R.id.answer_choice4);
        final TextView ansCount3 = (TextView) findViewById(R.id.answer_count4);

        answer3.addTextChangedListener(new TextWatcher() {
            String strCur;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strCur = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    ansCount3.setVisibility(View.INVISIBLE);
                } else {
                    ansCount3.setVisibility(View.VISIBLE);
                }
                int temp = 25 - s.length();
                ansCount3.setText(String.valueOf(temp));
                if (s.length() >= 25) {
                    ansCount3.setTextColor(Color.RED);
                } else {
                    int colorHint = getResources().getColor(R.color.texthintColor);
                    ansCount3.setTextColor(colorHint);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        final EditText answer4 = (EditText) findViewById(R.id.answer_choice5);
        final TextView ansCount4 = (TextView) findViewById(R.id.answer_count5);

        answer4.addTextChangedListener(new TextWatcher() {
            String strCur;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strCur = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    ansCount4.setVisibility(View.INVISIBLE);
                } else {
                    ansCount4.setVisibility(View.VISIBLE);
                }
                int temp = 25 - s.length();
                ansCount4.setText(String.valueOf(temp));
                if (s.length() >= 25) {
                    ansCount4.setTextColor(Color.RED);
                } else {
                    int colorHint = getResources().getColor(R.color.texthintColor);
                    ansCount4.setTextColor(colorHint);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final ImageView delete2 = (ImageView) findViewById(R.id.delete2);
        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer2.setText("");
                plusIV.setVisibility(View.VISIBLE);
                second.setVisibility(View.GONE);
                insertCount = 2;
                Log.e("count", String.valueOf(insertCount));
            }
        });
        final ImageView delete3 = (ImageView) findViewById(R.id.delete3);
        delete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete2.setVisibility(View.VISIBLE);
                answer3.setText("");
                plusIV.setVisibility(View.VISIBLE);
                third.setVisibility(View.GONE);
                insertCount = 3;
                Log.e("count", String.valueOf(insertCount));
            }
        });
        final ImageView delete4 = (ImageView) findViewById(R.id.delete4);
        delete4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete3.setVisibility(View.VISIBLE);
                answer4.setText("");
                plusIV.setVisibility(View.VISIBLE);
                fourth.setVisibility(View.GONE);
                insertCount = 4;
                Log.e("count", String.valueOf(insertCount));
            }
        });

        second = (LinearLayout) findViewById(R.id.second_answer);
        third = (LinearLayout) findViewById(R.id.third_answer);
        fourth = (LinearLayout) findViewById(R.id.fourth_answer);


        final ImageView delete5 = (ImageView)findViewById(R.id.delete5);
        delete5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertCount = 4;
                plusIV.setVisibility(View.VISIBLE);
                delete5.setVisibility(View.INVISIBLE);
                delete3.setVisibility(View.VISIBLE);
                fourth.setVisibility(View.GONE);
            }
        });
        insertCount = 2;
        plusIV = (ImageView) findViewById(R.id.plus);
        plusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (insertCount == 2) {
                    plusIV.setVisibility(View.VISIBLE);
                    second.setVisibility(View.VISIBLE);
                    delete2.setVisibility(View.VISIBLE);
                    insertCount += 1;
                    Log.e("count", String.valueOf(insertCount));
                } else if (insertCount == 3) {
                    plusIV.setVisibility(View.VISIBLE);
                    third.setVisibility(View.VISIBLE);
                    delete2.setVisibility(View.INVISIBLE);
                    delete3.setVisibility(View.VISIBLE);
                    insertCount += 1;
                    Log.e("count", String.valueOf(insertCount));
                } else if (insertCount == 4) {
                    fourth.setVisibility(View.VISIBLE);
                    delete3.setVisibility(View.INVISIBLE);
                    insertCount += 1;
                    Log.e("count", String.valueOf(insertCount));
                }
                if (insertCount == 5) {
                    Log.e("count5555", String.valueOf(insertCount));
                    insertCount -= 1;
                    plusIV.setVisibility(View.GONE);
                    delete4.setVisibility(View.INVISIBLE);
                    delete5.setVisibility(View.VISIBLE);
                }

            }
        });


        ImageView register = (ImageView) findViewById(R.id.issue_writing_check);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getQuestoin = String.valueOf(question.getText());
                String insert1 = String.valueOf(answer.getText());
                String insert5 = String.valueOf(answer4.getText());
                String insert2 = String.valueOf(answer1.getText());
                String insert3 = String.valueOf(answer2.getText());
                String insert4 = String.valueOf(answer3.getText());

                if (TextUtils.isEmpty(getQuestoin)&&(TextUtils.isEmpty(insert1) || TextUtils.isEmpty(insert5) || TextUtils.isEmpty(insert2) || TextUtils.isEmpty(insert3) || TextUtils.isEmpty(insert4))){
                    Toast.makeText(MyApplication.getMyContext(), "질문을 입력해주세요.\n보기를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(getQuestoin)) {
                    Toast.makeText(MyApplication.getMyContext(), "질문을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(insert1) || TextUtils.isEmpty(insert5)) {
                    Toast.makeText(MyApplication.getMyContext(), "보기를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                if (!TextUtils.isEmpty(getQuestoin) && !TextUtils.isEmpty(insert1) && !TextUtils.isEmpty(insert5) && TextUtils.isEmpty(insert2) && TextUtils.isEmpty(insert3) && TextUtils.isEmpty(insert4)) {
                    new IssueRegisterAsync(getQuestoin, insert1, insert5, 2).execute();
                    finish();
                }
                if (!TextUtils.isEmpty(getQuestoin) && !TextUtils.isEmpty(insert1) && !TextUtils.isEmpty(insert5) && !TextUtils.isEmpty(insert2) && TextUtils.isEmpty(insert3) && TextUtils.isEmpty(insert4)) {
                    new IssueRegisterAsync(getQuestoin, insert1, insert2, insert5, 3).execute();
                    finish();
                }
                if (!TextUtils.isEmpty(getQuestoin) && !TextUtils.isEmpty(insert1) && !TextUtils.isEmpty(insert5) && !TextUtils.isEmpty(insert2) && !TextUtils.isEmpty(insert3) && TextUtils.isEmpty(insert4)) {
                    new IssueRegisterAsync(getQuestoin, insert1, insert2, insert3, insert5, 4).execute();
                    finish();
                }
                if (!TextUtils.isEmpty(getQuestoin) && !TextUtils.isEmpty(insert1) && !TextUtils.isEmpty(insert5) && !TextUtils.isEmpty(insert2) && !TextUtils.isEmpty(insert3) && !TextUtils.isEmpty(insert4)) {
                    new IssueRegisterAsync(getQuestoin, insert1, insert2, insert3, insert4, insert5, 5).execute();
                    finish();
                }


            }
        });


    }

    public class IssueRegisterAsync extends AsyncTask<Void, Void, String> {
        boolean flag;
        String question, a1, a2, a3, a4, a5;
        int count;

        public IssueRegisterAsync(String question, String a1, String a5, int count) {
            this.question = question;
            this.a1 = a1;
            this.a5 = a5;
            this.count = count;
        }

        public IssueRegisterAsync(String question, String a1, String a2, String a5, int count) {
            this.question = question;
            this.a5 = a5;
            this.a1 = a1;
            this.a2 = a2;
            this.count = count;
        }

        public IssueRegisterAsync(String question, String a1, String a2, String a3, String a5, int count) {
            this.question = question;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a5 = a5;
            this.count = count;
        }

        public IssueRegisterAsync(String question, String a1, String a2, String a3, String a4, String a5, int count) {
            this.question = question;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.count = count;
        }

        @Override
        protected String doInBackground(Void... params) {
            Response response = null;
            FormBody fd = null;
            try {

                if (count == 2) {
                    Log.e("답변2개", "true");
                    fd = new FormBody.Builder()
                            .add("user_id", PropertyManager.getInstance().getUserId())
                            .add("question", question)
                            .add("choice_arr", a1)
                            .add("choice_arr", a5)
                            .build();
                }
                if (count == 3) {
                    Log.e("답변3개", "true");
                    fd = new FormBody.Builder()
                            .add("user_id", PropertyManager.getInstance().getUserId())
                            .add("question", question)
                            .add("choice_arr", a1)
                            .add("choice_arr", a2)
                            .add("choice_arr", a5)
                            .build();
                }
                if (count == 4) {
                    Log.e("답변4개", "true");
                    fd = new FormBody.Builder()
                            .add("user_id", PropertyManager.getInstance().getUserId())
                            .add("question", question)
                            .add("choice_arr", a1)
                            .add("choice_arr", a2)
                            .add("choice_arr", a3)
                            .add("choice_arr", a5)
                            .build();
                }
                if (count == 5) {
                    Log.e("답변5개", "true");
                    fd = new FormBody.Builder()
                            .add("user_id", PropertyManager.getInstance().getUserId())
                            .add("question", question)
                            .add("choice_arr", a1)
                            .add("choice_arr", a2)
                            .add("choice_arr", a3)
                            .add("choice_arr", a4)
                            .add("choice_arr", a5)
                            .build();
                }

                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_ISSUE_INSERT)
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
                e("11", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("22", uee.toString());
            } catch (Exception e) {
                e("33", e.toString());
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


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

}
