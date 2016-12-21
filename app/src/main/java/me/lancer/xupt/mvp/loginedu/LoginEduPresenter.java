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
        model.loadCheckCode();
    }

    @Override
    public void loadCheckCodeSuccess(String cookie) {
        view.showCheckCode(cookie);
    }

    @Override
    public void loadCheckCodeFailure(String log) {
        view.showMsg(log);
    }

    public void login(String number, String password, String checkcode, String cookie) {
        view.showLoad();
        model.login(number, password, checkcode, cookie);
    }

    @Override
    public void loginSuccess(String cookie) {
        view.login(cookie);
        view.hideLoad();
    }

    @Override
    public void loginFailure(String log) {
        if (log != null && log.length() > 0) {
            view.showMsg(log);
        }
        view.hideLoad();
    }

    public void home(String number, String cookie) {
        model.home(number, cookie);
    }

    @Override
    public void homeSuccess(String number, String name) {
        view.home(number, name);
    }

    @Override
    public void homeFailure(String log) {
        if (log != null && log.length() > 0) {
            view.showMsg(log);
        }
    }
}
