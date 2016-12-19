package me.lancer.xupt.mvp.book;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                if (list != null) {
                    presenter.searchSuccess(list);
                } else {
                    presenter.searchSuccess(null);
                }
            } else {
                presenter.searchFailure("!error!----status code:" + response.code());
                Log.e("search", "!error!----status code:" + response.code());
            }
        } catch (Exception e) {
            presenter.searchFailure("!error!----exception:" + e.toString());
            Log.e("search", "!error!----exception:" + e.toString());
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
                if (list != null) {
                    presenter.rankSuccess(list);
                } else {
                    presenter.rankSuccess(null);
                }
            } else {
                presenter.rankFailure("!error!----status code:" + response.code());
                Log.e("rank", "!error!----status code:" + response.code());
            }
        } catch (Exception e) {
            presenter.rankFailure("!error!----exception:" + e.toString());
            Log.e("rank", "!error!----exception:" + e.toString());
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
                        bbItem.setBookAvaliable(jbItem.getString("Available"));
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
}
