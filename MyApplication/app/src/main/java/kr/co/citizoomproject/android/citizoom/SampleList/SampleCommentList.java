package kr.co.citizoomproject.android.citizoom.SampleList;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-07-29.
 */
public class SampleCommentList {
    private static ArrayList<String> usernick;

    static {
        usernick = new ArrayList<String>();
        usernick.add("사용자닉네임");
        usernick.add("사용자닉네임");
        usernick.add("사용자닉네임");
        usernick.add("사용자닉네임");
        usernick.add("사용자닉네임");
        usernick.add("사용자닉네임");
        usernick.add("사용자닉네임");
        usernick.add("사용자닉네임");

    }

    private static HashMap<Integer, String> nameMaps;

    static {
        nameMaps = new HashMap<Integer, String>();
        nameMaps.put(R.drawable.test_12, "금융회사의 지배구조에 관한 법률 일부개정법률안");
        nameMaps.put(R.drawable.test_12, "금융회사의 지배구조에 관한 법률 일부개정법률안");
        nameMaps.put(R.drawable.test_12, "금융회사의 지배구조에 관한 법률 일부개정법률안");
        nameMaps.put(R.drawable.test_12, "금융회사의 지배구조에 관한 법률 일부개정법률안");
        nameMaps.put(R.drawable.test_12, "금융회사의 지배구조에 관한 법률 일부개정법률안");
        nameMaps.put(R.drawable.test_12, "금융회사의 지배구조에 관한 법률 일부개정법률안");
        nameMaps.put(R.drawable.test_12, "금융회사의 지배구조에 관한 법률 일부개정법률안");
        nameMaps.put(R.drawable.test_12, "금융회사의 지배구조에 관한 법률 일부개정법률안");

    }

    public static ArrayList<String> getArrayList() {

        return usernick;
    }
    public static String getLawName(Integer key) {
        return nameMaps.get(key);
    }

}
