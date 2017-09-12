package kr.co.citizoomproject.android.citizoom.Interest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.citizoomproject.android.citizoom.AsyncMemberJSONList;
import kr.co.citizoomproject.android.citizoom.Member.NationalMemberDetailActivity;
import kr.co.citizoomproject.android.citizoom.Member.RepEntityObject;
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
public class InterestingRepJsonAdapter extends RecyclerView.Adapter<InterestingRepJsonAdapter.ViewHolder> {
    String SERVER_URL_REP_LIKE;
    public ArrayList<RepEntityObject> repEntityObjects;
    ImageView nothing;

    public InterestingRepJsonAdapter(Context context, ArrayList<RepEntityObject> resources) {
        this.repEntityObjects = resources;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.national_member_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RepEntityObject repEntityObject = repEntityObjects.get(position);

        Log.e("onBindviewHolder", repEntityObject.name);

        if (repEntityObject.interFlag) {
            holder.interest_mark.setChecked(true);
        } else if (!repEntityObject.interFlag) {
            holder.interest_mark.setChecked(false);
        }

        holder.interest_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                holder.interest_mark.setChecked(false);
                String RepID = String.valueOf(repEntityObject.rep_id);
                SERVER_URL_REP_LIKE = String.format("http://%s:%d/%s/%s/%s",
                        NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER,"rep",RepID,"like");
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
                                            holder.interest_mark.setChecked(true);
                                            repEntityObject.interFlag = true;
                                        } else {
                                            holder.interest_mark.setChecked(false);
                                            repEntityObject.interFlag = false;
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

                new AsyncMemberJSONList(InterestingRepJsonAdapter.this,nothing).execute();
            }
        });

        holder.name.setText(repEntityObject.name.toString());

        //holder.picture.setImageURI(Uri.parse(memberEntityObject.picture));

        //Glide.with(MyApplication.getMyContext()).load(memberEntitiyObject.picture).into(holder.picture);
        Glide.with(MyApplication.getMyContext()).load(repEntityObject.picture).into(holder.picture);

        holder.local_constituencies.setText(repEntityObject.local_constituencies.toString());

        String inputText = repEntityObject.birth_date.toString();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy년 MMM dd일");
        Date date = new Date();
        try {
            date = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputText = outputFormat.format(date);

        String comma;
        if(repEntityObject.birth_place.isEmpty()){
            comma = "";
        } else {
            comma = ", ";
        }
        holder.birth_date.setText(outputText + comma + repEntityObject.birth_place);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, NationalMemberDetailActivity.class);

                intent.putExtra("repID", repEntityObject.rep_id);
                Log.e("Adapter_rep_id", String.valueOf(repEntityObject.rep_id));

                context.startActivity(intent);

            }
        });

        if(repEntityObject.party.equals("국민의당")){
            holder.party.setImageResource(R.drawable.kookminuidanglogo);
        } else if(repEntityObject.party.equals("무소속")){
            holder.party.setImageResource(R.drawable.musosoklogo);
        } else if(repEntityObject.party.equals("더불어민주당")){
            holder.party.setImageResource(R.drawable.theminjulogo);
        } else if(repEntityObject.party.equals("새누리당")){
            holder.party.setImageResource(R.drawable.senurilogo);
        } else if(repEntityObject.party.equals("정의당")){
            holder.party.setImageResource(R.drawable.junguidanglogo);
        }

    }

    @Override
    public int getItemCount() {
        return repEntityObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final ImageView picture;
        public final TextView local_constituencies;
        public final TextView birth_date;
        public final ImageView party;
        public final ToggleButton interest_mark;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.member_name);
            picture = (ImageView) view.findViewById(R.id.interest_member);
            birth_date = (TextView) view.findViewById(R.id.member_birth_hometown);
            local_constituencies = (TextView) view.findViewById(R.id.district);
            party = (ImageView) view.findViewById(R.id.party_name);
            interest_mark = (ToggleButton) view.findViewById(R.id.interest_mark);
        }
    }

}