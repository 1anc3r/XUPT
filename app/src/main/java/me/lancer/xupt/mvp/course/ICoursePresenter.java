package me.lancer.xupt.mvp.course;

import java.util.List;

/**
 * Created by HuangFangzhi on 2016/12/13.
 */

public interface ICoursePresenter {

    void loadCourseSuccess(List<CourseBean> bean);

    void loadCourseFailure(String log);
}
