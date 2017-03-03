package me.lancer.xupt.ui.widget;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by daiyiming on 2016/11/28.
 */

public class Config {

    private static final String SP_NAME = "config";
    private static final String SP_KEY_BIRTHDAY = "birthday";
    private static final String SP_KEY_ANIM_AGE = "anim_age";
    private static final String SP_KEY_MOTTO = "motto";

    private SharedPreferences mSP = null;

    public Config(Context context) {
        mSP = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void putBirthday(long timestamp) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putLong(SP_KEY_BIRTHDAY, timestamp);
        editor.apply();
    }

    public void putAnimAge(int age) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putInt(SP_KEY_ANIM_AGE, age);
        editor.apply();
    }

    public void putMotto(String motto) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(SP_KEY_MOTTO, motto);
        editor.apply();
    }

    public long getBirthday() {
        return mSP.getLong(SP_KEY_BIRTHDAY, 0);
    }

    public int getAnimAge() {
        return mSP.getInt(SP_KEY_ANIM_AGE, 0);
    }

    public String getMotto() {
        return mSP.getString(SP_KEY_MOTTO, "");
    }

    public void clear() {
        SharedPreferences.Editor editor = mSP.edit();
        editor.clear();
        editor.apply();
    }

    public boolean isEnable() {
        return getBirthday() > 0 && getAnimAge() > 0;
    }

}
