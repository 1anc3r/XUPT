package me.lancer.xupt.mvp.loginlib;

import java.util.List;

import me.lancer.xupt.mvp.book.BookBean;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface ILoginLibPresenter {

    void loginSuccess(String cookie);

    void loginFailure(String log);

    void getDebtSuccess(String debt);

    void getDebtFailure(String log);

    void getCurrentSuccess(List<BookBean> list);

    void getCurrentFailure(String log);

    void getHistorySuccess(List<BookBean> list);

    void getHistoryFailure(String log);

    void getFavoriteSuccess(List<BookBean> list);

    void getFavoriteFailure(String log);
}
