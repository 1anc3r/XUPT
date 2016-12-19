package me.lancer.xupt.mvp.loginedu;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface ILoginEduPresenter {

    void loadCheckCodeSuccess(String cookie);

    void loadCheckCodeFailure(String log);

    void loginSuccess(String cookie);

    void loginFailure(String log);

    void homeSuccess(String number, String name);

    void homeFailure(String log);
}
