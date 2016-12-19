package me.lancer.xupt.ui.application;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.wuxiaolong.androidutils.library.LogUtil;
import com.wuxiaolong.androidutils.library.SharedPreferencesUtil;

/**
 * Created by HuangFangzhi on 2016/12/15.
 */

public class ApplicationInstance extends Application {

    private String number, name;
    public static String eduCookie, libCookie;
    private boolean course, score, user;

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isNight = SharedPreferencesUtil.getBoolean(this, ApplicationParameter.ISNIGHT, false);
        LogUtil.e("isNight=" + isNight);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEduCookie() {
        return eduCookie;
    }

    public void setEduCookie(String eduCookie) {
        this.eduCookie = eduCookie;
    }

    public String getLibCookie() {
        return libCookie;
    }

    public void setLibCookie(String libCookie) {
        this.libCookie = libCookie;
    }

    public boolean isCourse() {
        return course;
    }

    public void setCourse(boolean course) {
        this.course = course;
    }

    public boolean isScore() {
        return score;
    }

    public void setScore(boolean score) {
        this.score = score;
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }
}
