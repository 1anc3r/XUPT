package me.lancer.xupt.mvp.loginedu;

import android.os.Environment;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.lancer.xupt.ui.application.ApplicationInstance;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class LoginEduModel {

    ILoginEduPresenter presenter;

    public LoginEduModel(ILoginEduPresenter presenter) {
        this.presenter = presenter;
    }

    public void loadCheckCode() {
        String url = "http://222.24.19.201/CheckCode.aspx";
        String path = Environment.getExternalStorageDirectory().toString();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                File dir = new File(path + "/me.lancer.xupt");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir.getPath() + "/CheckCode.gif");
                if (file.exists()) {
                    file.delete();
                }
                InputStream is = response.body().byteStream();
                OutputStream os = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int c;
                while ((c = is.read(b)) > 0) {
                    os.write(b, 0, c);
                }
                is.close();
                os.close();
                if (response.header("Set-Cookie") != null) {
                    String rawCookie = response.header("Set-Cookie");
                    String cookie = rawCookie.substring(0, rawCookie.indexOf(';'));
                    presenter.loadCheckCodeSuccess(cookie);
                    Log.e("loadCheckCode", "加载验证码成功!");
                } else {
                    presenter.loadCheckCodeFailure("加载验证码失败!缺失cookie");
                    Log.e("loadCheckCode", "加载验证码失败!缺失cookie");
                }
            } else {
                presenter.loadCheckCodeFailure("加载验证码失败!状态码:" + response.code());
                Log.e("loadCheckCode", "加载验证码失败!状态码:" + response.code());
            }
        } catch (IOException e) {
            presenter.loadCheckCodeFailure("加载验证码失败!状态码:" + e.toString());
            Log.e("loadCheckCode", "加载验证码失败!状态码:" + e.toString());
        }
    }

    public void login(String number, String password, String checkcode, String cookie) {
        String url = "http://222.24.19.201/default2.aspx";
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("__VIEWSTATE", "dDwtNTE2MjI4MTQ7Oz61IGQDPAm6cyppI+uTzQcI8sEH6Q==")
                .add("txtUserName", number)
                .add("TextBox2", password)
                .add("txtSecretCode", checkcode)
                .add("RadioButtonList1", "学生")
                .add("Button1", "")
                .add("lbLanguage", "")
                .add("hidPdrs", "")
                .add("hidsc", "");
        Request request = new Request.Builder().url(url).addHeader("Cookie", cookie)
                .addHeader("Referer", "http://222.24.19.201").post(builder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 302) {
                if (cookie != null) {
                    presenter.loginSuccess(cookie);
                    Log.e("login", "登录成功!");
                } else {
                    presenter.loginFailure("登录失败!cookie为空");
                    Log.e("login", "登录失败!cookie为空");
                }
            } else {
                presenter.loginFailure("登录失败!状态码:" + response.code());
                Log.e("login", "登录失败!状态码:" + response.code());
            }
        } catch (Exception e) {
            presenter.loginFailure("登录失败!捕获异常:" + e.toString());
            Log.e("login", "登录失败!捕获异常:" + e.toString());
        }
    }

    public void home(String number, String cookie) {
        String url = "http://222.24.19.201/xs_main.aspx?xh=" + number;
        StringBuilder content = new StringBuilder();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                presenter.homeSuccess(number, getNameFromContent(content.toString()));
                Log.e("home", "加载主页成功!");
            } else {
                presenter.homeFailure("加载主页失败!状态码:" + response.code());
                Log.e("home", "加载主页失败!状态码:" + response.code());
            }
        } catch (IOException e) {
            presenter.homeFailure("加载主页失败!捕获异常:" + e.toString());
            Log.e("home", "加载主页失败!捕获异常:" + e.toString());
        }
    }

    public String getNameFromContent(String content) {
        Document document = Jsoup.parse(content);
        Element element = document.getElementById("xhxm");
        String name = element.text().substring(0, element.text().indexOf('同'));
        return name;
    }
}
