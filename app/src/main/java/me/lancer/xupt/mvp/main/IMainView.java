package me.lancer.xupt.mvp.main;

import me.lancer.xupt.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IMainView extends IBaseView {

    void showCheckCode(String cookie);

    void login(String cookie);

    void home(String number, String name);
}
