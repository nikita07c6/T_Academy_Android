package kr.co.citizoomproject.android.citizoom.Join;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.IOException;
import java.io.InputStream;

import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-08-27.
 */
public class JoinInquireActivity extends AppCompatActivity{

    private String assetTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inquire_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.inquire_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_back);

        TextView inquire = (TextView) findViewById(R.id.inquire_text);

        try {
            assetTxt = readText("inquire.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        inquire.setText(assetTxt);


    }
    private String readText(String file) throws IOException {
        InputStream is = getAssets().open(file);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        String text = new String(buffer);

        return text;
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

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
