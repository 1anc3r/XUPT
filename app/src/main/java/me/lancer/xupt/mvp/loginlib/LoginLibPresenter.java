package me.lancer.xupt.mvp.loginlib;

import java.util.List;

import me.lancer.xupt.mvp.base.IBasePresenter;
import me.lancer.xupt.mvp.book.BookBean;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public class LoginLibPresenter implements IBasePresenter<ILoginLibView>, ILoginLibPresenter {

    private ILoginLibView view;
    private LoginLibModel model;

    public LoginLibPresenter(ILoginLibView view) {
        attachView(view);
        model = new LoginLibModel(this);
    }

    @Override
    public void attachView(ILoginLibView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void login(String number, String password) {
        if (view != null) {
            view.showLoad();
            model.login(number, password);
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

    public void getDebt(String cookie) {
        if (view != null) {
//        view.showLoad();
            model.getDebt(cookie);
        }
    }

    @Override
    public void getDebtSuccess(String debt) {
        if (view != null) {
            view.showDebt(debt);
//        view.hideLoad();
        }
    }

    @Override
    public void getDebtFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
//        view.hideLoad();
        }
    }

    public void getCurrent(String cookie) {
        if (view != null) {
//        view.showLoad();
            model.getCurrent(cookie);
        }
    }

    @Override
    public void getCurrentSuccess(List<BookBean> list) {
        if (view != null) {
            view.showCurrent(list);
//        view.hideLoad();
        }
    }

    @Override
    public void getCurrentFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
//        view.hideLoad();
        }
    }

    public void getHistory(String cookie) {
        if (view != null) {
//        view.showLoad();
            model.getHistory(cookie);
        }
    }

    @Override
    public void getHistorySuccess(List<BookBean> list) {
        if (view != null) {
            view.showHistory(list);
//        view.hideLoad();
        }
    }

    @Override
    public void getHistoryFailure(String log) {
        if (log != null && log.length() > 0) {
            view.showMsg(log);
//        view.hideLoad();
        }
    }

    public void getFavorite(String cookie) {
        if (view != null) {
//        view.showLoad();
            model.getFavorite(cookie);
        }
    }

    @Override
    public void getFavoriteSuccess(List<BookBean> list) {
        if (view != null) {
            view.showFavorite(list);
//        view.hideLoad();
        }
    }

    @Override
    public void getFavoriteFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
//        view.hideLoad();
        }
    }
}
