package kr.co.citizoomproject.android.citizoom.Member;



/**
 * Created by ccei on 2016-08-08.
 */
public class RepNewsEntityObject {
    public String title;
    public String originallink;
    public String link;
    public String description;
    public String date;

    public RepNewsEntityObject() {
    }

    public RepNewsEntityObject(String title, String originallink, String link, String description, String date) {
        this.title = title;
        this.originallink = originallink;
        this.link = link;
        this.description = description;
        this.date = date;
    }
}
