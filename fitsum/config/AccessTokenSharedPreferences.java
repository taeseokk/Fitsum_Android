package org.fitsum.config;

import android.content.Context;
import android.content.SharedPreferences;

public class AccessTokenSharedPreferences {
    private static final String PREFS = "prefs";
    //Access Token 은 교환주기가 짧은 토큰
    //Refresh Token 은 교환주기가 김. 얘가 Access Token을 발급해줌.
    private static final String Access_Token = "X-AUTH-ACCESS-TOKEN";
    private static final String Refresh_Token = "X-AUTH-REFRESH-TOKEN";
    private Context mContext;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefsEditor;
    private static AccessTokenSharedPreferences instance;

    public static synchronized AccessTokenSharedPreferences init(Context context) {
        if(instance == null)
            instance = new AccessTokenSharedPreferences(context);
        return instance;
    }

    private AccessTokenSharedPreferences(Context context) {
        mContext = context;
        prefs = mContext.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();
    }

    public static void setAccessToken(String value) {
        prefsEditor.putString(Access_Token, value).commit();
    }

    public static String getAccessToken(String defValue) {
        return prefs.getString(Access_Token,defValue);
    }

    public static void setRefreshToken(String value) {
        prefsEditor.putString(Refresh_Token, value).commit();
    }

    public static String getRefreshToken(String defValue) {
        return prefs.getString(Refresh_Token,defValue);
    }

    public static void clearToken() {
        prefsEditor.clear().apply();
    }

}
