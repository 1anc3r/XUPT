package me.lancer.xupt.mvp.book;

import java.util.List;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IBookPresenter {

    void searchSuccess(List<BookBean> list);

    void searchFailure(String log);

    void rankSuccess(List<BookBean> list);

    void rankFailure(String log);
}
