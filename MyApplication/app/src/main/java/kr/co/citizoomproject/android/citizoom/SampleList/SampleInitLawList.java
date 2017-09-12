package kr.co.citizoomproject.android.citizoom.SampleList;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-07-29.
 */
public class SampleInitLawList  {
    private static ArrayList<Integer> imageResources;

    static {
        imageResources = new ArrayList<Integer>();
        imageResources.add(R.drawable.initlawtest_03);
        imageResources.add(R.drawable.initlawtest_03);
        imageResources.add(R.drawable.initlawtest_03);
        imageResources.add(R.drawable.initlawtest_03);
        imageResources.add(R.drawable.initlawtest_03);
        imageResources.add(R.drawable.initlawtest_03);

    }

    private static HashMap<Integer, String> nameMaps;

    static {
        nameMaps = new HashMap<Integer, String>();
        //nameMaps.put(R.drawable.test_12, "금융회사의 지배구조에 관한 법률 일부개정법률안");
    }

    public static ArrayList<Integer> getArrayList() {

        return imageResources;
    }
   /* public static String getIssueName(Integer key) {
        return nameMaps.get(key);
    }*/
}
