package kr.co.citizoomproject.android.citizoom.Setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-07-26.
 */
public class SettingActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_back);

/*        RelativeLayout change_profile = (RelativeLayout)findViewById(R.id.setting_profile_layout);
        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingProfileChangeActivity.class);
                startActivity(intent);
            }
        });*/

        RelativeLayout setting_facebook = (RelativeLayout)findViewById(R.id.app_facebook_layout);
        setting_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url ="http://www.facebook.com/Citizoom-911005955689557/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        RelativeLayout setting_send_email = (RelativeLayout)findViewById(R.id.inquire_layout);
        setting_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("plain/text");

                String[] tos = { "teamcitizoom@gmail.com" };
                it.putExtra(Intent.EXTRA_EMAIL, tos);
                it.putExtra(Intent.EXTRA_SUBJECT, "Citizoom 문의,제안,신고 메일");
                it.putExtra(Intent.EXTRA_TEXT, "문의,제안,신고 내용을 입력하세요.");
                startActivity(it);
            }
        });

        RelativeLayout setting_notice = (RelativeLayout)findViewById(R.id.notice_layout);


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
}
