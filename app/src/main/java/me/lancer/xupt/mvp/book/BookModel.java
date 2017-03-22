package me.lancer.xupt.mvp.book;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.lancer.xupt.mvp.reviewer.bean.ReviewerBean;

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

    public void reviewer(int type, int pager) {
        String url = "";
        switch (type) {
            case 1:
                url = "https://book.douban.com/review/best/?start=" + pager;
                break;//最受欢迎书评
            case 2:
                url = "https://book.douban.com/review/latest/?start=" + pager;
                break;//最新书评
            case 3:
                url = "https://movie.douban.com/review/best/?start=" + pager;
                break;//最受欢迎影评
            case 4:
                url = "https://movie.douban.com/review/latest/?start=" + pager;
                break;//最新影评
            case 5:
                url = "https://music.douban.com/review/pop/?start=" + pager;
                break;//流行音乐
            case 6:
                url = "https://music.douban.com/review/latest/?start=" + pager;
                break;//最新乐评
            default:
                url = "https://book.douban.com/review/best/?start=" + pager;
                break;//最受欢迎书评
        }
        List<ReviewerBean> list;
        String content;
        try {
            if (!(content = getContentFromHtml(url)).contains("失败!")) {
                list = getReviewerFromContent(content);
                presenter.reviewerSuccess(list);
                Log.e("reviewer", "获取书评成功!");
            } else {
                presenter.reviewerFailure(content);
                Log.e("reviewer", "获取书评失败!");
            }
        } catch (Exception e) {
            presenter.reviewerFailure("获取书评失败!捕获异常:" + e.toString());
            Log.e("reviewer", "获取书评失败!");
        }
    }

    public void view(String url) {
        ReviewerBean item;
        String content;
        try {
            if (!(content = getContentFromHtml(url)).contains("失败!")) {
                item = getViewFromContent(content);
                presenter.viewSuccess(item);
                Log.e("reviewer", "获取书评成功!");
            } else {
                presenter.viewFailure(content);
                Log.e("reviewer", "获取书评失败!");
            }
        } catch (Exception e) {
            presenter.viewFailure("获取书评失败!捕获异常:" + e.toString());
            Log.e("reviewer", "获取书评失败!");
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

    private String getContentFromHtml(String url) {
        StringBuilder content = new StringBuilder();
        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                BufferedReader reader = new BufferedReader(response.body().charStream());
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                Log.e("reviewe.fromHtml", "加载成功!");
                return content.toString();
            } else {
                Log.e("reviewe.fromHtml", "加载失败!状态码:" + response.code());
                return "加载失败!状态码:" + response.code();
            }
        } catch (IOException e) {
            Log.e("reviewe.fromHtml", "加载失败!捕获异常:" + e.toString());
            return "加载失败!捕获异常:" + e.toString();
        }
    }

    private List<ReviewerBean> getReviewerFromContent(String content) {
        List<ReviewerBean> revieweList = new ArrayList<>();
        Document document = Jsoup.parse(content);
        Element element = document.getElementById("content");
        Elements elements = element.getElementsByClass("main review-item");
        for (int i = 0; i < elements.size(); i++) {
            ReviewerBean brItem = new ReviewerBean();
            brItem.setImg(elements.get(i).getElementsByTag("img").attr("src"));
            brItem.setHref(elements.get(i).getElementsByClass("title-link").attr("href"));
            brItem.setTitle(elements.get(i).getElementsByClass("title-link").text());
            brItem.setContent(elements.get(i).getElementsByClass("short-content").text());
            revieweList.add(brItem);
        }
        return revieweList;
    }

    private ReviewerBean getViewFromContent(String content) {
        ReviewerBean brItem = new ReviewerBean();
        Document document = Jsoup.parse(content);
        Element element = document.getElementById("content");
        Log.e("element", element.text());
        brItem.setRating(element.getElementsByClass("main-title-hide").text());
        brItem.setTime(element.getElementsByClass("main-meta").text());
        brItem.setDetail(element.getElementsByClass("review-content clearfix").text());
        return brItem;
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
