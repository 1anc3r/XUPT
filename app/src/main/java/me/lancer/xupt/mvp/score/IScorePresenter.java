package me.lancer.xupt.mvp.score;

import java.util.List;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IScorePresenter {

    void loadScoreSuccess(List<ScoreBean> list);

    void loadScoreFailure(String log);
}
