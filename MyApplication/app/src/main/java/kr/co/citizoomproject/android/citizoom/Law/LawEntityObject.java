package kr.co.citizoomproject.android.citizoom.Law;

import java.lang.reflect.Array;

/**
 * Created by ccei on 2016-08-02.
 */
public class LawEntityObject {
    public String objectID;
    public int lawID;
    public String committee;
    public String date;
    public String processStatus;
    public String title;
    public String proposer;
    public String proposerType;
    public String contents;
    public int view;
    public int agree_count;
    public int disagree_count;
    public Array voteArr;
    public String repID;
    public String vote;
    public boolean likeFlag;
    public boolean dislikeFlag;
    public boolean interFlag;
    public String billPDFUrl;

    public LawEntityObject() {    }

    public LawEntityObject(String objectID, int lawID, String committee, String date,
                           String processStatus, String title, String proposer,
                           String proposerType, String contents, int view,
                           int agree_count, int disagree_count, Array voteArr,
                           String repID, String vote, boolean likeFlag,
                           boolean dislikeFlag, boolean interFlag, String billPDFUrl) {
        this.objectID = objectID;
        this.lawID = lawID;
        this.committee = committee;
        this.date = date;
        this.processStatus = processStatus;
        this.title = title;
        this.proposer = proposer;
        this.proposerType = proposerType;
        this.contents = contents;
        this.view = view;
        this.agree_count = agree_count;
        this.disagree_count = disagree_count;
        this.voteArr = voteArr;
        this.repID = repID;
        this.vote = vote;
        this.likeFlag = likeFlag;
        this.dislikeFlag = dislikeFlag;
        this.interFlag = interFlag;
        this.billPDFUrl = billPDFUrl;
    }
}
