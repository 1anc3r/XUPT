package me.lancer.xupt.mvp.logincard.fragment;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.base.fragment.PresenterFragment;
import me.lancer.xupt.mvp.logincard.ILoginCardView;
import me.lancer.xupt.mvp.logincard.LoginCardPresenter;
import me.lancer.xupt.mvp.rollcall.fragment.DetailFragment;
import me.lancer.xupt.ui.activity.AboutActivity;
import me.lancer.xupt.ui.activity.MainActivity;
import me.lancer.xupt.ui.application.mApp;
import me.lancer.xupt.mvp.rollcall.fragment.StatisticFragment;
import me.lancer.xupt.ui.view.ClearEditText;
import me.lancer.xupt.util.NetworkDiagnosis;

public class XuptRollCallFragment extends PresenterFragment<LoginCardPresenter> implements ILoginCardView {

    mApp app = new mApp();

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
                if (log.equals(getString(R.string.show))) {
                    pdLogin.show();
                } else if (log.equals(getString(R.string.hide))) {
                    pdLogin.dismiss();
                } else if (log.equals(getString(R.string.checkcode))) {
                    String checkCodePath = Environment.getExternalStorageDirectory() + getString(R.string.path_checkcode_png);
                    Bitmap bitmap = BitmapFactory.decodeFile(checkCodePath);
                    ivCheckCode.setImageBitmap(bitmap);
                } else if (log.equals(getString(R.string.login))) {
                    app.setRollcall(true);
                    loginDialog.dismiss();
                    initTabLayout(getView());
                } else {
                    Log.e(getString(R.string.log), (String) msg.obj);
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
        toolbar.setTitle(R.string.roll_title);
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
        data.putInt(getString(R.string.id), 0);
        data.putString(getString(R.string.title), getString(R.string.roll_detail));
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, getString(R.string.roll_detail));

        newfragment = new StatisticFragment();
        data = new Bundle();
        data.putInt(getString(R.string.id), 1);
        data.putString(getString(R.string.title), getString(R.string.roll_statistic));
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, getString(R.string.roll_statistic));

        viewPager.setAdapter(adapter);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            index = bundle.getInt(getString(R.string.index));
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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_about:
                        Intent intent0 = new Intent();
                        intent0.putExtra("link", "https://github.com/1anc3r");
                        intent0.putExtra("title", "Github");
                        intent0.setClass(getActivity(), AboutActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.menu_blog:
                        Intent intent1 = new Intent();
                        intent1.putExtra("link", "https://www.1anc3r.me");
                        intent1.putExtra("title", "Blog");
                        intent1.setClass(getActivity(), AboutActivity.class);
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });
    }

//    private void inflateMenu() {
//        toolbar.inflateMenu(R.menu.menu_search);
//    }

    private void initSearchView() {
        final SearchView searchView = (SearchView) toolbar.getMenu()
                .findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showToast(getString(R.string.lazy));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void initData() {
        app = (mApp) getActivity().getApplication();
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.spf_user), Context.MODE_PRIVATE);
        number = sharedPreferences.getString(getString(R.string.spf_number), "");
        password = sharedPreferences.getString(getString(R.string.spf_passwd_roll), "");
    }

    private void initView(View view) {
        clRollCall = (CoordinatorLayout) view.findViewById(R.id.cl_rollcall);
        pdLogin = new ProgressDialog(getActivity());
        pdLogin.setMessage(getString(R.string.logining));
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
        tvTitle.setText(R.string.roll_login_title);
        cetNumber = (ClearEditText) loginDialogView.findViewById(R.id.cet_number);
        cetNumber.setText(number);
        etPassword = (EditText) loginDialogView.findViewById(R.id.et_password);
        etPassword.setHint("一卡通密码(默认学号后六位)");
        etPassword.setText(password);
        cetCheckCode = (ClearEditText) loginDialogView.findViewById(R.id.cet_checkcode);
        ivCheckCode = (ImageView) loginDialogView.findViewById(R.id.iv_checkcode);
        btnLogin = (Button) loginDialogView.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(vOnClickListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(loginDialogView);
        loginDialog = builder.create();
        loginDialog.setCancelable(true);
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnLogin) {
                NetworkDiagnosis networkDiagnosis = new NetworkDiagnosis();
                if (networkDiagnosis.checkNetwork(getActivity().getApplication())) {
                    if (cetNumber.getText().toString().equals("")) {
                        showSnackbar(clRollCall, getString(R.string.number_null));
                    } else if (etPassword.getText().toString().equals("")) {
                        showSnackbar(clRollCall, getString(R.string.passwd_null));
                    } else if (cetCheckCode.getText().toString().equals("")) {
                        showSnackbar(clRollCall, getString(R.string.checkcode_null));
                    } else {
                        number = cetNumber.getText().toString();
                        password = etPassword.getText().toString();
                        checkcode = cetCheckCode.getText().toString();
                        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.spf_user), Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.spf_number), number);
                        editor.putString(getString(R.string.spf_passwd_roll), password);
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
        app.setCardCookie0(cookie);
        Message msg = new Message();
        msg.obj = getString(R.string.checkcode);
        handler.sendMessage(msg);
    }

    @Override
    public void login(String cookie) {
        this.cookie = cookie;
        app.setCardCookie1(cookie);
        Message msg = new Message();
        msg.obj = getString(R.string.login);
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
        msg.obj = getString(R.string.show);
        handler.sendMessage(msg);
    }

    @Override
    public void hideLoad() {
        Message msg = new Message();
        msg.obj = getString(R.string.hide);
        handler.sendMessage(msg);
    }
}
