package me.lancer.xupt.mvp.rollcall;

import java.util.List;

import me.lancer.xupt.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2016/12/14.
 */

public class RollCallPresenter implements IBasePresenter<IRollCallView>, IRollCallPresenter {

    private IRollCallView view;
    private RollCallModel model;

    public RollCallPresenter(IRollCallView view) {
        attachView(view);
        model = new RollCallModel(this);
    }

    @Override
    public void attachView(IRollCallView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void getDetail(String start, String end, String status, String flag, String page, String cookie0, String cookie1) {
        if (view != null) {
            view.showLoad();
            model.getDetail(start, end, status, flag, page, cookie0, cookie1);
        }
    }

    @Override
    public void getDetailSuccess(List<RollCallBean> list) {
        if (view != null) {
            view.showDetail(list);
            view.hideLoad();
        }
    }

    @Override
    public void getDetailFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void getStatistic(String cookie0, String cookie1) {
        if (view != null) {
            view.showLoad();
            model.getStatistic(cookie0, cookie1);
        }
    }

    @Override
    public void getStatisticSuccess(List<RollCallBean> list) {
        if (view != null) {
            view.showStatistic(list);
            view.hideLoad();
        }
    }

    @Override
    public void getStatisticFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }

    public void appeal(RollCallBean rollcall, String remark, String state, String cookie0, String cookie1) {
        if (view != null) {
            view.showLoad();
            model.appeal(rollcall, remark, state, cookie0, cookie1);
        }
    }

    @Override
    public void appealSuccess(String log) {
        if (view != null) {
            view.appeal(log);
            view.hideLoad();
        }
    }

    @Override
    public void appealFailure(String log) {
        if (log != null && log.length() > 0 && view != null) {
            view.showMsg(log);
            view.hideLoad();
        }
    }
}
