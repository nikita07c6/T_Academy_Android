package kr.co.citizoomproject.android.citizoom.Member;

/**
 * Created by ccei on 2016-08-12.
 */
public class RepVotingStatusObject {
    public int rep_id;
    public String vote;

    public RepVotingStatusObject() {
    }

    public RepVotingStatusObject(int rep_id, String vote) {
        this.rep_id = rep_id;
        this.vote = vote;
    }
}
