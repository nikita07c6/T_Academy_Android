package kr.co.citizoomproject.android.citizoom;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PropertyManager {
    public static final String USER_ID = "userID";
    public static final String NICKNAME = "nickname";
    private static PropertyManager instance;
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        Context context = MyApplication.getMyContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    public String getUserId() {
        return mPrefs.getString(USER_ID, "");
    }

    public void setUserId(String userId) {
        mEditor.putString(USER_ID, userId);
        mEditor.commit();
    }

    public String getNickname() {
        return mPrefs.getString(NICKNAME, "");
    }

    public void setNickname(String nickname) {
        mEditor.putString(NICKNAME, nickname);
        mEditor.commit();
    }

    /*
     서버로 넘길 ID 또는 토큰 값
     */
    private static final String FIELD_FACEBOOK_ID = "facebookId";
    private static final String FIELD_FACEBOOK_TOKEN_KEY = "facebookToken";

    public void setFacebookId(String id) {
        mEditor.putString(FIELD_FACEBOOK_ID, id);
        mEditor.commit();
    }

    public String getFaceBookId() {
        return mPrefs.getString(FIELD_FACEBOOK_ID, "");
    }

    public void setFacebookToken(String token) {
        mEditor.putString(FIELD_FACEBOOK_TOKEN_KEY, token);
    }
    public String getFieldFacebookTokenKey() {
        return mPrefs.getString(FIELD_FACEBOOK_TOKEN_KEY, "");
    }

    private static final String FIELD_KAKAO_ACCESS_TOKEN_KEY = "kakaoAccessToken";
    private static final String FIELD_KAKAO_REFRESH_TOKEN_KEY = "kakaoRefreshToken";

    public void setFieldKakaoAccessTokenKey(String token) {
        mEditor.putString(FIELD_KAKAO_ACCESS_TOKEN_KEY, token);
    }

    public String getFieldKakaoAccessTokenKey(){
        return mPrefs.getString(FIELD_KAKAO_ACCESS_TOKEN_KEY, "");
    }

    public void setFieldKakaoRefreshTokenKey(String token) {
        mEditor.putString(FIELD_KAKAO_REFRESH_TOKEN_KEY, token);
    }

    public String getFieldKakaoRefreshTokenKey(){
        return mPrefs.getString(FIELD_KAKAO_REFRESH_TOKEN_KEY, "");
    }

}
