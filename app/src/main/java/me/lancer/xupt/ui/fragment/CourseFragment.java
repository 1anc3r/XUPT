package me.lancer.xupt.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.PresenterFragment;
import me.lancer.xupt.mvp.course.CourseBean;
import me.lancer.xupt.mvp.course.CoursePresenter;
import me.lancer.xupt.mvp.course.ICourseView;
import me.lancer.xupt.ui.application.ApplicationInstance;
import me.lancer.xupt.ui.view.ScheduleView;

public class CourseFragment extends PresenterFragment<CoursePresenter> implements ICourseView {

    private ApplicationInstance app;

    private ScheduleView svCourse;
    ProgressDialog pdLogin;

    List<CourseBean> courseList;

    String number, name, cookie;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pdLogin.dismiss();
                    break;
                case 1:
                    pdLogin.show();
                    break;
                case 2:
                    Log.e(getString(R.string.log), (String) msg.obj);
                    break;
                case 3:
                    if (msg.obj != null) {
                        app.setCourse(false);
                        courseList = (List<CourseBean>) msg.obj;
                        svCourse.updateSchedule(courseList);
                    }
                    break;
            }
        }
    };

    Runnable loadCourse = new Runnable() {
        @Override
        public void run() {
            presenter.loadCourse(number, name, cookie, app.isCourse());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        svCourse = (ScheduleView) view.findViewById(R.id.sv_course);
        pdLogin = new ProgressDialog(getActivity());
        pdLogin.setMessage(getString(R.string.course_loading));
        pdLogin.setCancelable(false);
    }

    private void initData() {
        app = (ApplicationInstance) getActivity().getApplication();
        number = app.getNumber();
        name = app.getName();
        cookie = app.getEduCookie();
        new Thread(loadCourse).start();
    }

    @Override
    protected CoursePresenter onCreatePresenter() {
        return new CoursePresenter(CourseFragment.this);
    }

    @Override
    public void showCourse(List<CourseBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showMsg(String log) {
        Message msg = new Message();
        msg.what = 2;
        msg.obj = log;
        handler.sendMessage(msg);
    }

    @Override
    public void showLoad() {
        Message msg = new Message();
        msg.what = 1;
        handler.sendMessage(msg);
    }

    @Override
    public void hideLoad() {
        Message msg = new Message();
        msg.what = 0;
        handler.sendMessage(msg);
    }
}

