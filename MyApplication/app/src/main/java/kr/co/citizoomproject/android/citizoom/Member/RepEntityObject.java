package kr.co.citizoomproject.android.citizoom.Member;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ccei on 2016-08-03.
 */
public class RepEntityObject {
    public String objectID;
    public int rep_id;
    public String name;
    public String picture;
    public String election_number;
    public ArrayList<String> committee = new ArrayList<String>();
    public String local_constituencies;
    public String birth_date;
    public String birth_place;
    public String party;
    public String twitter_link;
    public String facebook_link;
    public ArrayList<String> career_arr = new ArrayList<String>();
    public String academy;
    public String phone;
    public String vote_rate;
    public String email;
    public Boolean interFlag;

    public RepEntityObject() {    }

    public RepEntityObject(String objectID, int rep_id, String name, String picture, String election_number,
                           ArrayList<String> committee, String local_constituencies, String birth_date,
                           String birth_place, String party, String twitter_link, String facebook_link,
                           ArrayList<String> career_arr, String academy, String phone, String vote_rate, String email, Boolean interFlag) {
        this.objectID = objectID;
        this.rep_id = rep_id;
        this.name = name;
        this.picture = picture;
        this.election_number = election_number;
        this.committee = committee;
        this.local_constituencies = local_constituencies;
        this.birth_date = birth_date;
        this.birth_place = birth_place;
        this.party = party;
        this.twitter_link = twitter_link;
        this.facebook_link = facebook_link;
        this.career_arr = career_arr;
        this.academy = academy;
        this.phone = phone;
        this.vote_rate = vote_rate;
        this.email = email;
        this.interFlag = interFlag;
    }
}
