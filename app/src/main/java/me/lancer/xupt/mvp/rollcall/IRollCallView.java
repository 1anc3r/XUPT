package me.lancer.xupt.mvp.rollcall;

import java.util.List;

import me.lancer.xupt.mvp.base.IBaseView;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IRollCallView extends IBaseView {

    void showDetail(List<RollCallBean> list);

    void showStatistic(List<RollCallBean> list);

    void appeal(String log);
}
