package me.lancer.xupt.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.user.IUserView;
import me.lancer.xupt.mvp.user.UserBean;
import me.lancer.xupt.mvp.user.UserPresenter;
import me.lancer.xupt.ui.application.ApplicationInstance;
import me.lancer.xupt.ui.view.CircleImageView;

public class UserEduFragment extends PresenterFragment<UserPresenter> implements IUserView {

    ApplicationInstance app = new ApplicationInstance();

    LinearLayout llUser;
    CircleImageView civHead;
    TextView tvHead, tvNumber, tvClass, tvMajor, tvCollege, tvSchool;
    ProgressDialog pdLogin;

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
                    Log.e("log", (String) msg.obj);
                    break;
                case 3:
                    if (msg.obj != null) {
                        app.setUser(false);
                        UserBean bean = (UserBean) msg.obj;
                        tvHead.setText(bean.getUserName());
                        tvNumber.setText(bean.getUserNumber());
                        tvClass.setText(bean.getUserClass());
                        tvMajor.setText(bean.getUserMajor());
                        tvCollege.setText(bean.getUserCollege());
                        tvSchool.setText("西安邮电大学");
                    }
                    break;
            }
        }
    };

    Runnable loadUser = new Runnable() {
        @Override
        public void run() {
            presenter.loadUser(number, name, cookie, app.isUser());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_edu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        llUser = (LinearLayout) view.findViewById(R.id.ll_user);
        civHead = (CircleImageView) view.findViewById(R.id.civ_head);
        tvHead = (TextView) view.findViewById(R.id.tv_head);
        tvNumber = (TextView) view.findViewById(R.id.tv_number);
        tvClass = (TextView) view.findViewById(R.id.tv_class);
        tvMajor = (TextView) view.findViewById(R.id.tv_major);
        tvCollege = (TextView) view.findViewById(R.id.tv_college);
        tvSchool = (TextView) view.findViewById(R.id.tv_school);
        pdLogin = new ProgressDialog(getActivity());
        pdLogin.setMessage("正在加载信息...");
        pdLogin.setCancelable(false);
    }

    private void initData() {
        app = (ApplicationInstance) getActivity().getApplication();
        number = app.getNumber();
        name = app.getName();
        cookie = app.getEduCookie();
        new Thread(loadUser).start();
    }

    @Override
    protected UserPresenter onCreatePresenter() {
        return new UserPresenter(UserEduFragment.this);
    }

    @Override
    public void showUser(UserBean bean) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = bean;
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
