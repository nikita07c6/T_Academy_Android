package kr.co.citizoomproject.android.citizoom.Issue;


import android.text.BoringLayout;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ccei on 2016-08-21.
 */
public class IssueGetObject {
    public String register_id;
    public String question;
    public ArrayList<IssueAnswerObject> choice_arr = new ArrayList<>();
    public String date;
    public String nickname;
    public boolean voteFlag;
    public int choiceFlag;
    public int totalCount;
    public String issue_id;
    public String profile_image;

    public IssueGetObject() {
    }
}
