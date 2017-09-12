package kr.co.citizoomproject.android.citizoom.SampleList;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-07-28.
 */
public class SampleMemberList {
    private static ArrayList<Integer> imageResources;

    static {
        imageResources = new ArrayList<Integer>();
        imageResources.add(R.drawable.member_replace_01);
        imageResources.add(R.drawable.member_replace_01);
        imageResources.add(R.drawable.member_replace_01);
        imageResources.add(R.drawable.member_replace_01);
        imageResources.add(R.drawable.member_replace_01);
        imageResources.add(R.drawable.member_replace_01);
        imageResources.add(R.drawable.member_replace_01);
        imageResources.add(R.drawable.member_replace_01);
    }

    private static HashMap<Integer, String> nameMaps;

    static {
        nameMaps = new HashMap<Integer, String>();
        nameMaps.put(R.drawable.member_replace_01, "의원이름");
        nameMaps.put(R.drawable.member_replace_01, "의원이름");
        nameMaps.put(R.drawable.member_replace_01, "의원이름");
        nameMaps.put(R.drawable.member_replace_01, "의원이름");
        nameMaps.put(R.drawable.member_replace_01, "의원이름");
        nameMaps.put(R.drawable.member_replace_01, "의원이름");
        nameMaps.put(R.drawable.member_replace_01, "의원이름");
        nameMaps.put(R.drawable.member_replace_01, "의원이름");
    }

    public static ArrayList<Integer> getArrayList() {

        return imageResources;
    }
    public static String getMemberName(Integer key) {
        return nameMaps.get(key);
    }
}
