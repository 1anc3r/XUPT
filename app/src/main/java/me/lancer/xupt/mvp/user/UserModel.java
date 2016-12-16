package me.lancer.xupt.mvp.user;

import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import me.lancer.xupt.util.ContentGetterSetter;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class UserModel {

    IUserPresenter presenter;

    String number, name, cookie;

    public UserModel(IUserPresenter presenter) {
        this.presenter = presenter;
    }

    public void loadUser(String number, String name, String cookie, boolean refresh) {
        this.number = number;
        this.name = name;
        this.cookie = cookie;
        ContentGetterSetter contentGetterSetter = new ContentGetterSetter("user_", number);
        String url = "http://222.24.19.201/xsgrxx.aspx?xh=" + number + "&xm=" + name + "&gnmkdm=N121501";
        String path = Environment.getExternalStorageDirectory().toString();
        String content;
        UserBean bean;
        if (!(content = contentGetterSetter.getContentFromFile(path)).contains("!error!") && !refresh) {
//            bean = getUserFromContent(content);
            bean = getUserFromJson(content);
            presenter.loadUserSuccess(bean);
            Log.e("loadUser", "loadUserSuccess.done");
        } else if (!(content = contentGetterSetter.getContentFromHtml(url, cookie)).contains("!error!") && refresh) {
            bean = getUserFromContent(content);
            content = setUserToJson(bean);
            contentGetterSetter.setContentToFile(path, content);
            presenter.loadUserSuccess(bean);
            Log.e("loadUser", "loadUserSuccess.done");
        } else {
            presenter.loadUserFailure("loadUserFailure.done");
            Log.e("loadUser", "loadUserFailure.done");
        }
    }

    public UserBean getUserFromContent(String content) {
        UserBean bean = new UserBean();
        Document document = Jsoup.parse(content);
        Element number = document.getElementById("xh");
        Element name = document.getElementById("xm");
        Element sex = document.getElementById("lbl_xb");
        Element colleague = document.getElementById("lbl_xy");
        Element major = document.getElementById("lbl_zymc");
        Element classes = document.getElementById("lbl_xzb");
        Element imageUrl = document.getElementById("xszp");
        String imageSuffix = imageUrl.attr("src");
        bean.setUserNumber(number.text());
        bean.setUserName(name.text());
        bean.setUserSex(sex.text());
        bean.setUserCollege(colleague.text());
        bean.setUserMajor(major.text());
        bean.setUserClass(classes.text());
        bean.setUserImage("http://222.24.19.201/" + imageSuffix);
        return bean;
    }

    public UserBean getUserFromJson(String json) {
        try {
            JSONObject jbUser = new JSONObject(json);
            UserBean bean = new UserBean();
            bean.setUserName((String) jbUser.get("name"));
            bean.setUserNumber((String) jbUser.get("number"));
            bean.setUserSex((String) jbUser.get("sex"));
            bean.setUserCollege((String) jbUser.get("college"));
            bean.setUserMajor((String) jbUser.get("major"));
            bean.setUserClass((String) jbUser.get("class"));
            bean.setUserImage((String) jbUser.get("image"));
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String setUserToJson(UserBean bean) {
        try {
            JSONObject jbUser = new JSONObject();
            jbUser.put("name", bean.getUserName());
            jbUser.put("number", bean.getUserNumber());
            jbUser.put("sex", bean.getUserSex());
            jbUser.put("college", bean.getUserCollege());
            jbUser.put("major", bean.getUserMajor());
            jbUser.put("class", bean.getUserClass());
            jbUser.put("image", bean.getUserImage());
            return jbUser.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "!error!----" + e.toString();
        }
    }
}
