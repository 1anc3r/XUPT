package me.lancer.xupt.mvp.logincard;

import me.lancer.xupt.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class LoginCardPresenter implements IBasePresenter<ILoginCardView>, ILoginCardPresenter {

    private ILoginCardView view;
    private LoginCardModel model;

    public LoginCardPresenter(ILoginCardView view) {
        attachView(view);
        model = new LoginCardModel(this);
    }

    @Override
    public void attachView(ILoginCardView view) {
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
}
