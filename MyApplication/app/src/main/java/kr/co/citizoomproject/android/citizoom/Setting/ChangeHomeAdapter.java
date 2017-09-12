package kr.co.citizoomproject.android.citizoom.Setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import kr.co.citizoomproject.android.citizoom.Join.JoinActivity;
import kr.co.citizoomproject.android.citizoom.Join.JoinSettingFilterActivity;
import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;
import static android.util.Log.getStackTraceString;
import static android.util.Log.isLoggable;

/**
 * Created by ccei on 2016-08-24.
 */
public class ChangeHomeAdapter extends RecyclerView.Adapter<ChangeHomeAdapter.ViewHolder> {
    public AddressObject addressObjects;
    public String selectedAddr;
    Context current;

    public ChangeHomeAdapter(Context context, AddressObject resources) {
        this.addressObjects = resources;
        current = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_address_item, parent, false);
        return new ViewHolder(view);
    }

    public void setSelectedAddr(String string){
        selectedAddr = string;
    }

    public String getSelectedAddr(){
        return selectedAddr;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AddressObject addressObject = addressObjects;
        Log.e("주소목록adap", addressObject.address.get(position));

        holder.address.setText(addressObject.address.get(position));

        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAddr = addressObject.address.get(position);

                setSelectedAddr(selectedAddr);


                new HomeChangeAsync(selectedAddr).execute();

                AlertDialog.Builder builder = new AlertDialog.Builder(current);

                builder.setMessage("주소가 등록되었습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                                if(current instanceof ChangeHomeActivity) {
                                    ((ChangeHomeActivity)current).finish();
                                }
                                if(current instanceof JoinActivity) {

                                }
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressObjects.address.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView address;
        public final ToggleButton select;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            address = (TextView) view.findViewById(R.id.address_item);
            select = (ToggleButton) view.findViewById(R.id.select_address);
        }
    }

    public class HomeChangeAsync extends AsyncTask<Void, Void, String> {
        boolean flag;
        String address;

        public HomeChangeAsync(String address) {
            this.address = address;
        }

        @Override
        protected String doInBackground(Void... params) {
            Response response = null;
            FormBody fd = null;

            try {

                fd = new FormBody.Builder()
                        .add("location", address)
                        .build();

                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_CHANGE_LOCATION)
                        .post(fd)
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
                }
            } catch (UnknownHostException une) {
                e("주소변경1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("주소변경2", uee.toString());
            } catch (Exception e) {
                e("주소변경3", e.toString());
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

        }


    }
}