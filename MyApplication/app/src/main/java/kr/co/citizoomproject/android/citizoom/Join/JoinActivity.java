package kr.co.citizoomproject.android.citizoom.Join;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import kr.co.citizoomproject.android.citizoom.Setting.AddressObject;
import kr.co.citizoomproject.android.citizoom.Setting.ChangeHomeAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-22.
 */
public class JoinActivity extends AppCompatActivity {
    RecyclerView rv;
    EditText nickRegister, addressInsert;
    String address, nickStr, addrStr;
    private ChangeHomeAdapter changeHomeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_setting_user_info);

        final ImageView inquire = (ImageView)findViewById(R.id.inquire);
        inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinActivity.this, JoinInquireActivity.class);
                startActivity(intent);
            }
        });

        nickRegister = (EditText) findViewById(R.id.nickname_register);

        InputMethodManager imm = (InputMethodManager) getSystemService(MyApplication.getMyContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nickRegister.getWindowToken(), 0);

        ImageView changeNick = (ImageView) findViewById(R.id.nickname_check);
        changeNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nickRegister.getText())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);     // 여기서 this는 Activity의 this

                    // 여기서 부터는 알림창의 속성 설정
                    builder.setMessage("닉네임을 입력해주세요.")// 메세지 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정

                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                // 확인 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();    // 알림창 띄우기

                } else {
                    String string = String.valueOf(nickRegister.getText());
                    new NickRegisterAsync(string).execute();
                }
            }
        });

        rv = (RecyclerView) findViewById(R.id.address_recycler);
        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));

        addressInsert = (EditText) findViewById(R.id.address_insert);

        ImageView search = (ImageView) findViewById(R.id.join_addr);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address = String.valueOf(addressInsert.getText());
                new LocationChangeAsync(address).execute();

                InputMethodManager imm = (InputMethodManager) getSystemService(JoinActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addressInsert.getWindowToken(), 0);
            }
        });

        final ToggleButton privacyAgree = (ToggleButton) findViewById(R.id.privacy_btn);

        ImageView next = (ImageView) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nickRegister.getText().toString()) && !TextUtils.isEmpty(addressInsert.getText().toString()) && privacyAgree.isChecked()) {
                    Toast.makeText(JoinActivity.this, "닉네임은 필수 입력 항목입니다.", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(nickRegister.getText().toString()) && TextUtils.isEmpty(addressInsert.getText().toString()) && privacyAgree.isChecked()) {
                    Toast.makeText(JoinActivity.this, "선거구는 필수 입력 항목입니다.", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(nickRegister.getText().toString()) && !TextUtils.isEmpty(addressInsert.getText().toString()) && !privacyAgree.isChecked()) {
                    Toast.makeText(JoinActivity.this, "약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(nickRegister.getText().toString()) && !TextUtils.isEmpty(addressInsert.getText().toString()) && privacyAgree.isChecked()) {
                    Intent intent = new Intent(JoinActivity.this, JoinSettingFilterActivity.class);
                    intent.putExtra("nickname", nickStr);
                    addrStr = changeHomeAdapter.getSelectedAddr();
                    Log.e("주소", addrStr);
                    intent.putExtra("location", addrStr);
                    startActivity(intent);
                } else {
                    Toast.makeText(JoinActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public class NickRegisterAsync extends AsyncTask<Void, Void, String> {
        boolean flag;
        String registernick;

        public NickRegisterAsync(String changeNick) {
            this.registernick = changeNick;
        }

        @Override
        protected String doInBackground(Void... params) {
            Response response = null;

            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_CHANGE_NICK + registernick)
                        .build();

                //동기 방식
                response = client.newCall(request).execute();

                flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                Log.e("response==>", String.valueOf(responseCode));

                if (flag) {
                    e("response결과", response.message()); //읃답에 대한 메세지(OK)
                    //e("response응답바디", response.body().string()); //json으로 변신

                    StringBuilder buf = new StringBuilder(response.body().string());

                    JSONObject jsonObject = new JSONObject(buf.toString());
                    String str_temp = jsonObject + "";
                    String[] tempArr = str_temp.split(":");
                    String tpm2 = tempArr[1];
                    String nickCheck = tpm2.split("\"")[1];

                    Log.e("nickCheck", nickCheck);
                    if (nickCheck.equals("Fail")) {
                        return null;
                    } else {
                        PropertyManager.getInstance().setNickname(nickCheck);
                        return nickCheck;
                    }
                }
            } catch (UnknownHostException une) {
                e("닉네임설정1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("닉네임설정2", uee.toString());
            } catch (Exception e) {
                e("닉네임설정3", e.toString());
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
        protected void onPostExecute(final String s) {
            if (TextUtils.isEmpty(s) || s.equals(null)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);     // 여기서 this는 Activity의 this

                // 여기서 부터는 알림창의 속성 설정
                builder.setMessage("닉네임이 중복됩니다. 다시 입력해주세요.")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기            } else {
                nickRegister.setText(s);
            }
            if (!TextUtils.isEmpty(s)) {
                nickStr = registernick;

                // 닉네임 세팅
                PropertyManager.getInstance().setNickname(nickStr);

                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);     // 여기서 this는 Activity의 this

                // 여기서 부터는 알림창의 속성 설정
                builder.setMessage("사용가능한 닉네임입니다.")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기            } else {
            }

        }
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
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_SEARCH_LOCATION + inserted)
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
                e("거주지설정1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("거주지설정2", uee.toString());
            } catch (Exception e) {
                e("거주지설정3", e.toString());
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
            if (result == null) {
                Toast.makeText(JoinActivity.this, "이미 등록된 회원입니다.", Toast.LENGTH_SHORT).show();
            }
            if (result != null) {
                changeHomeAdapter = new ChangeHomeAdapter(JoinActivity.this, result);
                changeHomeAdapter.notifyDataSetChanged();
                rv.setAdapter(changeHomeAdapter);
            }

        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }


}
