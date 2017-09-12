package kr.co.citizoomproject.android.citizoom.Law;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-28.
 */
public class LawCommentWritingActivity extends AppCompatActivity implements Serializable{
    String SERVER_URL_LAW_COMMENT_INSERT;
    LinearLayout commentLayout;
    String innerComment, COMMENTID;
    int isAgree, LAWID;
    boolean mAgree;

    public LawCommentWritingActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.law_comment_writing);

        final EditText comment = (EditText) findViewById(R.id.user_comment_editText);
        final TextView textCount = (TextView) findViewById(R.id.comment_count);
        final ImageView agree = (ImageView) findViewById(R.id.agree);
        final ImageView disagree = (ImageView) findViewById(R.id.disagree);

        final Intent intent = getIntent();
        LAWID = intent.getIntExtra("LAWID", LAWID);

        SERVER_URL_LAW_COMMENT_INSERT = String.format("http://%s:%d/%s/%d", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER, "comment", LAWID);
        Log.e("댓글 URL", SERVER_URL_LAW_COMMENT_INSERT);


        if (intent.hasExtra("modifiedComment")){
            LawCommentWritingActivity modifiedWriting = (LawCommentWritingActivity)intent.getSerializableExtra("modifiedComment");
            COMMENTID = modifiedWriting.COMMENTID;
            comment.setText(modifiedWriting.innerComment);
            if(modifiedWriting.mAgree) {
                agree.setImageResource(R.drawable.btn_agree_on);
                disagree.setVisibility(View.INVISIBLE);
                isAgree = 1;
            } else if(!modifiedWriting.mAgree) {
                disagree.setImageResource(R.drawable.btn_disagree_on);
                agree.setVisibility(View.INVISIBLE);
                isAgree = 2;
            }
        }

        commentLayout = (LinearLayout) findViewById(R.id.comment_layout);
        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                comment.requestFocus();

                //키보드 보이게 하는 부분
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        });


        comment.addTextChangedListener(new TextWatcher() {
            String strCur;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strCur = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                textCount.setText(s.length() + "/200");
                if(s.length() >= 200) {
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

        ImageView close = (ImageView) findViewById(R.id.law_comment_cancel_Btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NavUtils.navigateUpFromSameTask(this);
                InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                finish();
            }
        });


        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (disagree.isShown()) {
                    agree.setImageResource(R.drawable.btn_agree_on);
                    disagree.setVisibility(View.INVISIBLE);
                } else {
                    agree.setImageResource(R.drawable.btn_agree_off);
                    disagree.setVisibility(View.VISIBLE);
                }
                isAgree = 1;
            }
        });

        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agree.isShown()) {
                    disagree.setImageResource(R.drawable.btn_disagree_on);
                    agree.setVisibility(View.INVISIBLE);
                } else {
                    disagree.setImageResource(R.drawable.btn_disagree_off);
                    agree.setVisibility(View.VISIBLE);
                }
                isAgree = 2;
            }
        });

        ImageView register = (ImageView) findViewById(R.id.law_comment_save_Btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                innerComment = String.valueOf(comment.getText());
                if(intent.hasExtra("modifiedComment")){

                    if ((agree.isShown() && disagree.isShown()) && innerComment.isEmpty()) {
                        Toast.makeText(MyApplication.getMyContext(), "의견을 입력해주세요\n찬성, 반대를 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                    if ((agree.isShown() && disagree.isShown()) && !innerComment.isEmpty()) {
                        Toast.makeText(MyApplication.getMyContext(), "찬성, 반대를 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                    if (!(agree.isShown() && disagree.isShown()) && innerComment.isEmpty()) {
                        Toast.makeText(MyApplication.getMyContext(), "의견을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                    if (!(agree.isShown() && disagree.isShown()) && !innerComment.isEmpty()) {
                        innerComment = String.valueOf(comment.getText());
                        new CommentModifyAsync().execute();
                        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        finish();
                    }

                } else {

                    if ((agree.isShown() && disagree.isShown()) && innerComment.isEmpty()) {
                        Toast.makeText(MyApplication.getMyContext(), "의견을 입력해주세요\n찬성, 반대를 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                    if ((agree.isShown() && disagree.isShown()) && !innerComment.isEmpty()) {
                        Toast.makeText(MyApplication.getMyContext(), "찬성, 반대를 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                    if (!(agree.isShown() && disagree.isShown()) && innerComment.isEmpty()) {
                        Toast.makeText(MyApplication.getMyContext(), "의견을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                    if (!(agree.isShown() && disagree.isShown()) && !innerComment.isEmpty()) {
                        innerComment = String.valueOf(comment.getText());
                        new CommentRegisterAsync().execute();
                        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        finish();
                    }

                }
            }
        });

    }

    public class CommentRegisterAsync extends AsyncTask<Void, Void, String> {
        boolean flag;

        @Override
        protected String doInBackground(Void... params) {
            Response response = null;

            MultipartBody.Builder MB;
            MB = new MultipartBody.Builder();
            MB.setType(MultipartBody.FORM);

            try {
                MB.addFormDataPart("isAgree", String.valueOf(isAgree));
                MB.addFormDataPart("user_id", PropertyManager.getInstance().getUserId());
                MB.addFormDataPart("contents", innerComment);
                MB.addFormDataPart("nickname", PropertyManager.getInstance().getNickname());

                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                RequestBody fileUploadBody = MB.build();

                Request request = new Request.Builder()
                        .url(SERVER_URL_LAW_COMMENT_INSERT)
                        .post(fileUploadBody)
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

    public class CommentModifyAsync extends AsyncTask<Void, Void, String> {
        boolean flag;

        @Override
        protected String doInBackground(Void... params) {
            Response response = null;

            MultipartBody.Builder MB;
            MB = new MultipartBody.Builder();
            MB.setType(MultipartBody.FORM);

            try {

                MB.addFormDataPart("isAgree", String.valueOf(isAgree));
                MB.addFormDataPart("user_id", PropertyManager.getInstance().getUserId());
                MB.addFormDataPart("contents", innerComment);
                MB.addFormDataPart("comment_id", COMMENTID);

                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                RequestBody fileUploadBody = MB.build();

                Request request = new Request.Builder()
                        .url(SERVER_URL_LAW_COMMENT_INSERT)
                        .put(fileUploadBody)
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
                e("댓글수정11", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("댓글수정22", uee.toString());
            } catch (Exception e) {
                e("댓글수정33", e.toString());
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