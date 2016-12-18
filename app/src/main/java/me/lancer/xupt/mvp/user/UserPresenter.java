package me.lancer.xupt.mvp.user;

import android.util.Log;

import me.lancer.xupt.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class UserPresenter implements IBasePresenter<IUserView>, IUserPresenter {

    private IUserView view;
    private UserModel model;

    public UserPresenter(IUserView view) {
        attachView(view);
        model = new UserModel(this);
    }

    @Override
    public void attachView(IUserView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadUser(String number, String name, String cookie, boolean refresh) {
        view.showLoad();
        model.loadUser(number, name, cookie, refresh);
    }

    @Override
    public void loadUserSuccess(UserBean bean) {
        view.showUser(bean);
        view.hideLoad();
    }

    @Override
    public void loadUserFailure(String log) {
        view.showMsg(log);
        view.hideLoad();
    }
}
