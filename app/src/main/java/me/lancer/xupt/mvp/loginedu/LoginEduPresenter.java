package me.lancer.xupt.mvp.loginedu;

import me.lancer.xupt.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class LoginEduPresenter implements IBasePresenter<ILoginEduView>, ILoginEduPresenter {

    private ILoginEduView view;
    private LoginEduModel model;

    public LoginEduPresenter(ILoginEduView view) {
        attachView(view);
        model = new LoginEduModel(this);
    }

    @Override
    public void attachView(ILoginEduView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadCheckCode() {
        if (view != null) {
            model.loadCheckCode();
        }
    }

    @Override
    public void loadCheckCodeSuccess(String cookie) {
        if (view != null) {
            view.showCheckCode(cookie);
        }
    }

    @Override
    public void loadCheckCodeFailure(String log) {
        if (view != null) {
            view.showMsg(log);
        }
    }

    public void login(String number, String password, String checkcode, String cookie) {
        if (view != null) {
            view.showLoad();
            model.login(number, password, checkcode, cookie);
        }
    }

    @Override
    public void loginSuccess(String cookie) {
        if (view != null) {
            view.login(cookie);
            view.hideLoad();
        }
    }

    @Override
    public void loginFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void home(String number, String cookie) {
        if (view != null) {
            model.home(number, cookie);
        }
    }

    @Override
    public void homeSuccess(String number, String name) {
        if (view != null) {
            view.home(number, name);
        }
    }

    @Override
    public void homeFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
        }
    }
}
