package me.lancer.xupt.mvp.logincard;

import android.os.Environment;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class LoginCardModel {

    ILoginCardPresenter presenter;
    HttpClient client = new DefaultHttpClient();
    String cookie;

    public LoginCardModel(ILoginCardPresenter presenter) {
        this.presenter = presenter;
    }

    public void loadCheckCode() {
        try {
            Date date = new Date();
            String url = "http://jwkq.xupt.edu.cn:8080/Common/GetValidateCode?time=" + date.getTime();
            String path = Environment.getExternalStorageDirectory().toString();
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                Header[] header = response.getHeaders("Set-Cookie");
                File dir = new File(path + "/me.lancer.xupt");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir.getPath() + "/CheckCode.png");
                if (file.exists()) {
                    file.delete();
                }
                OutputStream os = new FileOutputStream(file);
                byte[] b = EntityUtils.toByteArray(response.getEntity());
                os.write(b);
                os.close();
                if (header.length != 0) {
                    String rawCookie = header[0].getValue().toString();
                    cookie = rawCookie.substring(0, rawCookie.length() - 18);
                    presenter.loadCheckCodeSuccess(cookie);
                    Log.e("loadCheckCode", "加载验证码成功!");
                }
            } else {
                presenter.loadCheckCodeFailure("加载验证码失败!状态码:" + response.getStatusLine().getStatusCode());
                Log.e("loadCheckCode", "加载验证码失败!状态码:" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            presenter.loadCheckCodeFailure("加载验证码失败!捕获异常:" + e.toString());
            Log.e("loadCheckCode", "加载验证码失败!捕获异常:" + e.toString());
        }
    }

    public void login(String number, String password, String checkcode, String cookie) {
        try {
            HttpPost post = new HttpPost("http://jwkq.xupt.edu.cn:8080/Account/Login");
            JSONObject jbLogin = new JSONObject();
            jbLogin.put("UserName", number);
            jbLogin.put("UserPassword", password);
            jbLogin.put("ValiCode", checkcode);
            post.setEntity(new StringEntity(jbLogin.toString()));
            post.addHeader("Set-Cookie", cookie);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                Header[] header = response.getHeaders("Set-Cookie");
                if (header.length != 0) {
                    String rawCookie = header[0].getValue().toString();
                    cookie = rawCookie.substring(0, rawCookie.length() - 18);
                    HttpEntity entity = response.getEntity();
                    String content = EntityUtils.toString(entity, "utf-8").toString();
                    String log = isSucceedFromContent(content);
                    if (log.equals("登录成功!")) {
                        presenter.loginSuccess(cookie);
                        Log.e("login", "登录成功!");
                    } else {
                        presenter.loginFailure(log);
                    }
                }
            } else {
                presenter.loginFailure("登录失败!状态码:" + response.getStatusLine().getStatusCode());
                Log.e("login", "登录失败!状态码:" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            presenter.loginFailure("登录失败!捕获异常:" + e.toString());
            Log.e("login", "登录失败!捕获异常:" + e.toString());
        }
    }

    private String isSucceedFromContent(String content) {
        try {
            JSONObject jbSucceed = new JSONObject(content);
            if (jbSucceed.getBoolean("IsSucceed")) {
                return "登录成功!";
            } else if (jbSucceed.getInt("Obj") == 1003) {
                return "验证码错误!";
            } else if (jbSucceed.getInt("Obj") == 1000) {
                return "用户名或密码错误!";
            } else {
                return "登录失败!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
