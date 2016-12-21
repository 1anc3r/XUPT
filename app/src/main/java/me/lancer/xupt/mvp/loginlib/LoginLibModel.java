package me.lancer.xupt.mvp.loginlib;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.lancer.xupt.mvp.book.BookBean;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class LoginLibModel {

    ILoginLibPresenter presenter;

    public LoginLibModel(ILoginLibPresenter presenter) {
        this.presenter = presenter;
    }

    public void login(String number, String password) {
        String url = "http://api.xiyoumobile.com/xiyoulibv2/user/login?username=S" + number + "&password="+password;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                String content =  response.body().string();
                Boolean result = new JSONObject(content).getBoolean("Result");
                String cookie = new JSONObject(content).getString("Detail");
                if (result) {
                    if (cookie != null) {
                        presenter.loginSuccess(cookie);
                        Log.e("loginSuccess", "登录成功!");
                    }
                }else{
                    presenter.loginFailure("登录失败!错误信息:" + cookie);
                    Log.e("login", "登录失败!错误信息:" + cookie);
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

    public void getDebt(String cookie) {
        String url = "https://api.xiyoumobile.com/xiyoulibv2/user/info?session=" + cookie;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            String debt;
            if (response.code() == 200) {
                String content = response.body().string();
                debt = getDebtFromContent(content);
                if (debt != null) {
                    presenter.getDebtSuccess(debt);
                } else {
                    presenter.getDebtSuccess("0");
                }
            } else {
                presenter.getDebtFailure("获取欠费情况失败!状态码:" + response.code());
                Log.e("getDebt", "获取欠费情况失败!状态码:" + response.code());
            }
        } catch (Exception e) {
            presenter.getDebtFailure("获取欠费情况失败!捕获异常:" + e.toString());
            Log.e("getDebt", "获取欠费情况失败!捕获异常:" + e.toString());
        }
    }

    public void getCurrent(String cookie) {
        String url = "https://api.xiyoumobile.com/xiyoulibv2/user/rent?session=" + cookie;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            List<BookBean> list;
            if (response.code() == 200) {
                list = getCurrentFromContent(response.body().string());
                if (list != null) {
                    presenter.getCurrentSuccess(list);
                } else {
                    presenter.getCurrentSuccess(null);
                }
            } else {
                presenter.getCurrentFailure("获取当前借阅失败!状态码:" + response.code());
                Log.e("getrCurrent", "获取当前借阅失败!状态码:" + response.code());
            }
        } catch (Exception e) {
            presenter.getCurrentFailure("获取当前借阅失败!捕获异常:" + e.toString());
            Log.e("getrCurrent", "获取当前借阅失败!捕获异常:" + e.toString());
        }
    }

    public void getHistory(String cookie) {
        String url = "https://api.xiyoumobile.com/xiyoulibv2/user/history?session=" + cookie;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            List<BookBean> list;
            if (response.code() == 200) {
                list = getHistoryFromContent(response.body().string());
                if (list != null) {
                    presenter.getHistorySuccess(list);
                } else {
                    presenter.getHistorySuccess(null);
                }
            } else {
                presenter.getHistoryFailure("获取历史借阅失败!状态码:" + response.code());
                Log.e("getHistory", "获取历史借阅失败!状态码:" + response.code());
            }
        } catch (Exception e) {
            presenter.getHistoryFailure("获取历史借阅失败!捕获异常:" + e.toString());
            Log.e("getHistory", "获取历史借阅失败!捕获异常:" + e.toString());
        }
    }

    public void getFavorite(String cookie) {
        String url = "https://api.xiyoumobile.com/xiyoulibv2/user/favorite?session=" + cookie;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            List<BookBean> list;
            if (response.code() == 200) {
                list = getFavoriteFromContent(response.body().string());
                if (list != null) {
                    presenter.getFavoriteSuccess(list);
                } else {
                    presenter.getFavoriteSuccess(null);
                }
            } else {
                presenter.getFavoriteFailure("获取收藏关注失败!状态码:" + response.code());
                Log.e("getFavorite", "获取收藏关注失败!状态码:" + response.code());
            }
        } catch (Exception e) {
            presenter.getFavoriteFailure("获取收藏关注失败!捕获异常:" + e.toString());
            Log.e("getFavorite", "获取收藏关注失败!捕获异常:" + e.toString());
        }
    }

    private String getDebtFromContent(String content){
        try {
            JSONObject jbUser = new JSONObject(content);
            if (jbUser.getBoolean("Result")) {
                JSONObject jbDetail = jbUser.getJSONObject("Detail");
                String debt = jbDetail.getString("Debt");
                return debt;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BookBean> getCurrentFromContent(String content){
        try {
            JSONObject jbBook = new JSONObject(content);
            if (jbBook.getBoolean("Result")) {
                JSONArray jaBook = jbBook.getJSONArray("Detail");
                List<BookBean> list = new ArrayList<>();
                if (jaBook.length() == 0) {
                    return null;
                } else {
                    for (int i = 0; i < jaBook.length(); i++) {
                        BookBean bbItem = new BookBean();
                        JSONObject jbItem = jaBook.getJSONObject(i);
                        bbItem.setBookBarCode(jbItem.getString("Barcode"));
                        bbItem.setBookMainTitle(jbItem.getString("Title"));
                        bbItem.setBookDepartment(jbItem.getString("Department"));
                        bbItem.setBookState(jbItem.getString("State"));
                        bbItem.setBookDate(jbItem.getString("Date"));
                        bbItem.setBookCanRenew(jbItem.getBoolean("CanRenew"));
                        bbItem.setBookDepartmentId(jbItem.getString("Department_id"));
                        bbItem.setBookLibraryId(jbItem.getString("Library_id"));
                        list.add(bbItem);
                    }
                    return list;
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BookBean> getHistoryFromContent(String content){
        try {
            JSONObject jbBook = new JSONObject(content);
            if (jbBook.getBoolean("Result")) {
                JSONArray jaBook = jbBook.getJSONArray("Detail");
                List<BookBean> list = new ArrayList<>();
                if (jaBook.length() == 0) {
                    return null;
                } else {
                    for (int i = 0; i < jaBook.length(); i++) {
                        BookBean bbItem = new BookBean();
                        JSONObject jbItem = jaBook.getJSONObject(i);
                        bbItem.setBookBarCode(jbItem.getString("Barcode"));
                        bbItem.setBookMainTitle(jbItem.getString("Title"));
                        bbItem.setBookState(jbItem.getString("Type"));
                        bbItem.setBookDate(jbItem.getString("Date"));
                        list.add(bbItem);
                    }
                    return list;
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BookBean> getFavoriteFromContent(String content){
        try {
            JSONObject jbBook = new JSONObject(content);
            if (jbBook.getBoolean("Result")) {
                JSONArray jaBook = jbBook.getJSONArray("Detail");
                List<BookBean> list = new ArrayList<>();
                if (jaBook.length() == 0) {
                    return null;
                } else {
                    for (int i = 0; i < jaBook.length(); i++) {
                        BookBean bbItem = new BookBean();
                        JSONObject jbItem = jaBook.getJSONObject(i);
                        bbItem.setBookId(jbItem.getString("ID"));
                        bbItem.setBookMainTitle(jbItem.getString("Title"));
                        bbItem.setBookAuthor(jbItem.getString("Author"));
                        bbItem.setBookPublish(jbItem.getString("Pub"));
                        bbItem.setBookSort(jbItem.getString("Sort"));
                        bbItem.setBookISBN(jbItem.getString("ISBN"));
                        list.add(bbItem);
                    }
                    return list;
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
