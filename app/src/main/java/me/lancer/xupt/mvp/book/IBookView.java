package me.lancer.xupt.mvp.book;

import java.util.List;

import me.lancer.xupt.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IBookView extends IBaseView {

    void search(List<BookBean> list);

    void rank(List<BookBean> list);
}
