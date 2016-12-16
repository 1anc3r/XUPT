package me.lancer.xupt.mvp.user;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IUserPresenter {

    void loadUserSuccess(UserBean bean);

    void loadUserFailure(String log);
}
