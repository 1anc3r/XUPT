package me.lancer.xupt.mvp.logincard;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface ILoginCardPresenter {

    void loadCheckCodeSuccess(String cookie);

    void loadCheckCodeFailure(String log);

    void loginSuccess(String cookie);

    void loginFailure(String log);
}
