package com.laundry.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.laundry.app.dto.user.PersonalInfoDto;

public class SharePreferenceManager {

    private static final String PREFERENCE_FILE_NAME = "laundry_preference";

    private static final String PREFERENCE_KEY_AUTHENTICATION_TOKEN = "authentication_token";

    private static final String PREFERENCE_KEY_AUTHENTICATION_USERNAME = "authentication_username";

    private static final String PREFERENCE_KEY_AUTHENTICATION_USERNAME_AVATAR = "authentication_username_avatar";

    private static final String PREFERENCE_KEY_AUTHENTICATION_USERNAME_AVATAR_SOCIAL = "authentication_username_avatar_social";

    private static final String PREFERENCE_KEY_USER_INFOMATION = "preference_user_infomation";

    private static final String PREFERENCE_KEY_ROLE = "role";

    private static final String PREFERENCE_VISITED_HOME = "visited_home";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_FILE_NAME,
                Activity.MODE_PRIVATE);
    }

    public static String getToken(Context context) {
        SharedPreferences preference = getSharedPreferences(context);
        return preference.getString(PREFERENCE_KEY_AUTHENTICATION_TOKEN, null);
    }

    public static void setToken(Context context, String token) {
        SharedPreferences preference = getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(PREFERENCE_KEY_AUTHENTICATION_TOKEN, token);
        editor.apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences preference = getSharedPreferences(context);
        return preference.getString(PREFERENCE_KEY_AUTHENTICATION_USERNAME, null);
    }

    public static void setUsername(Context context, String username) {
        SharedPreferences preference = getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(PREFERENCE_KEY_AUTHENTICATION_USERNAME, username);
        editor.apply();
    }

    public static String getMode(Context context) {
        SharedPreferences preference = getSharedPreferences(context);
        return preference.getString(PREFERENCE_KEY_ROLE, null);
    }

    public static void setMode(Context context, String mode) {
        SharedPreferences preference = getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(PREFERENCE_KEY_ROLE, mode);
        editor.apply();
    }

    public static boolean getVisitedHomeScreen(Context context) {
        SharedPreferences preference = getSharedPreferences(context);
        return preference.getBoolean(PREFERENCE_VISITED_HOME, false);
    }

    public static void hasVisitedHome(Context context, boolean hasVisited) {
        SharedPreferences preference = getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(PREFERENCE_VISITED_HOME, hasVisited);
        editor.apply();
    }

    public static String getUserAvatar(Context context) {
        SharedPreferences preference = getSharedPreferences(context);
        return preference.getString(PREFERENCE_KEY_AUTHENTICATION_USERNAME_AVATAR, null);
    }

    public static void setUserAvatar(Context context, String url) {
        SharedPreferences preference = getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(PREFERENCE_KEY_AUTHENTICATION_USERNAME_AVATAR, url);
        editor.apply();
    }

    public static String getUserAvatarSocialLogin(Context context) {
        SharedPreferences preference = getSharedPreferences(context);
        return preference.getString(PREFERENCE_KEY_AUTHENTICATION_USERNAME_AVATAR_SOCIAL, null);
    }

    public static void setUserAvatarSocialLogin(Context context, String url) {
        SharedPreferences preference = getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(PREFERENCE_KEY_AUTHENTICATION_USERNAME_AVATAR_SOCIAL, url);
        editor.apply();
    }

    /**
     * Set personal info sharedpreference
     * @param context
     * @param personalInfoDto
     */
    public static void setAccountInfomation(Context context, PersonalInfoDto personalInfoDto) {
        final Gson gson = new Gson();
        String accountInfomation = gson.toJson(personalInfoDto);
        SharedPreferences preference = getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(PREFERENCE_KEY_USER_INFOMATION, accountInfomation);
        editor.apply();
    }

    /**
     * Get personal info from shared preference
     * @param context
     * @return
     */
    public static PersonalInfoDto getAccountInfomation(Context context) {
        SharedPreferences preference = getSharedPreferences(context);
        Gson gson = new Gson();
        String accountInfomationStr = preference.getString(PREFERENCE_KEY_USER_INFOMATION, "");
        PersonalInfoDto personalInfoDto = gson.fromJson(accountInfomationStr, PersonalInfoDto.class);
        return personalInfoDto;
    }

}
