package me.lancer.xupt.mvp.score;

import java.util.List;

import me.lancer.xupt.mvp.base.IBaseView;
import me.lancer.xupt.mvp.user.UserBean;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface IScoreView extends IBaseView {

    void showScore(List<ScoreBean> list);
}
