package kr.co.citizoomproject.android.citizoom.Law;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.citizoomproject.android.citizoom.R;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-28.
 */
public class LawDetailContentFragment extends Fragment {
    public static int increment;
    static LawDetailActivity owner;
    private static TextView lawcontent;
    private static String pdfurl;
    private static String contents;
    static Bundle bundle;

    public LawDetailContentFragment() {
    }

    public static LawDetailContentFragment newInstance(Bundle bun) {
        LawDetailContentFragment lawDetailContentFragment = new LawDetailContentFragment();
        bundle = bun;
        if(bundle != null){
            pdfurl = bundle.getString("pdfurl");
            contents = bundle.getString("contents");
            lawDetailContentFragment.setArguments(bundle);
        }
        return lawDetailContentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = (View) inflater.inflate(R.layout.law_detail_content, container, false);

        owner = (LawDetailActivity) getActivity();

        if(bundle != null){
            lawcontent = (TextView) view.findViewById(R.id.law_content);
            Log.i("mylog", "contents : " + contents);
            lawcontent.setText(contents);

            ImageView loadPDF = (ImageView) view.findViewById(R.id.view_original_law);
            if(TextUtils.isEmpty(pdfurl)){
                loadPDF.setVisibility(View.INVISIBLE);
            } else if(!TextUtils.isEmpty(pdfurl)){
                loadPDF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(pdfurl), "application/pdf");
                        startActivity(intent);
                    }
                });
            }
        }
        return view;
    }

}
