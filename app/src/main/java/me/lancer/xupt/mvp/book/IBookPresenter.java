package me.lancer.xupt.mvp.book;

import java.util.List;
import java.util.Map;

import me.lancer.xupt.mvp.reviewer.bean.ReviewerBean;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IBookPresenter {

    void searchSuccess(List<BookBean> list);

    void searchFailure(String log);

    void reviewerSuccess(List<ReviewerBean> list);

    void reviewerFailure(String log);

    void viewSuccess(ReviewerBean item);

    void viewFailure(String log);

    void rankSuccess(List<BookBean> list);

    void rankFailure(String log);

    void detailSuccess(Map<String, List<BookBean>> map);

    void detailFailure(String log);

    void addFavoriteSuccess(String log);

    void addFavoriteFailure(String log);

    void delFavoriteSuccess(String log);

    void delFavoriteFailure(String log);
}
