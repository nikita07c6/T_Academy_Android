package kr.co.citizoomproject.android.citizoom;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.Issue.IssueAnswerObject;
import kr.co.citizoomproject.android.citizoom.Issue.IssueGetObject;
import kr.co.citizoomproject.android.citizoom.Law.LawCommentEntityObject;
import kr.co.citizoomproject.android.citizoom.Law.LawDetailCommentFragment;
import kr.co.citizoomproject.android.citizoom.Law.LawEntityObject;
import kr.co.citizoomproject.android.citizoom.Law.LawGraphGetObject;
import kr.co.citizoomproject.android.citizoom.Law.LawGraphListObject;
import kr.co.citizoomproject.android.citizoom.Member.LocalRepEntityObject;
import kr.co.citizoomproject.android.citizoom.Member.NationalMemberDetailInitLawFragment;
import kr.co.citizoomproject.android.citizoom.Member.RepEntityObject;
import kr.co.citizoomproject.android.citizoom.Member.RepInitLawObject;
import kr.co.citizoomproject.android.citizoom.Member.RepNewsEntityObject;
import kr.co.citizoomproject.android.citizoom.Member.RepVotingStatusObject;
import kr.co.citizoomproject.android.citizoom.Member.SearchMemberObject;
import kr.co.citizoomproject.android.citizoom.Setting.AddressObject;
import kr.co.citizoomproject.android.citizoom.Setting.ProfileObject;

/**
 * Created by ccei on 2016-07-21.
 */
public class ParseDataParseHandler {
    public static LawGraphGetObject getJSONLawGraphRequest(StringBuilder buf) {
        JSONObject jsonObject = null;
        LawGraphGetObject entity = new LawGraphGetObject();

        try {
            jsonObject = new JSONObject(buf.toString());

            entity.total = jsonObject.getInt("total");
            entity.agree_cnt = jsonObject.getInt("agree_cnt");
            entity.disagree_cnt = jsonObject.getInt("disagree_cnt");

            JSONObject man_Agree = jsonObject.getJSONObject("man_agree");
            LawGraphListObject manAgreeList = new LawGraphListObject();
            manAgreeList.twenty = man_Agree.getInt("20");
            manAgreeList.thirty = man_Agree.getInt("30");
            manAgreeList.forty = man_Agree.getInt("40");
            manAgreeList.fifty = man_Agree.getInt("50");
            manAgreeList.sixty = man_Agree.getInt("60");
            entity.man_agree = manAgreeList;

            JSONObject man_Disagree = jsonObject.getJSONObject("man_disagree");
            LawGraphListObject manDisagreeList = new LawGraphListObject();
            manDisagreeList.twenty = man_Disagree.getInt("20");
            manDisagreeList.thirty = man_Disagree.getInt("30");
            manDisagreeList.forty = man_Disagree.getInt("40");
            manDisagreeList.fifty = man_Disagree.getInt("50");
            manDisagreeList.sixty = man_Disagree.getInt("60");
            entity.man_disagree = manDisagreeList;

            JSONObject woman_Agree = jsonObject.getJSONObject("woman_agree");
            LawGraphListObject womanAgreeList = new LawGraphListObject();
            womanAgreeList.twenty = woman_Agree.getInt("20");
            womanAgreeList.thirty = woman_Agree.getInt("30");
            womanAgreeList.forty = woman_Agree.getInt("40");
            womanAgreeList.fifty = woman_Agree.getInt("50");
            womanAgreeList.sixty = woman_Agree.getInt("60");
            entity.woman_agree = womanAgreeList;

            JSONObject woman_Disagree = jsonObject.getJSONObject("woman_disagree");
            LawGraphListObject womanDisagreeList = new LawGraphListObject();
            womanDisagreeList.twenty = woman_Disagree.getInt("20");
            womanDisagreeList.thirty = woman_Disagree.getInt("30");
            womanDisagreeList.forty = woman_Disagree.getInt("40");
            womanDisagreeList.fifty = woman_Disagree.getInt("50");
            womanDisagreeList.sixty = woman_Disagree.getInt("60");
            entity.woman_disagree = womanDisagreeList;

        } catch (JSONException je) {
            Log.e("주소파싱", "JSON파싱 중 에러발생", je);
        }
        return entity;
    }

    public static ProfileObject getJSONProfileRequest(StringBuilder buf) {
        JSONObject jsonObject = null;
        ProfileObject entity = new ProfileObject();

        try {
            jsonObject = new JSONObject(buf.toString());

            entity.id = jsonObject.getString("_id");
            entity.fbid = jsonObject.getString("fbId");
            entity.location = jsonObject.getString("location");
            entity.nickname = jsonObject.getString("nickname");
            entity.profile_image = jsonObject.getString("profile_image");

            JSONArray topics = jsonObject.getJSONArray("interesting_topics");
            int topicsSize = topics.length();
            for (int i = 0; i < topicsSize; i++) {
                entity.interesting_topics.add(topics.getString(i));
            }

        } catch (JSONException je) {
            Log.e("주소파싱", "JSON파싱 중 에러발생", je);
        }
        return entity;
    }

    public static AddressObject getJSONAddressReqeustAllList(StringBuilder buf) {
        JSONObject jsonObject = null;
        AddressObject entity = new AddressObject();

        try {
            jsonObject = new JSONObject(buf.toString());

            JSONArray address = jsonObject.getJSONArray("addressArr");
            int addressSize = address.length();
            for (int i = 0; i < addressSize; i++) {
                entity.address.add(address.getString(i));
            }


        } catch (JSONException je) {
            Log.e("주소파싱", "JSON파싱 중 에러발생", je);
        }
        return entity;
    }

    public static ArrayList<IssueGetObject> getJSONIssueRequestAllList(StringBuilder buf) {

        ArrayList<IssueGetObject> jsonAllList = null;
        JSONObject jsonObject = null;
        try {
            jsonAllList = new ArrayList<IssueGetObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("issue_arr");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                IssueGetObject entity = new IssueGetObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.register_id = jData.getString("register_id");
                entity.question = jData.getString("question");

                JSONArray choiceArr = jData.getJSONArray("choice_arr");
                int choiceSize = choiceArr.length();
                Log.e("choice갯수", String.valueOf(choiceSize));
                for (int j = 0; j < choiceSize; j++) {
                    JSONObject obj = choiceArr.getJSONObject(j);
                    final IssueAnswerObject answerObject = new IssueAnswerObject();
                    answerObject.choice = obj.getString("choice");
                    answerObject.vote_count = obj.getInt("vote_count");
                    entity.choice_arr.add(answerObject);
                }

                entity.date = jData.getString("date");
                entity.nickname = jData.getString("nickname");
                entity.voteFlag = jData.getBoolean("voteFlag");
                entity.choiceFlag = jData.getInt("choiceFlag");
                entity.totalCount = jData.getInt("totalCount");
                Log.e("totalCount", String.valueOf(entity.totalCount));
                entity.issue_id = jData.getString("issue_id");
                entity.profile_image = jData.getString("profile_image");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }



    public static ArrayList<LawEntityObject> getJSONLawRequestAllList(StringBuilder buf) {

        ArrayList<LawEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;
        try {
            jsonAllList = new ArrayList<LawEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            Log.e("total", String.valueOf(total));
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("law_arr");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                LawEntityObject entity = new LawEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.lawID = jData.getInt("law_id");
                entity.committee = jData.getString("committee");
                entity.date = jData.getString("date");
                entity.title = jData.getString("title");
                entity.proposer = jData.getString("proposer");
                entity.view = jData.getInt("view");
                entity.agree_count = jData.getInt("agree_count");
                entity.disagree_count = jData.getInt("disagree_count");
                entity.likeFlag = jData.getBoolean("likeFlag");
                entity.dislikeFlag = jData.getBoolean("dislikeFlag");
                entity.interFlag = jData.getBoolean("interFlag");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<LawEntityObject> getJSONInterestLawRequestAllList(StringBuilder buf) {

        ArrayList<LawEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;
        try {
            jsonAllList = new ArrayList<LawEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            Log.e("total", String.valueOf(total));
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("law_list");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                LawEntityObject entity = new LawEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.lawID = jData.getInt("law_id");
                entity.committee = jData.getString("committee");
                entity.date = jData.getString("date");
                entity.title = jData.getString("title");
                entity.proposer = jData.getString("proposer");
                entity.view = jData.getInt("view");
                entity.agree_count = jData.getInt("agree_count");
                entity.disagree_count = jData.getInt("disagree_count");
                entity.likeFlag = jData.getBoolean("likeFlag");
                entity.dislikeFlag = jData.getBoolean("dislikeFlag");
                entity.interFlag = jData.getBoolean("interFlag");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }


    public static LawEntityObject getJSONLawDetailRequestList(StringBuilder buf) {
        LawEntityObject entity = new LawEntityObject();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(buf.toString());

            JSONObject jData = jsonObject;

            Log.e("LawDetail", String.valueOf(jData));

            entity.committee = jData.getString("committee");
            entity.lawID = jData.getInt("law_id");
            entity.date = jData.getString("date");
            entity.processStatus = jData.getString("processStatus");
            entity.title = jData.getString("title");
            entity.proposer = jData.getString("proposer");
            entity.contents = jData.getString("contents");
            entity.view = jData.getInt("view");
            entity.agree_count = jData.getInt("agree_count");
            entity.disagree_count = jData.getInt("disagree_count");
            entity.likeFlag = jData.getBoolean("likeFlag");
            entity.dislikeFlag = jData.getBoolean("dislikeFlag");
            entity.interFlag = jData.getBoolean("interFlag");
            entity.billPDFUrl = jData.getString("billPDFUrl");

        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return entity;
    }

    public static ArrayList<RepInitLawObject> getJSONRepInitLawRequestList(StringBuilder buf) {
        ArrayList<RepInitLawObject> jsonAllList = null;
        JSONArray jsonArray = null;
        try {
            jsonAllList = new ArrayList<RepInitLawObject>();
            JSONObject jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            NationalMemberDetailInitLawFragment.totalCount = jsonObject.getInt("total");
            Log.e("total", String.valueOf(total));
            if (total == 0) {
                return jsonAllList;
            }
            jsonArray = jsonObject.getJSONArray("law_arr");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                RepInitLawObject entity = new RepInitLawObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.title = jData.getString("title");
                entity.committee = jData.getString("committee");
                entity.date = jData.getString("date");
                entity.state = jData.getString("state");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }


    public static ArrayList<RepVotingStatusObject> getJSONRepVotingStatusRequestList(StringBuilder buf) {
        ArrayList<RepVotingStatusObject> jsonAllList = null;
        JSONArray jsonArray = null;
        try {
            jsonAllList = new ArrayList<RepVotingStatusObject>();
            JSONObject jsonObject = new JSONObject(buf.toString());
            jsonArray = jsonObject.getJSONArray("vote_arr");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                RepVotingStatusObject entity = new RepVotingStatusObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.rep_id = jData.getInt("rep_id");
                entity.vote = jData.getString("vote");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<SearchMemberObject> getJSONSearchMemberAllList(StringBuilder buf, String[] strings) {

        ArrayList<SearchMemberObject> jsonAllList = null;
        JSONArray jsonArray = null;
        try {
            jsonAllList = new ArrayList<SearchMemberObject>();
            JSONObject jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            if (total == 0) {
                return jsonAllList;
            }
            jsonArray = jsonObject.getJSONArray("rep_arr");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                SearchMemberObject entity = new SearchMemberObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.image = jData.getString("picture");
                entity.name = jData.getString("name");
                strings[i] = entity.name;
                entity.party = jData.getString("party");
                entity.repID = jData.getInt("rep_id");
                entity.local_constituencies = jData.getString("local_constituencies");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<LawEntityObject> getJSONSearchLawAllList(StringBuilder buf, ArrayList<String> strings) {

        ArrayList<LawEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;
        try {
            jsonAllList = new ArrayList<LawEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            Log.e("total", String.valueOf(total));
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("law_arr");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                LawEntityObject entity = new LawEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.lawID = jData.getInt("law_id");
                entity.committee = jData.getString("committee");
                entity.date = jData.getString("date");

                entity.title = jData.getString("title");
                strings.add(entity.title);

                entity.proposer = jData.getString("proposer");
                entity.view = jData.getInt("view");
                entity.agree_count = jData.getInt("agree_count");
                entity.disagree_count = jData.getInt("disagree_count");
                entity.likeFlag = jData.getBoolean("likeFlag");
                entity.dislikeFlag = jData.getBoolean("dislikeFlag");
                entity.interFlag = jData.getBoolean("interFlag");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }


    public static ArrayList<RepEntityObject> getJSONMemberRequestAllList(StringBuilder buf) {

        ArrayList<RepEntityObject> jsonAllList = null;
        JSONArray jsonArray = null;
        try {
            jsonAllList = new ArrayList<RepEntityObject>();
            JSONObject jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            if (total == 0) {
                return jsonAllList;
            }
            jsonArray = jsonObject.getJSONArray("rep_list");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                RepEntityObject entity = new RepEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.rep_id = jData.getInt("rep_id");
                entity.name = jData.getString("name");
                entity.picture = jData.getString("picture");
                entity.party = jData.getString("party");
                entity.local_constituencies = jData.getString("local_constituencies");
                entity.birth_date = jData.getString("birth_date");
                entity.birth_place = jData.getString("birth_place");
                entity.interFlag = jData.getBoolean("interFlag");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static LocalRepEntityObject getJSONMyRepRequestList(StringBuilder buf) {
        LocalRepEntityObject entity = new LocalRepEntityObject();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(buf.toString());

            JSONObject jData = jsonObject;

            entity.rep_id = jData.getInt("rep_id");
            entity.name = jData.getString("name");
            Log.e("Local국회의원 이름", entity.name);
            entity.picture = jData.getString("picture");
            entity.party = jData.getString("party");
            entity.committee = jData.getString("committee");
            entity.local_constituencies = jData.getString("local_constituencies");
            entity.birth_date = jData.getString("birth_date");
            entity.birth_place = jData.getString("birth_place");
            entity.facebook_link = jData.getString("facebook_link");
            entity.election_number = jData.getString("election_number");
            entity.twitter_link = jData.getString("twitter_link");
            entity.phone = jData.getString("phone");
            entity.vote_rate = jData.getString("vote_rate");
            entity.interFlag = jData.getBoolean("interFlag");

        } catch (JSONException je) {
            Log.e("LocalRepEntityObject", "JSON파싱 중 에러발생", je);
        }
        return entity;
    }

    public static RepEntityObject getJSONMyRepRequestAllList(StringBuilder buf) {
        RepEntityObject entity = new RepEntityObject();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(buf.toString());

            JSONObject jData = jsonObject;

            entity.rep_id = jData.getInt("rep_id");
            entity.name = jData.getString("name");
            Log.e("국회의원 이름", entity.name);
            entity.picture = jData.getString("picture");
            entity.party = jData.getString("party");

            JSONArray committee = jsonObject.getJSONArray("committee");
            int committeeSize = committee.length();
            if (committeeSize == 0) {
                entity.committee = null;
            } else {
                for (int i = 0; i < committeeSize; i++) {
                    entity.committee.add(committee.getString(i));
                }
            }
            entity.election_number = jData.getString("election_number");
            entity.local_constituencies = jData.getString("local_constituencies");
            entity.birth_date = jData.getString("birth_date");
            entity.birth_place = jData.getString("birth_place");
            entity.facebook_link = jData.getString("facebook_link");
            entity.twitter_link = jData.getString("twitter_link");

            JSONArray career = jsonObject.getJSONArray("career_arr");
            int careerSize = career.length();
            for (int i = 0; i < careerSize; i++) {
                entity.career_arr.add(career.getString(i));
            }

            entity.phone = jData.getString("phone");
            entity.vote_rate = jData.getString("vote_rate");
            entity.interFlag = jData.getBoolean("interFlag");

        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return entity;
    }

    public static ArrayList<LawCommentEntityObject> getJSONLawCommentRequestAllList(StringBuilder buf) {

        ArrayList<LawCommentEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;
        try {
            jsonAllList = new ArrayList<LawCommentEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            LawDetailCommentFragment.commentTotal = total;
            JSONArray jsonArray = jsonObject.getJSONArray("comment_list");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                LawCommentEntityObject entity = new LawCommentEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.writer_id = jData.getString("user_id");
                entity.nickname = jData.getString("nickname");
                entity.contents = jData.getString("contents");
                entity.date = jData.getString("date");
                entity.likeCount = jData.getInt("likeCount");
                entity.dislikeCount = jData.getInt("dislikeCount");
                entity.isAgree = jData.getBoolean("isAgree");
                entity.commentID = jData.getString("comment_id");
                entity.likeFlag = jData.getBoolean("likeFlag");
                entity.dislikeFlag = jData.getBoolean("dislikeFlag");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<LawCommentEntityObject> getJSONLawCommentRequestLikeList(StringBuilder buf) {

        ArrayList<LawCommentEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;
        try {
            jsonAllList = new ArrayList<LawCommentEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            LawDetailCommentFragment.commentTotal = total;
            JSONArray jsonArray = jsonObject.getJSONArray("like_comment");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                LawCommentEntityObject entity = new LawCommentEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.writer_id = jData.getString("user_id");
                entity.nickname = jData.getString("nickname");
                entity.contents = jData.getString("contents");
                entity.date = jData.getString("date");
                entity.likeCount = jData.getInt("likeCount");
                entity.dislikeCount = jData.getInt("dislikeCount");
                entity.isAgree = jData.getBoolean("isAgree");
                entity.commentID = jData.getString("comment_id");
                entity.likeFlag = jData.getBoolean("likeFlag");
                entity.dislikeFlag = jData.getBoolean("dislikeFlag");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<LawCommentEntityObject> getJSONLawCommentRequestDislikeList(StringBuilder buf) {

        ArrayList<LawCommentEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;
        try {
            jsonAllList = new ArrayList<LawCommentEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            LawDetailCommentFragment.commentTotal = total;
            JSONArray jsonArray = jsonObject.getJSONArray("dislike_comment");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                LawCommentEntityObject entity = new LawCommentEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.writer_id = jData.getString("user_id");
                entity.nickname = jData.getString("nickname");
                entity.contents = jData.getString("contents");
                entity.date = jData.getString("date");
                entity.likeCount = jData.getInt("likeCount");
                entity.dislikeCount = jData.getInt("dislikeCount");
                entity.isAgree = jData.getBoolean("isAgree");
                entity.commentID = jData.getString("comment_id");
                entity.likeFlag = jData.getBoolean("likeFlag");
                entity.dislikeFlag = jData.getBoolean("dislikeFlag");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<RepNewsEntityObject> getJSONRepNewsRequestList(StringBuilder buf) {

        ArrayList<RepNewsEntityObject> jsonAllList = null;
        JSONArray rootJsonArray = null;
        try {
            jsonAllList = new ArrayList<RepNewsEntityObject>();
            rootJsonArray = new JSONArray(buf.toString());
            int jsonSize = rootJsonArray.length();
            for (int i = 0; i < jsonSize; i++) {
                JSONObject reqNews = rootJsonArray.getJSONObject(i);
                RepNewsEntityObject valueObject = new RepNewsEntityObject();

                JSONArray titleArray = reqNews.optJSONArray("title");
                valueObject.title = titleArray.optString(0);

                JSONArray originallink = reqNews.optJSONArray("originallink");
                valueObject.originallink = originallink.optString(0);

                JSONArray description = reqNews.optJSONArray("description");
                valueObject.description = description.optString(0);

                JSONArray pubDate = reqNews.optJSONArray("pubDate");
                valueObject.date = pubDate.optString(0);

                jsonAllList.add(valueObject);

            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }


}

