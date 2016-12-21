package me.lancer.xupt.mvp.logincard;

import me.lancer.xupt.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface ILoginCardView extends IBaseView {

    void showCheckCode(String cookie);

    void login(String cookie);
}
