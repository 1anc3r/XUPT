package me.lancer.xupt.mvp.rollcall;

import java.util.List;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IRollCallPresenter {

    void getDetailSuccess(List<RollCallBean> list);

    void getDetailFailure(String log);

    void getStatisticSuccess(List<RollCallBean> list);

    void getStatisticFailure(String log);

    void appealSuccess(String log);

    void appealFailure(String log);
}
