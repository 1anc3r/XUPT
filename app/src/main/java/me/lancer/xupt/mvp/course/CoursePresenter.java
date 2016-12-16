package me.lancer.xupt.mvp.course;

import java.util.List;

import me.lancer.xupt.mvp.base.IBasePresenter;

/**
 * Created by HuangFangzhi on 2016/12/14.
 */

public class CoursePresenter implements IBasePresenter<ICourseView>, ICoursePresenter {

    private ICourseView view;
    private CourseModel model;

    public CoursePresenter(ICourseView view) {
        attachView(view);
        model = new CourseModel(this);
    }

    @Override
    public void attachView(ICourseView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadCourse(String number, String name, String cookie, boolean refresh) {
        view.showLoad();
        model.loadCourse(number, name, cookie, refresh);
    }

    @Override
    public void loadCourseSuccess(List<CourseBean> list) {
        view.showCourse(list);
        view.hideLoad();
    }

    @Override
    public void loadCourseFailure(String log) {
        view.showMsg(log);
        view.hideLoad();
    }
}
