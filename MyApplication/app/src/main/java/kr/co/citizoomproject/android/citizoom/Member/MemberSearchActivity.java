package kr.co.citizoomproject.android.citizoom.Member;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-26.
 */
public class MemberSearchActivity extends AppCompatActivity {
    public String[] nameArr = new String[300];
    RecyclerView rv;
    ArrayAdapter<String> arrayAdapter = null;
    AutoCompleteTextView autoEdit;
    ImageView nothing;

    String changeURL;
    String changeWord;

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncSearchMemberJSONList().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_member);

        nothing = (ImageView) findViewById(R.id.nothing_law);

        autoEdit = (AutoCompleteTextView) findViewById(R.id.autoedit);

        autoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeWord = String.valueOf(editable);
            }
        });

        ImageView search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncSearchMemberJSONList2(changeWord).execute();
            }
        });

        rv = (RecyclerView) findViewById(R.id.member_all_list);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    } // onCreate

    public class AsyncSearchMemberJSONList extends AsyncTask<String, Integer, ArrayList<SearchMemberObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<SearchMemberObject> doInBackground(String... params) {
            Response response = null;
            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .header("user_id", PropertyManager.getInstance().getUserId())
                        .url("http://52.78.101.62:3000/rep/list")
                        .build();

                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();

                if (flag) {
                    return ParseDataParseHandler.getJSONSearchMemberAllList(new StringBuilder(responseBody.string()), nameArr);
                }
            } catch (UnknownHostException une) {
                e("의원찾기1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("의원찾기2", uee.toString());
            } catch (Exception e) {
                e("의원찾기3", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return null;

        }

        protected void onPostExecute(ArrayList<SearchMemberObject> result) {

            if (result.size() == 0) {
                nothing.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
            Log.e("결과사이즈", String.valueOf(result.size()));


            if (result != null && result.size() > 0) {
                nothing.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);

                MemberSearchAdapter memberSearchAdapter = new MemberSearchAdapter(MemberSearchActivity.this, result);
                memberSearchAdapter.notifyDataSetChanged();
                rv.setAdapter(memberSearchAdapter);

                arrayAdapter = new ArrayAdapter<String>(MyApplication.getMyContext(), R.layout.autocomplete_item, nameArr);
                autoEdit.setAdapter(arrayAdapter);
                Log.e("memberArray", nameArr[0]);

            }

        }
    }

    public class MemberSearchAdapter extends RecyclerView.Adapter<MemberSearchAdapter.ViewHolder> {
        private ArrayList<SearchMemberObject> searchMemberObjects;

        public MemberSearchAdapter(Context context, ArrayList<SearchMemberObject> resources) {
            this.searchMemberObjects = resources;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_member_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final SearchMemberObject searchMemberObject = searchMemberObjects.get(position);

            Glide.with(MemberSearchActivity.this).load(searchMemberObject.image).into(holder.picture);
            holder.name.setText(searchMemberObject.name);

            if (searchMemberObject.party.equals("국민의당")) {
                holder.party.setImageResource(R.drawable.kookminuidanglogo);
            } else if (searchMemberObject.party.equals("무소속")) {
                holder.party.setImageResource(R.drawable.musosoklogo);
            } else if (searchMemberObject.party.equals("더불어민주당")) {
                holder.party.setImageResource(R.drawable.theminjulogo);
            } else if (searchMemberObject.party.equals("새누리당")) {
                holder.party.setImageResource(R.drawable.senurilogo);
            } else if (searchMemberObject.party.equals("정의당")) {
                holder.party.setImageResource(R.drawable.junguidanglogo);
            }

            holder.local.setText(searchMemberObject.local_constituencies);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, NationalMemberDetailActivity.class);

                    intent.putExtra("repID", searchMemberObject.repID);
                    Log.e("Adapter_rep_id", String.valueOf(searchMemberObject.repID));

                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return searchMemberObjects.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final CircleImageView picture;
            public final TextView name;
            public final ImageView party;
            public final TextView local;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                picture = (CircleImageView) view.findViewById(R.id.member_image);
                name = (TextView) view.findViewById(R.id.member_name);
                party = (ImageView) view.findViewById(R.id.party_name);
                local = (TextView) view.findViewById(R.id.district);
            }
        }
    }

    public class AsyncSearchMemberJSONList2 extends AsyncTask<String, Integer, ArrayList<SearchMemberObject>> {
        ProgressDialog dialog;

        CharSequence sequence;

        public AsyncSearchMemberJSONList2(CharSequence sequence) {
            this.sequence = sequence;
        }

        @Override
        protected ArrayList<SearchMemberObject> doInBackground(String... params) {
            Response response = null;
            changeURL = "http://52.78.101.62:3000/rep/search?text=" + sequence;
            Log.i("tag : ", changeURL);

            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .header("user_id", PropertyManager.getInstance().getUserId())
                        .url(changeURL)
                        .build();
                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                //int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONSearchMemberAllList(new StringBuilder(responseBody.string()), nameArr);
                }
            } catch (UnknownHostException une) {
                e("fileUpLoad", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("fileUpLoad", uee.toString());
            } catch (Exception e) {
                e("fileUpLoad", e.toString());
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
            dialog = ProgressDialog.show(MemberSearchActivity.this, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<SearchMemberObject> result) {
            dialog.dismiss();
            if (result.size() == 0) {
                nothing.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
            Log.e("결과사이즈", String.valueOf(result.size()));


            if (result != null && result.size() > 0) {
                nothing.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);

                MemberSearchAdapter memberSearchAdapter = new MemberSearchAdapter(MemberSearchActivity.this, result);
                memberSearchAdapter.notifyDataSetChanged();
                rv.setAdapter(memberSearchAdapter);

                arrayAdapter = new ArrayAdapter<String>(MyApplication.getMyContext(), R.layout.autocomplete_item, nameArr);
                autoEdit.setAdapter(arrayAdapter);
                Log.e("memberArray", nameArr[0]);

            }
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }
} // MainSearchActivity
