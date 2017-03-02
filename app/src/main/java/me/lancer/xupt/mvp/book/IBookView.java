package me.lancer.xupt.mvp.book;

import java.util.List;
import java.util.Map;

import me.lancer.xupt.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IBookView extends IBaseView {

    void search(List<BookBean> list);

    void reviewer(List<BookReviewer> list);

    void view(BookReviewer item);

    void rank(List<BookBean> list);

    void detail(Map<String, List<BookBean>> map);
}
