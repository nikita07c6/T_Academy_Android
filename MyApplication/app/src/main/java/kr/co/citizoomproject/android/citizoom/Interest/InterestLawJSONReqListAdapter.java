package kr.co.citizoomproject.android.citizoom.Interest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.AsyncLawJSONList;
import kr.co.citizoomproject.android.citizoom.Law.LawDetailActivity;
import kr.co.citizoomproject.android.citizoom.Law.LawEntityObject;
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
 * Created by ccei on 2016-08-11.
 */
public class InterestLawJSONReqListAdapter extends RecyclerView.Adapter<InterestLawJSONReqListAdapter.ViewHolder> {
    String SERVER_URL_LAWS_PROS_CONS, SERVER_URL_LAW_LIKE;
    public ArrayList<LawEntityObject> lawEntityObjects;
    InterestLawMemberActivity owner;
    public InterestLawFragment fragment;

    public InterestLawJSONReqListAdapter(Context context, ArrayList<LawEntityObject> resources) {
        owner = (InterestLawMemberActivity)context;
        this.lawEntityObjects = resources;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.law_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final LawEntityObject lawEntityObject = lawEntityObjects.get(position);

        Log.e("onBindviewHolder", lawEntityObject.title);

        String whatcommittee = lawEntityObject.committee.toString();
        int thiscommittee = getCommittee(whatcommittee);
        if(TextUtils.isEmpty(whatcommittee)){
            holder.committee.setImageResource(R.drawable.undecided);
            holder.date.setVisibility(View.GONE);
        } else {
            holder.committee.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            switch (thiscommittee) {
                case 1:
                    holder.committee.setImageResource(R.drawable.agricultureforestry);
                    holder.date.setBackgroundResource(R.color.agricultureforestry);
                    break;
                case 2:
                    holder.committee.setImageResource(R.drawable.assemblyoperation);
                    holder.date.setBackgroundResource(R.color.assemblyoperation);
                    break;
                case 3:
                    holder.committee.setImageResource(R.drawable.defense);
                    holder.date.setBackgroundResource(R.color.defense);
                    break;
                case 4:
                    holder.committee.setImageResource(R.drawable.educationculture);
                    holder.date.setBackgroundResource(R.color.educationculture);
                    break;
                case 5:
                    holder.committee.setImageResource(R.drawable.environmentlabor);
                    holder.date.setBackgroundResource(R.color.environmentlabor);
                    break;
                case 6:
                    holder.committee.setImageResource(R.drawable.foreign);
                    holder.date.setBackgroundResource(R.color.foreign);
                    break;
                case 7:
                    holder.committee.setImageResource(R.drawable.futurecreation);
                    holder.date.setBackgroundResource(R.color.futurecreation);
                    break;
                case 8:
                    holder.committee.setImageResource(R.drawable.healthwelfare);
                    holder.date.setBackgroundResource(R.color.healthwelfare);
                    break;
                case 9:
                    holder.committee.setImageResource(R.drawable.industrycommercial);
                    holder.date.setBackgroundResource(R.color.industrycommercial);
                    break;
                case 10:
                    holder.committee.setImageResource(R.drawable.information);
                    holder.date.setBackgroundResource(R.color.information);
                    break;
                case 11:
                    holder.committee.setImageResource(R.drawable.law);
                    holder.date.setBackgroundResource(R.color.law);
                    break;
                case 12:
                    holder.committee.setImageResource(R.drawable.politicalaffairs);
                    holder.date.setBackgroundResource(R.color.politicalaffairs);
                    break;
                case 13:
                    holder.committee.setImageResource(R.drawable.safety);
                    holder.date.setBackgroundResource(R.color.safety);
                    break;
                case 14:
                    holder.committee.setImageResource(R.drawable.strategyfinance);
                    holder.date.setBackgroundResource(R.color.strategyfinance);
                    break;
                case 15:
                    holder.committee.setImageResource(R.drawable.traffic);
                    holder.date.setBackgroundResource(R.color.traffic);
                    break;
                case 16:
                    holder.committee.setImageResource(R.drawable.womenfamily);
                    holder.date.setBackgroundResource(R.color.womenfamily);
                    break;
            }
        }

        holder.title.setText(lawEntityObject.title.toString());
        holder.lawID.setText(String.valueOf(lawEntityObject.lawID));

        holder.date.setText(lawEntityObject.date.toString());
        holder.proposer.setText(lawEntityObject.proposer.toString());
        holder.viewCount.setText(String.valueOf(lawEntityObject.view));
        holder.agree_count.setText(String.valueOf(lawEntityObject.agree_count));
        holder.disagree_count.setText(String.valueOf(lawEntityObject.disagree_count));

        if (lawEntityObject.likeFlag) {
            holder.agree_Button.setChecked(true);
        } else if (!lawEntityObject.likeFlag) {
            holder.agree_Button.setChecked(false);
        }
        if (lawEntityObject.dislikeFlag) {
            holder.disagree_Button.setChecked(true);
        } else if (!lawEntityObject.dislikeFlag) {
            holder.disagree_Button.setChecked(false);
        }
        if (lawEntityObject.interFlag) {
            holder.interest_law_mark.setChecked(true);
        } else if (!lawEntityObject.interFlag) {
            holder.interest_law_mark.setChecked(false);
        }

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, LawDetailActivity.class);

                intent.putExtra("lawID", lawEntityObject.lawID);
                Log.e("JSONAdapter:lawID", String.valueOf(lawEntityObject.lawID));

                context.startActivity(intent);
            }
        });

        holder.committee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, LawDetailActivity.class);

                intent.putExtra("lawID", lawEntityObject.lawID);
                Log.e("JSONAdapter:lawID", String.valueOf(lawEntityObject.lawID));

                context.startActivity(intent);
            }
        });

        holder.interest_law_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                holder.interest_law_mark.setChecked(false);
                String LAWID = holder.lawID.getText().toString();
                SERVER_URL_LAW_LIKE = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER,"laws",LAWID,"like");
                Log.e("SERVER_URL_LAW_LIKE", SERVER_URL_LAW_LIKE);

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

                            //업로드는 타임 및 리드타임을 넉넉히 준다.
                            OkHttpClient client = MySingleton.sharedInstance().httpClient;

                            //요청 Body 세팅==> 그전 Query Parameter세팅과 같은 개념
                            RequestBody fileUploadBody = MB.build();

                            //요청 세팅
                            Request request = new Request.Builder()
                                    .url(SERVER_URL_LAW_LIKE)
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
                                            holder.interest_law_mark.setChecked(true);
                                            lawEntityObject.interFlag = true;
                                        } else {
                                            holder.interest_law_mark.setChecked(false);
                                            lawEntityObject.interFlag = false;
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


                Log.e("즐겨찾기 버튼 눌림", "rue");

                fragment.refreshInterestLawList();

            }
        });

        holder.agree_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String LAWID = holder.lawID.getText().toString();
                SERVER_URL_LAWS_PROS_CONS = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER,"laws",LAWID,"rating");
                Log.e("LAWS_PROS_CONS", SERVER_URL_LAWS_PROS_CONS);

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
                            MB.addFormDataPart("like", String.valueOf(1));

                            OkHttpClient client = MySingleton.sharedInstance().httpClient;

                            //요청 Body 세팅==> 그전 Query Parameter세팅과 같은 개념
                            RequestBody fileUploadBody = MB.build();

                            //요청 세팅
                            Request request = new Request.Builder()
                                    .url(SERVER_URL_LAWS_PROS_CONS)
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
                                final boolean agreeFlag = jsonObject.getBoolean("likeFlag");
                                final boolean disagreeFlag = jsonObject.getBoolean("dislikeFlag");

                                Log.e("좋아요 수 확인", String.valueOf(agreeCount));
                                Log.e("싫어요 수 확인", String.valueOf(disagreeCount));

                                view.post(new Runnable(){
                                    @Override
                                    public void run() {

                                        holder.agree_count.setText(String.valueOf(agreeCount));
                                        holder.agree_Button.setChecked(agreeFlag);
                                        holder.disagree_count.setText(String.valueOf(disagreeCount));
                                        holder.disagree_Button.setChecked(disagreeFlag);

                                        if(holder.disagree_Button.isChecked()){
                                            holder.disagree_Button.setChecked(false);
                                        }
                                        lawEntityObject.likeFlag = true;
                                        lawEntityObject.agree_count += 1;
                                    }
                                });
                            }

                        } catch (UnknownHostException une) {
                            e("AsyncLawInsertune", une.toString());
                        } catch (UnsupportedEncodingException uee) {
                            e("AsyncLawInsertuee", uee.toString());
                        } catch (Exception e) {
                            e("AsyncLawInserte", e.toString());
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

        holder.disagree_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String LAWID = holder.lawID.getText().toString();
                SERVER_URL_LAWS_PROS_CONS = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER,"laws",LAWID,"rating");
                Log.e("LAWS_PROS_CONS", SERVER_URL_LAWS_PROS_CONS);

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
                            MB.addFormDataPart("like", String.valueOf(2));

                            OkHttpClient client = MySingleton.sharedInstance().httpClient;

                            //요청 Body 세팅==> 그전 Query Parameter세팅과 같은 개념
                            RequestBody fileUploadBody = MB.build();

                            //요청 세팅
                            Request request = new Request.Builder()
                                    .url(SERVER_URL_LAWS_PROS_CONS)
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
                                final boolean agreeFlag = jsonObject.getBoolean("likeFlag");
                                final boolean disagreeFlag = jsonObject.getBoolean("dislikeFlag");
                                Log.e("좋아요 수 확인", String.valueOf(agreeCount));
                                Log.e("싫어요 수 확인", String.valueOf(disagreeCount));

                                view.post(new Runnable(){
                                    @Override
                                    public void run() {
                                        holder.agree_count.setText(String.valueOf(agreeCount));
                                        holder.agree_Button.setChecked(agreeFlag);
                                        holder.disagree_count.setText(String.valueOf(disagreeCount));
                                        holder.disagree_Button.setChecked(disagreeFlag);
                                        if(holder.agree_Button.isChecked()){
                                            holder.agree_Button.setChecked(false);
                                        }
                                        lawEntityObject.dislikeFlag = true;
                                        lawEntityObject.disagree_count += 1;
                                    }
                                });
                            }

                        } catch (UnknownHostException une) {
                            e("AsyncLawInsertune", une.toString());
                        } catch (UnsupportedEncodingException uee) {
                            e("AsyncLawInsertuee", uee.toString());
                        } catch (Exception e) {
                            e("AsyncLawInserte", e.toString());
                        } finally {
                            if (response != null) {
                                response.close();
                            }
                        }
                    }
                }).start();

            }
        });

    }

    @Override
    public int getItemCount() {
        return lawEntityObjects.size();
    }

    public int getCommittee(String string) {
        if (string.equals("농림축산식품해양수산위원회")) {
            return 1;
        } else if (string.equals("국회운영위원회")) {
            return 2;
        } else if (string.equals("국방위원회")) {
            return 3;
        } else if (string.equals("교육문화체육관광위원회")) {
            return 4;
        } else if (string.equals("환경노동위원회")) {
            return 5;
        } else if (string.equals("외교통일위원회")) {
            return 6;
        } else if (string.equals("미래창조과학방송통신위원회")) {
            return 7;
        } else if (string.equals("보건복지위원회")) {
            return 8;
        } else if (string.equals("산업통상자원위원회")) {
            return 9;
        } else if (string.equals("정보위원회")) {
            return 10;
        } else if (string.equals("법제사법위원회")) {
            return 11;
        } else if (string.equals("정무위원회")) {
            return 12;
        } else if (string.equals("안전행정위원회")) {
            return 13;
        } else if (string.equals("기획재정위원회")) {
            return 14;
        } else if (string.equals("국토교통위원회")) {
            return 15;
        } else if (string.equals("여성가족위원회")) {
            return 16;
        } else if (TextUtils.isEmpty(string)) {
            return 0;
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView committee;
        public final TextView lawID;
        public final TextView date;
        public final TextView title;
        public final TextView proposer;
        public final TextView viewCount;
        public final TextView agree_count;
        public final TextView disagree_count;
        public final ToggleButton interest_law_mark;
        public final ToggleButton agree_Button;
        public final ToggleButton
                disagree_Button;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            committee = (ImageView) view.findViewById(R.id.committee);
            lawID = (TextView) view.findViewById(R.id.law_id);
            date = (TextView) view.findViewById(R.id.submit_date);
            title = (TextView) view.findViewById(R.id.law_title);
            proposer = (TextView) view.findViewById(R.id.init_member);
            viewCount = (TextView) view.findViewById(R.id.law_view_count);
            agree_count = (TextView) view.findViewById(R.id.law_good_count);
            disagree_count = (TextView) view.findViewById(R.id.law_bad_count);
            interest_law_mark = (ToggleButton) view.findViewById(R.id.interest_law_Btn);
            agree_Button = (ToggleButton) view.findViewById(R.id.law_good_Btn);
            disagree_Button = (ToggleButton) view.findViewById(R.id.law_bad_Btn);
        }
    }

}
