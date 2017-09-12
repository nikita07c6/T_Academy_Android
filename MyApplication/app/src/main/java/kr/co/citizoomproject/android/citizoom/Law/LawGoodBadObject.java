package kr.co.citizoomproject.android.citizoom.Law;

/**
 * Created by ccei on 2016-08-13.
 */
public class LawGoodBadObject {
    String message;
    int agree;
    int disagree;

    public LawGoodBadObject() {
    }

    public LawGoodBadObject(String message, int agree, int disagree) {
        this.message = message;
        this.agree = agree;
        this.disagree = disagree;
    }
}
