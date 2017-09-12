package kr.co.citizoomproject.android.citizoom.Law;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-03.
 */
public class LawCommentJSONAdapter extends RecyclerView.Adapter<LawCommentJSONAdapter.ViewHolder> {
    String SERVER_URL_COMMENT_LIKE, SERVER_URL_COMMENT_DELETE;
    private ArrayList<LawCommentEntityObject> lawCommentEntityObjects;
    Date date;
    LawDetailActivity owner;

    public LawCommentJSONAdapter(Context context, ArrayList<LawCommentEntityObject> resources) {
        this.lawCommentEntityObjects = resources;
        owner = (LawDetailActivity) context;
    }

    public static String formatTimeString(Date tempDate) {

        long curTime = System.currentTimeMillis();
        long regTime = tempDate.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = diffTime + "초 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) > 6) {
            // 일주일 넘게 지났으면 날짜 표시
        }
        return msg;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.law_user_comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final LawCommentEntityObject lawCommentEntityObject = lawCommentEntityObjects.get(position);

        SERVER_URL_COMMENT_DELETE = NetworkDefineConstant.SERVER_URL_SELECT_COMMENT_LAW + "/" + LawDetailActivity.lawID;
        Log.e("댓글아이디", lawCommentEntityObject.commentID);
        Log.e("삭제URL", SERVER_URL_COMMENT_DELETE);


        final CharSequence[] items = {"수정", "삭제"};

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(owner, R.style.MyDialogTheme);

                // 여기서 부터는 알림창의 속성 설정
                builder.setTitle(lawCommentEntityObject.nickname)        // 제목 설정
                        .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
                            public void onClick(DialogInterface dialog, int index){
                                if (index == 0){
                                    LawCommentWritingActivity modifyComment = new LawCommentWritingActivity();

                                    modifyComment.mAgree = lawCommentEntityObject.isAgree;
                                    modifyComment.COMMENTID = lawCommentEntityObject.commentID;
                                    modifyComment.innerComment = lawCommentEntityObject.contents;

                                    Intent intent = new Intent(MyApplication.getMyContext(), LawCommentWritingActivity.class);
                                    intent.putExtra("LAWID", lawCommentEntityObject.lawID);
                                    intent.putExtra("modifiedComment", modifyComment);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MyApplication.getMyContext().startActivity(intent);
                                }
                                if (index == 1){
                                    new AsyncLawCommentDeleteJSONList(lawCommentEntityObject.commentID).execute();
                                }
                                Toast.makeText(MyApplication.getMyContext(), items[index], Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기

            }
        });

        Log.e("onBindviewHolder", lawCommentEntityObject.nickname);
        holder.nickname.setText(lawCommentEntityObject.nickname.toString());
        holder.contents.setText(lawCommentEntityObject.contents.toString());

        String inputText = lawCommentEntityObject.date;
        SimpleDateFormat format  = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        try {
            date = format.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String outputText;
        if (formatTimeString(date).isEmpty()) {
            outputText = format.format(date);
        } else {
            outputText = formatTimeString(date);
        }

        holder.date.setText(outputText);

        if (lawCommentEntityObject.likeFlag) {
            holder.likeButton.setChecked(true);
        } else if (!lawCommentEntityObject.likeFlag) {
            holder.likeButton.setChecked(false);
        }
        if (lawCommentEntityObject.dislikeFlag) {
            holder.dislikeButton.setChecked(true);
        } else if (!lawCommentEntityObject.dislikeFlag) {
            holder.dislikeButton.setChecked(false);
        }

        if (lawCommentEntityObject.isAgree) {
            Log.e("찬성이냐 반대냐", String.valueOf(lawCommentEntityObject.isAgree));
            holder.agree.setImageResource(R.drawable.likecomment);
        }
        if (!lawCommentEntityObject.isAgree) {
            Log.e("찬성이냐 반대냐", String.valueOf(lawCommentEntityObject.isAgree));
            holder.agree.setImageResource(R.drawable.hatecomment);
        }

        holder.likeCount.setText(String.valueOf(lawCommentEntityObject.likeCount));
        holder.dislikeCount.setText(String.valueOf(lawCommentEntityObject.dislikeCount));

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String COMMENTID = lawCommentEntityObject.commentID.toString();
                SERVER_URL_COMMENT_LIKE = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER, "comment", COMMENTID, "rating");
                Log.e("댓글 좋아요 URL", SERVER_URL_COMMENT_LIKE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response response = null;
                        boolean flag = false;

                        MultipartBody.Builder MB;
                        MB = new MultipartBody.Builder();
                        MB.setType(MultipartBody.FORM);

                        try {
                            MB.addFormDataPart("user_id", PropertyManager.getInstance().getUserId());
                            MB.addFormDataPart("like", String.valueOf(1));

                            OkHttpClient client = MySingleton.sharedInstance().httpClient;

                            //요청 Body 세팅==> 그전 Query Parameter세팅과 같은 개념
                            RequestBody fileUploadBody = MB.build();

                            //요청 세팅
                            Request request = new Request.Builder()
                                    .url(SERVER_URL_COMMENT_LIKE)
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
                                final int agreeCount = jsonObject.getInt("agree_count");
                                final int disagreeCount = jsonObject.getInt("disagree_count");
                                Log.e("좋아요 수 확인", String.valueOf(agreeCount));
                                Log.e("싫어요 수 확인", String.valueOf(disagreeCount));

                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.likeCount.setText(String.valueOf(agreeCount));
                                        holder.dislikeCount.setText(String.valueOf(disagreeCount));
                                        if (holder.dislikeButton.isChecked()) {
                                            holder.dislikeButton.setChecked(false);
                                        }
                                    }
                                });
                            }

                        } catch (UnknownHostException une) {
                            e("댓글1에러", une.toString());
                        } catch (UnsupportedEncodingException uee) {
                            e("댓글2에러", uee.toString());
                        } catch (Exception e) {
                            e("댓글3에러", e.toString());
                        } finally {
                            if (response != null) {
                                response.close();
                            }
                        }
                    }
                }).start();


                Log.e("좋아요 버튼 눌림", "true");
            }
        });

        holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String COMMENTID = lawCommentEntityObject.commentID.toString();
                SERVER_URL_COMMENT_LIKE = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER, "comment", COMMENTID, "rating");
                Log.e("댓글 싫어요 URL", SERVER_URL_COMMENT_LIKE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response response = null;
                        boolean flag = false;

                        MultipartBody.Builder MB;
                        MB = new MultipartBody.Builder();
                        MB.setType(MultipartBody.FORM);

                        try {
                            MB.addFormDataPart("user_id", PropertyManager.getInstance().getUserId());
                            MB.addFormDataPart("like", String.valueOf(2));

                            OkHttpClient client = MySingleton.sharedInstance().httpClient;

                            //요청 Body 세팅==> 그전 Query Parameter세팅과 같은 개념
                            RequestBody fileUploadBody = MB.build();

                            //요청 세팅
                            Request request = new Request.Builder()
                                    .url(SERVER_URL_COMMENT_LIKE)
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
                                final int agreeCount = jsonObject.getInt("agree_count");
                                final int disagreeCount = jsonObject.getInt("disagree_count");
                                Log.e("좋아요 수 확인", String.valueOf(agreeCount));
                                Log.e("싫어요 수 확인", String.valueOf(disagreeCount));

                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.likeCount.setText(String.valueOf(agreeCount));
                                        holder.dislikeCount.setText(String.valueOf(disagreeCount));
                                        if (holder.likeButton.isChecked()) {
                                            holder.likeButton.setChecked(false);
                                        }
                                    }
                                });
                            }

                        } catch (UnknownHostException une) {
                            e("싫어요에러1", une.toString());
                        } catch (UnsupportedEncodingException uee) {
                            e("싫어요에러2", uee.toString());
                        } catch (Exception e) {
                            e("싫어요에러3", e.toString());
                        } finally {
                            if (response != null) {
                                response.close();
                            }
                        }
                    }
                }).start();

            }
        });

        if (lawCommentEntityObject.writer_id.equalsIgnoreCase(PropertyManager.getInstance().getUserId())) {
            holder.modify.setVisibility(View.VISIBLE);
            holder.likeButton.setClickable(false);
            holder.dislikeButton.setClickable(false);
        }


    }


    public class AsyncLawCommentDeleteJSONList extends AsyncTask<String, Integer, LawCommentEntityObject> {
        String commentID;

        public AsyncLawCommentDeleteJSONList(String commentID) {
            this.commentID = commentID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected LawCommentEntityObject doInBackground(String... params) {
            Response response = null;
            boolean flag = false;

            MultipartBody.Builder MB;
            MB = new MultipartBody.Builder();
            MB.setType(MultipartBody.FORM);

            try {

                MB.addFormDataPart("user_id", PropertyManager.getInstance().getUserId());
                MB.addFormDataPart("comment_id", commentID);

                OkHttpClient client = MySingleton.sharedInstance().httpClient;


                RequestBody fileUploadBody = MB.build();

                Request request = new Request.Builder()
                        .url(SERVER_URL_COMMENT_DELETE+commentID)
                        .delete(fileUploadBody)
                        .build();

                //동기 방식
                response = client.newCall(request).execute();

                flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                Log.e("response==>", String.valueOf(responseCode));

                if (flag){
                    e("response결과", response.message()); //읃답에 대한 메세지(OK)
                    //e("response응답바디", response.body().string()); //json으로 변신
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
        protected void onPostExecute(LawCommentEntityObject result) {
            new LawDetailCommentFragment.AsyncLawNewCommentJSONList(1,"default").execute();
        }
    }

    @Override
    public int getItemCount() {
        return lawCommentEntityObjects.size();
    }

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 7;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nickname;
        public final TextView contents;
        public final TextView date;
        public final TextView likeCount;
        public final TextView dislikeCount;
        public final ToggleButton likeButton;
        public final ToggleButton dislikeButton;
        public final ImageView agree;
        public final ImageView modify;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            nickname = (TextView) view.findViewById(R.id.comment_user_nick);
            contents = (TextView) view.findViewById(R.id.user_comment);
            date = (TextView) view.findViewById(R.id.comment_register_time);
            likeCount = (TextView) view.findViewById(R.id.comment_good_count);
            dislikeCount = (TextView) view.findViewById(R.id.comment_bad_count);
            likeButton = (ToggleButton) view.findViewById(R.id.comment_good);
            dislikeButton = (ToggleButton) view.findViewById(R.id.comment_bad);
            agree = (ImageView) view.findViewById(R.id.agreeordisagree);
            modify = (ImageView) view.findViewById(R.id.modifier);

        }
    }
}