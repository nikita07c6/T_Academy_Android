package kr.co.citizoomproject.android.citizoom.Member;

import java.util.ArrayList;

/**
 * Created by ccei on 2016-08-18.
 */
public class LocalRepEntityObject {
    public int rep_id;
    public String name;
    public String picture;
    public String election_number;
    public String party;
    public String committee;
    public String local_constituencies;
    public String birth_date;
    public String birth_place;
    public String facebook_link;
    public String twitter_link;
    public String academy;
    public String room_number;
    public String phone;
    public String vote_rate;
    public String email;
    public Boolean interFlag;

    public LocalRepEntityObject() {
    }

    public LocalRepEntityObject(int rep_id, String name, String picture, String election_number,
                                String party, String committee, String local_constituencies,
                                String birth_date, String birth_place, String facebook_link,
                                String twitter_link, String academy, String room_number, String phone,
                                String vote_rate, String email, Boolean interFlag) {
        this.rep_id = rep_id;
        this.name = name;
        this.picture = picture;
        this.election_number = election_number;
        this.party = party;
        this.committee = committee;
        this.local_constituencies = local_constituencies;
        this.birth_date = birth_date;
        this.birth_place = birth_place;
        this.facebook_link = facebook_link;
        this.twitter_link = twitter_link;
        this.academy = academy;
        this.room_number = room_number;
        this.phone = phone;
        this.vote_rate = vote_rate;
        this.email = email;
        this.interFlag = interFlag;
    }
}
