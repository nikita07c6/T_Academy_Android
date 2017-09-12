package kr.co.citizoomproject.android.citizoom.Setting;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.Member.RepNewsEntityObject;
import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-08-25.
 */
public class InterestCommitteeListAdapter extends RecyclerView.Adapter<InterestCommitteeListAdapter.ViewHolder> {
    public ArrayList<String> interesting_topics = new ArrayList<>();
    SettingProfileChangeActivity owner;

    public InterestCommitteeListAdapter(Context context) {
        owner = (SettingProfileChangeActivity) context;

    }

    public void setResources(ArrayList<String> resources) {
        interesting_topics = resources;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_topic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String topic = interesting_topics.get(position);

        Log.i("topic", topic);
        if(topic.equals("농림식품해양수산")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_agricultureforestry).into(holder.topic);
        }
        if(topic.equals("국회운영")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_assemblyoperation_off).into(holder.topic);
        }
        if(topic.equals("국방")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_defense_off).into(holder.topic);
        }
        if(topic.equals("교육문화체육관광")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_educationculture_off).into(holder.topic);
        }
        if(topic.equals("환경노동")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_environmentlabor_off).into(holder.topic);
        }
        if(topic.equals("외교통일")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_foreign_off).into(holder.topic);
        }
        if(topic.equals("미래창조과학방송통신")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_futurecreation_off).into(holder.topic);
        }
        if(topic.equals("보건복지")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_healthwelfare_off).into(holder.topic);
        }
        if(topic.equals("산업통상자원")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_industrycommercial_off).into(holder.topic);
        }
        if(topic.equals("정보")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_information_off).into(holder.topic);
        }
        if(topic.equals("법제사법")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_law_off).into(holder.topic);
        }
        if(topic.equals("정무")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_politicalaffairs_off).into(holder.topic);
        }
        if(topic.equals("안전행정")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_safety).into(holder.topic);
        }
        if(topic.equals("기획재정")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_strategyfinance_off).into(holder.topic);
        }
        if(topic.equals("국토교통")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_traffic_off).into(holder.topic);
        }
        if(topic.equals("여성가족")){
            Glide.with(MyApplication.getMyContext()).load(R.drawable.setting_womenfamily_off).into(holder.topic);
        }
    }

    @Override
    public int getItemCount() {
        return interesting_topics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView topic;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            topic = (ImageView) view.findViewById(R.id.topic);
        }
    }
}