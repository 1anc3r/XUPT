package me.lancer.xupt.mvp.book;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class BookModel {

    IBookPresenter presenter;

    public BookModel(IBookPresenter presenter) {
        this.presenter = presenter;
    }

    public void search(String keyword) {
        String url = "http://api.xiyoumobile.com/xiyoulibv2/book/search?keyword=" + keyword;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            List<BookBean> list;
            if (response.code() == 200) {
                list = getSearchFromContent(response.body().string());
                presenter.searchSuccess(list);
            } else {
                presenter.searchFailure("搜索失败!状态码:" + response.code());
                Log.e("search", "搜索失败!状态码:" + response.code());
            }
        } catch (Exception e) {
            presenter.searchFailure("搜索失败!捕获异常:" + e.toString());
            Log.e("search", "搜索失败!捕获异常:" + e.toString());
        }
    }

    public void rank(String type, String size) {
        String url = "http://api.xiyoumobile.com/xiyoulibv2/book/rank?type=" + type + "&size=" + size;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            List<BookBean> list;
            if (response.code() == 200) {
                list = getRankFromContent(response.body().string());
                presenter.rankSuccess(list);
            } else {
                presenter.rankFailure("获取排行失败!状态码:" + response.code());
                Log.e("rank", "获取排行失败!状态码:" + response.code());
            }
        } catch (Exception e) {
            presenter.rankFailure("获取排行失败!捕获异常:" + e.toString());
            Log.e("rank", "获取排行失败!捕获异常:" + e.toString());
        }
    }

    public void detail(int key, String value) {
        String url = "";
        if (key == 0) {
            url = "https://api.xiyoumobile.com/xiyoulibv2/book/detail/id/" + value;
        } else if (key == 1) {
            url = "https://api.xiyoumobile.com/xiyoulibv2/book/detail/barcode/" + value;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            Map<String, List<BookBean>> map;
            if (response.code() == 200) {
                map = getDetailFromContent(response.body().string());
                if (map != null) {
                    presenter.detailSuccess(map);
                } else {
                    presenter.detailFailure("获取详情失败!空指针");
                }
            } else {
                presenter.detailFailure("获取详情失败!状态码:" + response.code());
                Log.e("detail", "获取详情失败!状态码:" + response.code());
            }
        } catch (Exception e) {
            presenter.detailFailure("获取详情失败!捕获异常:" + e.toString());
            Log.e("detail", "获取详情失败!捕获异常:" + e.toString());
        }
    }

    public void addFavorite(String id, String cookie) {
        String url = "https://api.xiyoumobile.com/xiyoulibv2/user/addFav?id=" + id + "&session=" + cookie;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                JSONObject jbAdd = new JSONObject(response.body().string());
                if (jbAdd.getBoolean("Result")) {
                    String log = jbAdd.getString("Detail");
                    if (log.equals("ADDED_SUCCEED")) {
                        presenter.addFavoriteSuccess("收藏成功!");
                    } else if (log.equals("ALREADY_IN_FAVORITE")) {
                        presenter.addFavoriteSuccess("已经收藏!");
                    } else if (log.equals("ADDED_FAILED")) {
                        presenter.addFavoriteFailure("收藏失败!");
                    } else if (log.equals("USER_NOT_LOGIN")) {
                        presenter.addFavoriteFailure("收藏失败!用户未登录!");
                    } else if (log.equals("PARAM_ERROR")) {
                        presenter.addFavoriteFailure("收藏失败!参数错误,缺少参数!");
                    } else {
                        presenter.delFavoriteFailure("收藏失败!未知错误");
                    }
                } else {
                    presenter.addFavoriteFailure("收藏失败!");
                }
            } else {
                presenter.addFavoriteFailure("收藏失败!状态码:" + response.code());
                Log.e("addFavorite", "收藏失败!状态码:" + response.code());
            }
        } catch (Exception e) {
            presenter.addFavoriteFailure("收藏情况失败!捕获异常:" + e.toString());
            Log.e("addFavorite", "收藏失败!捕获异常:" + e.toString());
        }
    }

    public void delFavorite(String id, String number, String cookie) {
        String url = "https://api.xiyoumobile.com/xiyoulibv2/user/delFav?id=" + id + "&username=S" + number + "&session=" + cookie;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                JSONObject jbAdd = new JSONObject(response.body().string());
                if (jbAdd.getBoolean("Result")) {
                    String log = jbAdd.getString("Detail");
                    if (log.equals("DELETED_SUCCEED")) {
                        presenter.delFavoriteSuccess("取关成功!");
                    } else if (log.equals("DELETED_FAILED")) {
                        presenter.delFavoriteSuccess("取关失败!");
                    } else if (log.equals("USER_NOT_LOGIN")) {
                        presenter.delFavoriteFailure("取关失败!用户未登录!");
                    } else if (log.equals("PARAM_ERROR")) {
                        presenter.delFavoriteFailure("取关失败!参数错误,缺少参数!");
                    } else {
                        presenter.delFavoriteFailure("取关失败!未知错误");
                    }
                } else {
                    presenter.addFavoriteFailure("取关失败!");
                    Log.e("delFavorite", "取关失败!");
                }
            } else {
                presenter.delFavoriteFailure("取关失败!状态码:" + response.code());
                Log.e("delFavorite", "取关失败!状态码:" + response.code());
            }
        } catch (Exception e) {
            presenter.delFavoriteFailure("取关情况失败!捕获异常:" + e.toString());
            Log.e("delFavorite", "取关失败!捕获异常:" + e.toString());
        }
    }

    private List<BookBean> getSearchFromContent(String content) {
        try {
            JSONObject jbBook = new JSONObject(content);
            if (jbBook.getBoolean("Result")) {
                JSONObject jbDetail = jbBook.getJSONObject("Detail");
                JSONArray jaBook = jbDetail.getJSONArray("BookData");
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
                        bbItem.setBookISBN(jbItem.getString("ISBN"));
                        bbItem.setBookSort(jbItem.getString("Sort"));
                        bbItem.setBookTotal(jbItem.getString("Total"));
                        bbItem.setBookAvailable(jbItem.getString("Available"));
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

    private List<BookBean> getRankFromContent(String content) {
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
                        bbItem.setBookSort(jbItem.getString("Sort"));
                        bbItem.setBookBorNum(jbItem.getString("BorNum"));
                        bbItem.setBookRank(jbItem.getString("Rank"));
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

    private Map<String, List<BookBean>> getDetailFromContent(String content) {
        try {
            Map<String, List<BookBean>> map = new HashMap<>();
            List<BookBean> detail = new ArrayList<>();
            List<BookBean> circle = new ArrayList<>();
            List<BookBean> refer = new ArrayList<>();
            JSONObject jbBook = new JSONObject(content);
            if (jbBook.getBoolean("Result")) {
                JSONObject jbDeteil = jbBook.getJSONObject("Detail");
                BookBean bean = new BookBean();
                bean.setBookId(jbDeteil.getString("ID"));
                bean.setBookISBN(jbDeteil.getString("ISBN"));
                bean.setBookMainTitle(jbDeteil.getString("Title"));
                bean.setBookSubTitle(jbDeteil.getString("SecondTitle"));
                bean.setBookAuthor(jbDeteil.getString("Author"));
                bean.setBookPublish(jbDeteil.getString("Pub"));
                bean.setBookSort(jbDeteil.getString("Sort"));
                bean.setBookSubject(jbDeteil.getString("Subject"));
                bean.setBookTotal(jbDeteil.getString("Total"));
                bean.setBookAvailable(jbDeteil.getString("Avaliable"));
                if (jbDeteil.getJSONObject("DoubanInfo") != null) {
                    bean.setBookImage(jbDeteil.getJSONObject("DoubanInfo").getJSONObject("Images").getString("medium"));
                }
                detail.add(bean);
                map.put("detail", detail);
                JSONArray jaCircle = jbDeteil.getJSONArray("CirculationInfo");
                if (jaCircle.length() != 0) {
                    for (int i = 0; i < jaCircle.length(); i++) {
                        BookBean bbItem = new BookBean();
                        JSONObject jbItem = jaCircle.getJSONObject(i);
                        bbItem.setBookBarCode(jbItem.getString("Barcode"));
                        bbItem.setBookSort(jbItem.getString("Sort"));
                        bbItem.setBookDepartment(jbItem.getString("Department"));
                        bbItem.setBookState(jbItem.getString("Status"));
                        bbItem.setBookDate(jbItem.getString("Date"));
                        circle.add(bbItem);
                    }
                    map.put("circle", circle);
                }
                JSONArray jaRefer = jbDeteil.getJSONArray("ReferBooks");
                if (jaRefer.length() != 0) {
                    for (int i = 0; i < jaRefer.length(); i++) {
                        BookBean bbItem = new BookBean();
                        JSONObject jbItem = jaRefer.getJSONObject(i);
                        bbItem.setBookId(jbItem.getString("ID"));
                        bbItem.setBookMainTitle(jbItem.getString("Title"));
                        bbItem.setBookAuthor(jbItem.getString("Author"));
                        refer.add(bbItem);
                    }
                    map.put("refer", refer);
                }
                return map;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
