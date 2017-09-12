package kr.co.citizoomproject.android.citizoom.Law;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-08-01.
 */
public class LawDetailReviewDialogFragment extends DialogFragment {
    public static int increment;
    static LawDetailActivity owner;
    private static String status;
    static Bundle bundle;

    public LawDetailReviewDialogFragment() {
    }

    public static LawDetailReviewDialogFragment newInstance(Bundle bun) {
        LawDetailReviewDialogFragment lawDetailReviewDialogFragment = new LawDetailReviewDialogFragment();
        bundle = bun;
        if(bundle != null){
            status = bundle.getString("status");
            Log.e("심사현황", status);
            lawDetailReviewDialogFragment.setArguments(bundle);
        }
        return lawDetailReviewDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.law_detail_review_law_status_dialog, container);

        ImageView close = (ImageView) view.findViewById(R.id.dialog_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        owner = (LawDetailActivity) getActivity();

        ImageView lawStatus = (ImageView) view.findViewById(R.id.law_status);


        if (!status.isEmpty()) {
            String whatstatus = status;
            int thisstatus = getLawStatus(whatstatus);
            switch (thisstatus) {
                case 1:
                    lawStatus.setImageResource(R.drawable.stateofexamination1);
                    break;
                case 2:
                    lawStatus.setImageResource(R.drawable.stateofexamination2);
                    break;
                case 3:
                    lawStatus.setImageResource(R.drawable.stateofexamination3);
                    break;
                case 4:
                    lawStatus.setImageResource(R.drawable.stateofexamination4);
                    break;
                case 5:
                    lawStatus.setImageResource(R.drawable.stateofexamination5);
                    break;
                case 6:

                    break;
                case 7:

                    break;
                case 8:

                    break;
            }
        }

            // remove dialog title
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            return view;
        }


    public int getLawStatus(String string) {
        if (string.equals("접수")) {
            return 1;
        } else if (string.equals("위원회심사")) {
            return 2;
        } else if (string.equals("체계자구심사")) {
            return 3;
        } else if (string.equals("본회의심의")) {
            return 4;
        } else if (string.equals("이송")) {
            return 5;
        } else if (string.equals("폐기")) {
            return 6;
        } else if (string.equals("부결")) {
            return 7;
        } else if (string.equals("철회")) {
            return 8;
        }

        return 0;
    }
}