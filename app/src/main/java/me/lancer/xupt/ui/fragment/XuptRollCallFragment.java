package me.lancer.xupt.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.logincard.ILoginCardView;
import me.lancer.xupt.mvp.logincard.LoginCardPresenter;
import me.lancer.xupt.ui.activity.LoginCardActivity;
import me.lancer.xupt.ui.activity.MainActivity;
import me.lancer.xupt.ui.application.ApplicationInstance;
import me.lancer.xupt.ui.view.ClearEditText;
import me.lancer.xupt.util.NetworkDiagnosis;

public class XuptRollCallFragment extends PresenterFragment<LoginCardPresenter> implements ILoginCardView {

    ApplicationInstance app = new ApplicationInstance();

    CoordinatorLayout clRollCall;
    View loginDialogView;
    AlertDialog loginDialog;
    TextView tvTitle;
    ClearEditText cetNumber, cetCheckCode;
    EditText etPassword;
    ImageView ivCheckCode;
    Button btnLogin;
    ProgressDialog pdLogin;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String number, password, checkcode, cookie;

    private Toolbar toolbar;
    private int index = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                String log = (String) msg.obj;
                if (log.equals("show")) {
                    pdLogin.show();
                } else if (log.equals("hide")) {
                    pdLogin.dismiss();
                } else if (log.equals("checkcode")) {
                    String checkCodePath = Environment.getExternalStorageDirectory() + "/me.lancer.xupt/CheckCode.png";
                    Bitmap bitmap = BitmapFactory.decodeFile(checkCodePath);
                    ivCheckCode.setImageBitmap(bitmap);
                } else if (log.equals("login")) {
                    app.setRollcall(true);
                    loginDialog.dismiss();
                    initTabLayout(getView());
                } else {
                    Log.e("log", (String) msg.obj);
                    showSnackbar(clRollCall, (String) msg.obj);
                }
            }
        }
    };

    Runnable login = new Runnable() {
        @Override
        public void run() {
            presenter.login(number, password, checkcode, cookie);
        }
    };

    Runnable loadCheckCode = new Runnable() {
        @Override
        public void run() {
            presenter.loadCheckCode();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_xupt_rollcall, container, false);
    }

    @Override
    protected LoginCardPresenter onCreatePresenter() {
        return new LoginCardPresenter(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("考勤表");
        ((MainActivity) getActivity()).initDrawer(toolbar);
        initTabLayout(view);
        inflateMenu();
        initSearchView();
        initData();
        initView(view);
    }

    private void initTabLayout(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Fragment newfragment = new DetailFragment();
        Bundle data = new Bundle();
        data.putInt("id", 0);
        data.putString("title", "明细");
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "明细");

        newfragment = new StatisticFragment();
        data = new Bundle();
        data.putInt("id", 1);
        data.putString("title", "统计");
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "统计");

        viewPager.setAdapter(adapter);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            index = bundle.getInt("index");
        }
        viewPager.setCurrentItem(index, true);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void inflateMenu() {
        toolbar.inflateMenu(R.menu.menu_search);
    }

    private void initSearchView() {
        final SearchView searchView = (SearchView) toolbar.getMenu()
                .findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("搜索…");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("onQueryTextChange", s);
                return false;
            }
        });
    }

    private void initData() {
        app = (ApplicationInstance) getActivity().getApplication();
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        number = sharedPreferences.getString("number", "");
        password = sharedPreferences.getString("passwordcard", "");
    }

    private void initView(View view) {
        clRollCall = (CoordinatorLayout) view.findViewById(R.id.cl_rollcall);
        pdLogin = new ProgressDialog(getActivity());
        pdLogin.setMessage("正在登录...");
        pdLogin.setCancelable(false);
        showLoginDialog();
        if (!app.isRollcall()) {
            loginDialog.show();
            new Thread(loadCheckCode).start();
        }
    }

    private void showLoginDialog() {
        loginDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.login_dialog, null);
        tvTitle = (TextView) loginDialogView.findViewById(R.id.tv_title);
        tvTitle.setText("登录考勤表");
        cetNumber = (ClearEditText) loginDialogView.findViewById(R.id.cet_number);
        cetNumber.setText(number);
        etPassword = (EditText) loginDialogView.findViewById(R.id.et_password);
        etPassword.setText(password);
        cetCheckCode = (ClearEditText) loginDialogView.findViewById(R.id.cet_checkcode);
        ivCheckCode = (ImageView) loginDialogView.findViewById(R.id.iv_checkcode);
        btnLogin = (Button) loginDialogView.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(vOnClickListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(loginDialogView);
        loginDialog = builder.create();
        loginDialog.setCancelable(false);
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnLogin) {
                NetworkDiagnosis networkDiagnosis = new NetworkDiagnosis();
                if (networkDiagnosis.checkNetwork(getActivity().getApplication())) {
                    if (cetNumber.getText().toString().equals("")) {
                    } else if (etPassword.getText().toString().equals("")) {
                    } else {
                        number = cetNumber.getText().toString();
                        password = etPassword.getText().toString();
                        checkcode = cetCheckCode.getText().toString();
                        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("number", number);
                        editor.putString("passwordcard", password);
                        editor.apply();
                        loginDialog.dismiss();
                        new Thread(login).start();
                    }
                }
            }
        }
    };

    @Override
    public void showCheckCode(String cookie) {
        this.cookie = cookie;
        Log.e("cookie", cookie);
        app.setCardCookie0(cookie);
        Message msg = new Message();
        msg.obj = "checkcode";
        handler.sendMessage(msg);
    }

    @Override
    public void login(String cookie) {
        this.cookie = cookie;
        Log.e("cookie", cookie);
        app.setCardCookie1(cookie);
        Message msg = new Message();
        msg.obj = "login";
        handler.sendMessage(msg);
    }

    @Override
    public void showMsg(String log) {
        Message msg = new Message();
        msg.obj = log;
        handler.sendMessage(msg);
    }

    @Override
    public void showLoad() {
        Message msg = new Message();
        msg.obj = "show";
        handler.sendMessage(msg);
    }

    @Override
    public void hideLoad() {
        Message msg = new Message();
        msg.obj = "hide";
        handler.sendMessage(msg);
    }
}
