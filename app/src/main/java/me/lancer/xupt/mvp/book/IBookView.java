package me.lancer.xupt.mvp.book;

import java.util.List;
import java.util.Map;

import me.lancer.xupt.mvp.base.IBaseView;
import me.lancer.xupt.mvp.reviewer.bean.ReviewerBean;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IBookView extends IBaseView {

    void search(List<BookBean> list);

    void reviewer(List<ReviewerBean> list);

    void view(ReviewerBean item);

    void rank(List<BookBean> list);

    void detail(Map<String, List<BookBean>> map);
}
