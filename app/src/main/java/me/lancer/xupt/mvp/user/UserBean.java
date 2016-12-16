package me.lancer.xupt.mvp.user;

import java.util.ArrayList;
import java.util.List;

import me.lancer.xupt.mvp.course.CourseModel;
import me.lancer.xupt.mvp.score.ScoreModel;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class UserBean {

    private String userName;
    private String userNumber;
    private String userSex;
    private String userCollege;
    private String userMajor;
    private String userClass;
    private String userImage;
    private String userCookie;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserCollege() {
        return userCollege;
    }

    public void setUserCollege(String userCollege) {
        this.userCollege = userCollege;
    }

    public String getUserMajor() {
        return userMajor;
    }

    public void setUserMajor(String userMajor) {
        this.userMajor = userMajor;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserCookie() {
        return userCookie;
    }

    public void setUserCookie(String userCookie) {
        this.userCookie = userCookie;
    }
}
