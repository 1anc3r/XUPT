package me.lancer.xupt.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.book.BookBean;
import me.lancer.xupt.mvp.loginlib.ILoginLibView;
import me.lancer.xupt.mvp.loginlib.LoginLibPresenter;
import me.lancer.xupt.ui.adapter.CurrentAdapter;
import me.lancer.xupt.ui.adapter.FavoriteAdapter;
import me.lancer.xupt.ui.adapter.HistoryAdapter;
import me.lancer.xupt.ui.application.ApplicationInstance;
import me.lancer.xupt.ui.view.CircleImageView;
import me.lancer.xupt.ui.view.ClearEditText;

public class UserLibFragment extends PresenterFragment<LoginLibPresenter> implements ILoginLibView {

    ApplicationInstance app = new ApplicationInstance();

    LinearLayout llUser, llCurrent, llHistory, llFavorite;
    CircleImageView civHead;
    TextView tvHead, tvDebt, tvCurrent, tvHistory, tvFavorite, tvType, tvTitle;
    RecyclerView rvList;
    View loginDialogView, listDialogView;
    AlertDialog loginDialog;
    BottomSheetDialog listDialog;
    ClearEditText cetNumber, cetCheckCode;
    EditText etPassword;
    ImageView ivCheckCode;
    Button btnLogin;
    ProgressDialog pdLogin;

    List<BookBean> currentList = new ArrayList<>(), historyList = new ArrayList<>(), favoriteList = new ArrayList<>();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String number, password, cookie;

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
                    showSnackbar(llUser, (String) msg.obj);
                    break;
                case 3:
                    pdLogin.dismiss();
                    loginDialog.dismiss();
                    app.setLibCookie(cookie);
                    tvHead.setText(app.getName());
                    new Thread(getDebt).start();
                    break;
                case 4:
                    if (msg.obj != null) {
                        tvHead.setText(app.getName());
                        tvDebt.setText(msg.obj.toString() + "元");
                        new Thread(getList).start();
                    }
                    break;
                case 5:
                    currentList = (List<BookBean>) msg.obj;
                    if (currentList != null) {
                        tvCurrent.setText("" + currentList.size());
                    } else {
                        tvCurrent.setText("0");
                    }
                    break;
                case 6:
                    historyList = (List<BookBean>) msg.obj;
                    if (historyList != null) {
                        tvHistory.setText("" + historyList.size());
                    } else {
                        tvHistory.setText("0");
                    }
                    break;
                case 7:
                    favoriteList = (List<BookBean>) msg.obj;
                    if (favoriteList != null) {
                        tvFavorite.setText("" + favoriteList.size());
                    } else {
                        tvFavorite.setText("0");
                    }
                    break;
            }
        }
    };

    Runnable login = new Runnable() {
        @Override
        public void run() {
            presenter.login(number, password);
        }
    };

    Runnable getDebt = new Runnable() {
        @Override
        public void run() {
            presenter.getDebt(cookie);
        }
    };

    Runnable getList = new Runnable() {
        @Override
        public void run() {
            presenter.getCurrent(cookie);
            presenter.getHistory(cookie);
            presenter.getFavorite(cookie);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_lib, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        number = sharedPreferences.getString("number", "");
        password = sharedPreferences.getString("passwordlib", "");
        llUser = (LinearLayout) view.findViewById(R.id.ll_user);
        llCurrent = (LinearLayout) view.findViewById(R.id.ll_current);
        llCurrent.setOnClickListener(vOnClickListener);
        llHistory = (LinearLayout) view.findViewById(R.id.ll_history);
        llHistory.setOnClickListener(vOnClickListener);
        llFavorite = (LinearLayout) view.findViewById(R.id.ll_favorite);
        llFavorite.setOnClickListener(vOnClickListener);
        civHead = (CircleImageView) view.findViewById(R.id.civ_head);
        civHead.setOnClickListener(vOnClickListener);
        tvHead = (TextView) view.findViewById(R.id.tv_head);
        tvDebt = (TextView) view.findViewById(R.id.tv_debt);
        tvCurrent = (TextView) view.findViewById(R.id.tv_current);
        tvHistory = (TextView) view.findViewById(R.id.tv_history);
        tvFavorite = (TextView) view.findViewById(R.id.tv_favorite);
        pdLogin = new ProgressDialog(getActivity());
        pdLogin.setMessage("正在加载信息...");
        pdLogin.setCancelable(false);
        showLoginDialog();
    }

    private void initData() {
        app = (ApplicationInstance) getActivity().getApplication();
//        number = app.getNumber();
//        password = "123456";
        new Thread(login).start();
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == llCurrent) {
                showListDialog(1, currentList);
            } else if (v == llHistory) {
                showListDialog(2, historyList);
            } else if (v == llFavorite) {
                showListDialog(3, favoriteList);
            } else if (v == civHead) {
                loginDialog.show();
            } else if (v == btnLogin) {
                number = cetNumber.getText().toString();
                password = etPassword.getText().toString();
                sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("number", number);
                editor.putString("passwordlib", password);
                editor.apply();
                loginDialog.dismiss();
                new Thread(login).start();
            }
        }
    };

    public void showListDialog(int type, List<BookBean> list) {
        listDialogView = View.inflate(getActivity(), R.layout.list_dialog, null);
        tvType = (TextView) listDialogView.findViewById(R.id.tv_type);
        switch (type) {
            case 1:
                tvType.setText("当前借阅");
                break;
            case 2:
                tvType.setText("历史借阅");
                break;
            case 3:
                tvType.setText("收藏关注");
                break;
        }
        rvList = (RecyclerView) listDialogView.findViewById(R.id.rv_list);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.Adapter adapter = null;
        switch (type) {
            case 1:
                adapter = new CurrentAdapter(list);
                break;
            case 2:
                adapter = new HistoryAdapter(list);
                break;
            case 3:
                adapter = new FavoriteAdapter(list);
                break;
        }
        rvList.setAdapter(adapter);

        listDialog = new BottomSheetDialog(getActivity());
        listDialog.setContentView(listDialogView);
        listDialog.show();
    }

    private void showLoginDialog(){
        loginDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.login_dialog, null);
        tvTitle = (TextView) loginDialogView.findViewById(R.id.tv_title);
        tvTitle.setText("登录图书馆");
        cetNumber = (ClearEditText) loginDialogView.findViewById(R.id.cet_number);
        cetNumber.setText(number);
        etPassword = (EditText) loginDialogView.findViewById(R.id.et_password);
        etPassword.setText(password);
        cetCheckCode = (ClearEditText) loginDialogView.findViewById(R.id.cet_checkcode);
        cetCheckCode.setVisibility(View.GONE);
        ivCheckCode = (ImageView) loginDialogView.findViewById(R.id.iv_checkcode);
        ivCheckCode.setVisibility(View.GONE);
        btnLogin = (Button) loginDialogView.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(vOnClickListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(loginDialogView);
        loginDialog = builder.create();
    }

    @Override
    protected LoginLibPresenter onCreatePresenter() {
        return new LoginLibPresenter(UserLibFragment.this);
    }

    @Override
    public void login(String cookie) {
        this.cookie = cookie;
        app.setLibCookie(cookie);
        Message msg = new Message();
        msg.what = 3;
        msg.obj = cookie;
        handler.sendMessage(msg);
    }

    @Override
    public void showDebt(String debt) {
        Message msg = new Message();
        msg.what = 4;
        msg.obj = debt;
        handler.sendMessage(msg);
    }

    @Override
    public void showCurrent(List<BookBean> list) {
        Message msg = new Message();
        msg.what = 5;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showHistory(List<BookBean> list) {
        Message msg = new Message();
        msg.what = 6;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showFavorite(List<BookBean> list) {
        Message msg = new Message();
        msg.what = 7;
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