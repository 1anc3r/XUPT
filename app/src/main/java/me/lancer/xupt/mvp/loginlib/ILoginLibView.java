package me.lancer.xupt.mvp.loginlib;

import java.util.List;

import me.lancer.xupt.mvp.base.IBaseView;
import me.lancer.xupt.mvp.book.BookBean;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface ILoginLibView extends IBaseView {

    void login(String cookie);

    void showDebt(String debt);

    void showCurrent(List<BookBean> list);

    void showHistory(List<BookBean> list);

    void showFavorite(List<BookBean> list);
}
