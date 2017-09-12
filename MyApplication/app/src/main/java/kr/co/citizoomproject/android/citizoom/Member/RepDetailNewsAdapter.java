package kr.co.citizoomproject.android.citizoom.Member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-08-08.
 */
public class RepDetailNewsAdapter extends RecyclerView.Adapter<RepDetailNewsAdapter.ViewHolder> {
    private ArrayList<RepNewsEntityObject> repNewsEntityObjects;
    Date date;
    String originDate;

    public RepDetailNewsAdapter(Context context, ArrayList<RepNewsEntityObject> resources) {
        this.repNewsEntityObjects = resources;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.national_member_detail_news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RepNewsEntityObject repNewsEntityObject = repNewsEntityObjects.get(position);
        Log.e("onBindviewHolder", repNewsEntityObject.title);
        //String title = String.valueOf(Html.fromHtml(repNewsEntityObject.title));
        holder.title.setMovementMethod(LinkMovementMethod.getInstance());
        holder.title.setClickable(true);
        holder.title.setText((Html.fromHtml("<a href=" + repNewsEntityObject.originallink
        + ">" + repNewsEntityObject.title + "</a>")));

        String description = String.valueOf(Html.fromHtml(repNewsEntityObject.description));
        holder.description.setText(description);

        String inputText = repNewsEntityObject.date;
        SimpleDateFormat inputFormat = new SimpleDateFormat
                ("EEE',' MMM dd yyyy HH:mm:ss 'Z'", Locale.KOREA);

        SimpleDateFormat outputFormat =
                new SimpleDateFormat("yyyy년 MMM dd일 aa hh:mm");
        date = new Date();
        try {
            date = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputText = outputFormat.format(date);
        holder.date.setText(outputText);
    }

    @Override
    public int getItemCount() {
        return repNewsEntityObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public final TextView description;
        public final TextView date;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.news_title);
            description = (TextView) view.findViewById(R.id.news_description);
            date = (TextView) view.findViewById(R.id.news_date);
        }
    }
}