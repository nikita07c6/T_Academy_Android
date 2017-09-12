package kr.co.citizoomproject.android.citizoom.Law;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import kr.co.citizoomproject.android.citizoom.Member.RepVotingStatusObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-01.
 */
public class LawDetailMemberDialogFragment extends DialogFragment {
    public static int increment;
    static LawDetailActivity owner;
    static RecyclerView rv;

    public LawDetailMemberDialogFragment() {
    }

    public static LawDetailMemberDialogFragment newInstance(int initValue) {
        LawDetailMemberDialogFragment lawDetailMemberDialogFragment = new LawDetailMemberDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        lawDetailMemberDialogFragment.setArguments(bundle);
        return lawDetailMemberDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.law_detail_interest_member_voting_status_dialog, container);

        ImageView close = (ImageView) view.findViewById(R.id.dialog_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        rv = (RecyclerView) view.findViewById(R.id.interest_member_voting_status_recyclerview);

        owner = (LawDetailActivity) getActivity();

        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));
        // 다이어로그에 의원번호랑 상태밖에 없어..ㅠㅠ
        //rv.setAdapter(new LawMemberDialogRecyclerViewAdapter(MyApplication.getMyContext(), SampleInitLawList.getArrayList()));

        // remove dialog title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    public static class LawMemberDialogRecyclerViewAdapter extends RecyclerView.Adapter<LawMemberDialogRecyclerViewAdapter.ViewHolder> {
        private ArrayList<RepVotingStatusObject> member_name;

        public LawMemberDialogRecyclerViewAdapter(Context context, ArrayList<RepVotingStatusObject> resources) {
            this.member_name = resources;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView member_image;
            public final TextView member_name;
            public final ImageView vote_status;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                member_image = (ImageView) view.findViewById(R.id.vote_member_image);
                member_name = (TextView) view.findViewById(R.id.vote_member_name);
                vote_status = (ImageView) view.findViewById(R.id.vote_status);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.law_detail_interest_member_voting_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) { }

        @Override
        public int getItemCount() {
            return member_name.size();
        }
    }

    public static class AsyncLawJSONList extends AsyncTask<String, Integer, ArrayList<LawEntityObject>> {
        ProgressDialog dialog;

        @Override
        protected ArrayList<LawEntityObject> doInBackground(String... params) {
            Response response = null;
            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .header("user_id", PropertyManager.getInstance().getUserId())
                        .url(NetworkDefineConstant.SERVER_URL_LAW)
                        .build();
                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                //int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONLawRequestAllList(
                            new StringBuilder(responseBody.string()));
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
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(owner, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<LawEntityObject> result) {
            dialog.dismiss();

            if (result != null && result.size() > 0) {
                /*LawMemberDialogRecyclerViewAdapter lawMemberDialogRecyclerViewAdapter = new LawMemberDialogRecyclerViewAdapter(MyApplication.getMyContext(), result);
                rv.setAdapter(lawMemberDialogRecyclerViewAdapter);
                lawMemberDialogRecyclerViewAdapter.notifyDataSetChanged();*/
            }
        }
    }
}
