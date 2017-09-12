package kr.co.citizoomproject.android.citizoom.Setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import kr.co.citizoomproject.android.citizoom.Law.LawJSONReqListAdapter;
import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-17.
 */
public class ChangeHomeActivity extends AppCompatActivity {
    RecyclerView rv;
    String address;
    EditText addressInsert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_change_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.start_setting_user_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_back);

        rv = (RecyclerView)findViewById(R.id.address_recycler);
        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));

        addressInsert = (EditText) findViewById(R.id.address_insert);

        ImageView search = (ImageView)findViewById(R.id.search_btn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address = String.valueOf(addressInsert.getText());
                new LocationChangeAsync(address).execute();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class LocationChangeAsync extends AsyncTask<Void, Void, AddressObject> {
        boolean flag;
        String inserted;

        public LocationChangeAsync(String inserted) {
            this.inserted = inserted;
        }

        @Override
        protected AddressObject doInBackground(Void... params) {
            Response response = null;

            try {
                String url = NetworkDefineConstant.SERVER_URL_SEARCH_LOCATION + inserted;
                Log.e("주소url", url);


                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                Log.e("response==>", String.valueOf(responseCode));


                if (flag) {
                    e("response결과", response.message()); //읃답에 대한 메세지(OK)
                    //e("response응답바디", response.body().string()); //json으로 변신

                    return ParseDataParseHandler.getJSONAddressReqeustAllList(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("거주지변경1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("거주지변경2", uee.toString());
            } catch (Exception e) {
                e("거주지변경3", e.toString());
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
        protected void onPostExecute(AddressObject result) {
            if(result != null){
                ChangeHomeAdapter changeHomeAdapter = new ChangeHomeAdapter(ChangeHomeActivity.this, result);
                changeHomeAdapter.notifyDataSetChanged();
                rv.setAdapter(changeHomeAdapter);
            }

        }
    }


}
