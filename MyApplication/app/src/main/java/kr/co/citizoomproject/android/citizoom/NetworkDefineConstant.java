package kr.co.citizoomproject.android.citizoom;

import android.content.Intent;

import kr.co.citizoomproject.android.citizoom.Law.LawDetailActivity;

/**
 * Created by ccei on 2016-07-21.
 */
public class NetworkDefineConstant {
    public static final String HOST_URL = "52.78.101.62";

    public static final int PORT_NUMBER = 3000;

    public static final String SERVER_URL_NEW_USER = "http://" + HOST_URL + ":" + PORT_NUMBER + "/auth/user/check";

    public static final String SERVER_URL_LAW = "http://" + HOST_URL + ":" + PORT_NUMBER + "/laws";
    public static final String SERVER_URL_LAW_TOP = "http://" + HOST_URL + ":" + PORT_NUMBER + "/laws/top";
    public static final String SERVER_URL_INTEREST_LAW = "http://" + HOST_URL + ":" + PORT_NUMBER + "/" + PropertyManager.getInstance().getUserId() + "/interestinglaw";
    public static final String SERVER_URL_INTEREST_REP =  "http://" + HOST_URL + ":" + PORT_NUMBER + "/" + PropertyManager.getInstance().getUserId() + "/interestingrep";
    public static final String SERVER_URL_MY_REP = "http://" + HOST_URL + ":" + PORT_NUMBER + "/rep/" + PropertyManager.getInstance().getUserId() + "/local";
    public static final String SERVER_URL_SELECT_COMMENT_LAW = "http://" + HOST_URL + ":" + PORT_NUMBER + "/comment";
    public static final String SERVER_URL_ISSUE_INSERT = "http://" + HOST_URL + ":" + PORT_NUMBER + "/issues/list";
    public static final String SERVER_URL_ISSUE_DELETE = "http://" + HOST_URL + ":" + PORT_NUMBER + "/issues/";
    public static final String SERVER_URL_ISSUE_TOP = "http://" + HOST_URL + ":" + PORT_NUMBER + "/issues/top";
    public static final String SERVER_URL_FACEBOOK_TOKEN = "http://" + HOST_URL + ":" + PORT_NUMBER + "/auth/facebook/token";
    public static final String SERVER_URL_KAKAO_TOKEN = "http://" + HOST_URL + ":" + PORT_NUMBER + "/auth/kakao/token?access_token=";
    public static final String SERVER_URL_CHANGE_PROFILE_PIC = "http://" + HOST_URL + ":" + PORT_NUMBER + "/" + PropertyManager.getInstance().getUserId() + "/profile";
    public static final String SERVER_URL_CHANGE_NICK = "http://" + HOST_URL + ":" + PORT_NUMBER + "/" + PropertyManager.getInstance().getUserId() + "/nicknamecheck?nick=";
    public static final String SERVER_URL_SEARCH_LOCATION = "http://" + HOST_URL + ":" + PORT_NUMBER + "/" + PropertyManager.getInstance().getUserId() + "/findregionalrep?place=";
    public static final String SERVER_URL_CHANGE_TOPIC = "http://" + HOST_URL + ":" + PORT_NUMBER + "/" + PropertyManager.getInstance().getUserId() + "/topic";
    public static final String SERVER_URL_CHANGE_LOCATION = "http://" + HOST_URL + ":" + PORT_NUMBER + "/" + PropertyManager.getInstance().getUserId() + "/location";
    public static final String SERVER_URL_LOGIN = "http://" + HOST_URL + ":" + PORT_NUMBER + "/" + "auth/login";
    public static final String SERVER_URL_PROFILE = "http://" + HOST_URL + ":" + PORT_NUMBER + "/" + PropertyManager.getInstance().getUserId() + "/profile";




}
