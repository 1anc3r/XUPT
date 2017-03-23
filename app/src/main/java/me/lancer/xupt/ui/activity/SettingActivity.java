package me.lancer.xupt.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.base.activity.BaseActivity;
import me.lancer.xupt.mvp.loginedu.activity.LoginEduActivity;
import me.lancer.xupt.ui.adapter.SettingAdapter;
import me.lancer.xupt.ui.application.mApp;
import me.lancer.xupt.ui.application.mParams;

public class SettingActivity extends BaseActivity {

    mApp app;

    LinearLayout llNight, llFunc, llProblem, llFeedback, llDownload, llAboutUs;
    Button btnLoginOut;
    SwitchCompat scNight;
    BottomSheetDialog listDialog;
    AlertDialog aboutDialog, downloadDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isNight = false;

    List<String> funcList = new ArrayList<>(), problemList = new ArrayList<>();

    private final String root = Environment.getExternalStorageDirectory() + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
    }

    private void initView() {
        initToolbar(getString(R.string.settingcn));
        llNight = (LinearLayout) findViewById(R.id.ll_night);
        llNight.setOnClickListener(vOnClickListener);
        llFunc = (LinearLayout) findViewById(R.id.ll_func);
        llFunc.setOnClickListener(vOnClickListener);
        llProblem = (LinearLayout) findViewById(R.id.ll_problem);
        llProblem.setOnClickListener(vOnClickListener);
        llFeedback = (LinearLayout) findViewById(R.id.ll_feedback);
        llFeedback.setOnClickListener(vOnClickListener);
        llDownload = (LinearLayout) findViewById(R.id.ll_download);
        llDownload.setOnClickListener(vOnClickListener);
        llAboutUs = (LinearLayout) findViewById(R.id.ll_about_us);
        llAboutUs.setOnClickListener(vOnClickListener);
        btnLoginOut = (Button) findViewById(R.id.btn_login_out);
        btnLoginOut.setOnClickListener(vOnClickListener);
        scNight = (SwitchCompat) findViewById(R.id.sc_night);
        showAboutDialog();
        showDownloadDialog();
    }

    private void initData() {
        app = (mApp) getApplication();
        sharedPreferences = getSharedPreferences(getString(R.string.spf_user), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isNight = sharedPreferences.getBoolean(mParams.ISNIGHT, false);
        scNight.setChecked(isNight);
        scNight.setClickable(false);
        funcList.add("教务处 : 查询课表、成绩和个人信息");
        funcList.add("考勤表 : 查看考勤记录明细和统计, 也可以申诉");
        funcList.add("图书馆 : 检索图书，查看当前借阅、历史借阅和收藏, 也可以(取消)收藏、续借");
        funcList.add("四六级 : 查询英语四六级考试成绩, 包括听力、阅读、综合、写作和总分");
        problemList.add("教务处 : 请使用西邮教务管理系统的账号和密码登录");
        problemList.add("考勤表 : 使用西邮一卡通账号密码登录, 密码默认是学号后六位");
        problemList.add("图书馆 : 使用西邮图书馆账号密码登录, 密码默认是123456");
        problemList.add("应用内意见反馈通道尚未开启, 遇到Bug请发送邮件至huangfangzhi0@foxmail.com");
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == llNight){
                if (!isNight) {
                    editor.putBoolean(mParams.ISNIGHT, true);
                    editor.apply();
                    scNight.setChecked(true);
                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                } else {
                    editor.putBoolean(mParams.ISNIGHT, false);
                    editor.apply();
                    scNight.setChecked(false);
                    getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                }
            } else if (v == llFunc){
                showListDialog(1, funcList);
            } else if (v == llProblem){
                showListDialog(2, problemList);
            } else if (v == llFeedback){

            } else if (v == llDownload){
                downloadDialog.show();
            } else if (v == llAboutUs){
                aboutDialog.show();
            } else if (v == btnLoginOut){
                finish();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            startActivity(new Intent().setClass(mActivity, MainActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            startActivity(new Intent().setClass(SettingActivity.this, MainActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showListDialog(int type, List<String> list) {
        View listDialogView = View.inflate(mActivity, R.layout.list_dialog, null);
        TextView tvType = (TextView) listDialogView.findViewById(R.id.tv_type);
        switch (type) {
            case 1:
                tvType.setText("功能介绍");
                break;
            case 2:
                tvType.setText("常见问题");
                break;
        }
        RecyclerView rvList = (RecyclerView) listDialogView.findViewById(R.id.rv_list);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        RecyclerView.Adapter adapter = new SettingAdapter(list);
        rvList.setAdapter(adapter);

        listDialog = new BottomSheetDialog(mActivity);
        listDialog.setContentView(listDialogView);
        listDialog.show();
    }

    private void showAboutDialog(){
        View aboutDialogView = LayoutInflater.from(mActivity).inflate(R.layout.about_dialog, null);
        TextView tvOrganization = (TextView) aboutDialogView.findViewById(R.id.tv_organization);
        tvOrganization.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.xiyoumobile.com/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvBlog = (TextView) aboutDialogView.findViewById(R.id.tv_blog);
        tvBlog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.1anc3r.me");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(aboutDialogView);
        aboutDialog = builder.create();
    }

    private void showDownloadDialog(){
        View downloadDialogView = LayoutInflater.from(mActivity).inflate(R.layout.download_dialog, null);
        TextView tvAirFreeDownload = (TextView) downloadDialogView.findViewById(R.id.tv_airfree_download);
        tvAirFreeDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://o7gy5l0ax.bkt.clouddn.com/AirFree-Client.apk");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvAirFreeBlog = (TextView) downloadDialogView.findViewById(R.id.tv_airfree_blog);
        tvAirFreeBlog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.1anc3r.me/airfree-android-to-pc-remote/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvAirFreeGithub = (TextView) downloadDialogView.findViewById(R.id.tv_airfree_github);
        tvAirFreeGithub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://github.com/1anc3r/AirFree-Client");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvXuptDownload = (TextView) downloadDialogView.findViewById(R.id.tv_xupt_download);
        tvXuptDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://raw.githubusercontent.com/1anc3r/XUPT/master/screenshot/xupt.apk");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvXuptBlog = (TextView) downloadDialogView.findViewById(R.id.tv_xupt_blog);
        tvXuptBlog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.1anc3r.me/%E8%A5%BF%E9%82%AE%E8%AE%B0xupt/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvXuptGithub = (TextView) downloadDialogView.findViewById(R.id.tv_xupt_github);
        tvXuptGithub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://github.com/1anc3r/XUPT");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvSevenPoundsDownload = (TextView) downloadDialogView.findViewById(R.id.tv_sevenpounds_download);
        tvSevenPoundsDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://raw.githubusercontent.com/1anc3r/SevenPounds/master/screenshot/sevenpounds.apk");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvSevenPoundsBlog = (TextView) downloadDialogView.findViewById(R.id.tv_sevenpounds_blog);
        tvSevenPoundsBlog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.1anc3r.me/%E4%B8%83%E7%A3%85sevenpounds/");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        TextView tvSevenPoundsGithub = (TextView) downloadDialogView.findViewById(R.id.tv_sevenpounds_github);
        tvSevenPoundsGithub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://github.com/1anc3r/AirFree-Client");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(downloadDialogView);
        downloadDialog = builder.create();
    }
}
