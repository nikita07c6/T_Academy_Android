package kr.co.citizoomproject.android.citizoom.Member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-08-17.
 */
public class RepDetailInitLawAdapter extends RecyclerView.Adapter<RepDetailInitLawAdapter.ViewHolder> {
    private ArrayList<RepInitLawObject> repInitLawObjects;

    public RepDetailInitLawAdapter(Context context, ArrayList<RepInitLawObject> resources) {
        this.repInitLawObjects = resources;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.national_member_detail_init_law_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RepInitLawObject repInitLawObject = repInitLawObjects.get(position);

        holder.title.setText(repInitLawObject.title);
        if(TextUtils.isEmpty(repInitLawObject.committee)){
            holder.committee.setText("미정");
        } else {
            holder.committee.setText(repInitLawObject.committee);
        }
        holder.date.setText(repInitLawObject.date);
        holder.state.setText(repInitLawObject.state);
    }

    @Override
    public int getItemCount() {
        return repInitLawObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public final TextView committee;
        public final TextView date;
        public final TextView state;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.law_name_content);
            committee = (TextView) view.findViewById(R.id.committee_init_law_content);
            date = (TextView) view.findViewById(R.id.submission_date_content);
            state = (TextView) view.findViewById(R.id.status_content);
        }
    }
}