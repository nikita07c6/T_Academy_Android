package kr.co.citizoomproject.android.citizoom.Law;

/**
 * Created by ccei on 2016-08-03.
 */
public class LawCommentEntityObject {
    public int total;
    public String lawID;
    public String commentID;
    public String writer_id;
    public String nickname;
    public String date;
    public String contents;
    public boolean isAgree;
    public boolean likeFlag;
    public boolean dislikeFlag;
    public int likeCount;
    public int dislikeCount;

    public LawCommentEntityObject() {    }

    public LawCommentEntityObject(int total, String lawID, String commentID, String writer_id,
                                  String nickname, String date, String contents, boolean isAgree,
                                  boolean likeFlag, boolean dislikeFlag, int likeCount,
                                  int dislikeCount) {
        this.total = total;
        this.lawID = lawID;
        this.commentID = commentID;
        this.writer_id = writer_id;
        this.nickname = nickname;
        this.date = date;
        this.contents = contents;
        this.isAgree = isAgree;
        this.likeFlag = likeFlag;
        this.dislikeFlag = dislikeFlag;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }
}
