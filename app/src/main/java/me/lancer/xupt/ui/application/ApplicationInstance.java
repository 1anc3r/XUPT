package me.lancer.xupt.ui.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

/**
 * Created by HuangFangzhi on 2016/12/15.
 */

public class ApplicationInstance extends Application {

    private String number, name;
    public static String eduCookie, libCookie, CardCookie0, CardCookie1;
    private boolean course, score, user, rollcall;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean isNight = sharedPreferences.getBoolean(ApplicationParameter.ISNIGHT, false);
        Log.e("夜间模式", isNight+"");
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

    public String getCardCookie0() {
        return CardCookie0;
    }

    public void setCardCookie0(String CardCookie0) {
        this.CardCookie0 = CardCookie0;
    }

    public String getCardCookie1() {
        return CardCookie1;
    }

    public void setCardCookie1(String CardCookie1) {
        this.CardCookie1 = CardCookie1;
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

    public boolean isRollcall() {
        return rollcall;
    }

    public void setRollcall(boolean rollcall) {
        this.rollcall = rollcall;
    }
}
