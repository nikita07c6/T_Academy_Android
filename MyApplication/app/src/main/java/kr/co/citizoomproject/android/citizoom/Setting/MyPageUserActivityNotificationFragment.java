package kr.co.citizoomproject.android.citizoom.Setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.R;
import kr.co.citizoomproject.android.citizoom.SampleList.SampleInitLawList;

/**
 * Created by ccei on 2016-08-01.
 */
public class MyPageUserActivityNotificationFragment extends Fragment {
    public static int increment;
    static MyPageActivity owner;

    public MyPageUserActivityNotificationFragment() {
    }

    public static MyPageUserActivityNotificationFragment newInstance(int initValue) {
        MyPageUserActivityNotificationFragment myPageUserActivityNotificationFragment = new MyPageUserActivityNotificationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        myPageUserActivityNotificationFragment.setArguments(bundle);
        return myPageUserActivityNotificationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.my_page_user_notification_fragment, container, false);

        Bundle initBundle = getArguments();
        increment += initBundle.getInt("value");
        owner = (MyPageActivity)getActivity();

        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));
        rv.setAdapter(new UserActivityRecyclerViewAdapter(MyApplication.getMyContext(), SampleInitLawList.getArrayList()));

        return rv;
    }

    public class UserActivityRecyclerViewAdapter extends RecyclerView.Adapter<UserActivityRecyclerViewAdapter.ViewHolder> {
        private ArrayList<Integer> activity_condition;

        public UserActivityRecyclerViewAdapter(Context context, ArrayList<Integer> resources) {
            this.activity_condition = resources;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView activity_condition;
            public final TextView activity_contents;
            public final TextView activity_date;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                activity_condition = (ImageView) view.findViewById(R.id.activity_condition);
                activity_contents = (TextView) view.findViewById(R.id.user_activity_contents);
                activity_date = (TextView) view.findViewById(R.id.user_activity_date);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_page_user_activity_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.mView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                }
            });

        }
        @Override
        public int getItemCount() {
            return activity_condition.size();
        }
    }
}