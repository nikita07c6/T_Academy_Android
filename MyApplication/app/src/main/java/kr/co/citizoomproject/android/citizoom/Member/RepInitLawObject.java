package kr.co.citizoomproject.android.citizoom.Member;

/**
 * Created by ccei on 2016-08-17.
 */
public class RepInitLawObject {
    public int total;
    public String title;
    public String committee;
    public String date;
    public String state;

    public RepInitLawObject() {
    }

    public RepInitLawObject(int total, String title, String committee, String date, String state) {
        this.total = total;
        this.title = title;
        this.committee = committee;
        this.date = date;
        this.state = state;
    }
}
