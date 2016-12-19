package me.lancer.xupt.ui.activity;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.loginedu.ILoginEduView;
import me.lancer.xupt.mvp.loginedu.LoginEduPresenter;
import me.lancer.xupt.ui.application.ApplicationInstance;
import me.lancer.xupt.ui.view.ClearEditText;
import me.lancer.xupt.util.NetworkDiagnosis;

public class LoginEduActivity extends PresenterActivity<LoginEduPresenter> implements ILoginEduView {

    ApplicationInstance app = new ApplicationInstance();

    LinearLayout llLogin;
    ClearEditText cetNumber, cetCheckCode;
    EditText etPassword;
    ImageView ivCheckCode;
    Button btnLogin;
    ProgressDialog pdLogin;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String cookie, number, name, password, checkcode;

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
                    String checkCodePath = Environment.getExternalStorageDirectory() + "/me.lancer.xupt/CheckCode.gif";
                    Bitmap bitmap = BitmapFactory.decodeFile(checkCodePath);
                    ivCheckCode.setImageBitmap(bitmap);
                } else {
                    pdLogin.dismiss();
                    showSnackbar(llLogin, log);
                }
            }
        }
    };

    Runnable login = new Runnable() {
        @Override
        public void run() {
            presenter.login(cetNumber.getText().toString(), etPassword.getText().toString(), cetCheckCode.getText().toString(), cookie);
        }
    };

    Runnable loadCheckCode = new Runnable() {
        @Override
        public void run() {
            presenter.loadCheckCode();
        }
    };

    private final String root = Environment.getExternalStorageDirectory() + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar("登录");
        initView();
        initData();
    }

    private void initView() {
        llLogin = (LinearLayout) findViewById(R.id.ll_login);
        cetNumber = (ClearEditText) findViewById(R.id.cet_number);
        etPassword = (EditText) findViewById(R.id.et_password);
        cetCheckCode = (ClearEditText) findViewById(R.id.cet_checkcode);
        ivCheckCode = (ImageView) findViewById(R.id.iv_checkcode);
        ivCheckCode.setOnClickListener(vOnClickListener);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(vOnClickListener);
        pdLogin = new ProgressDialog(this);
        pdLogin.setMessage("正在登录...");
        pdLogin.setCancelable(false);
    }

    private void initData() {
        app = (ApplicationInstance) this.getApplication();
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        number = sharedPreferences.getString("number", "");
        name = sharedPreferences.getString("name", "");
        password = sharedPreferences.getString("password", "");
        cetNumber.setText(number);
        etPassword.setText(password);
        app.setCourse(true);
        app.setScore(true);
        app.setUser(true);
        if (new File(root + "me.lancer.xupt/course_" + number).exists() && new File(root + "me.lancer.xupt/score_" + number).exists() && new File(root + "me.lancer.xupt/user_" + number).exists()) {
            app.setNumber(number);
            app.setName(name);
            app.setCourse(false);
            app.setScore(false);
            app.setUser(false);
            Intent intent = new Intent();
            intent.setClass(LoginEduActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            new Thread(loadCheckCode).start();
        }
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnLogin) {
                NetworkDiagnosis networkDiagnosis = new NetworkDiagnosis();
                if (networkDiagnosis.checkNetwork(getApplication())) {
                    if (cetNumber.getText().toString().equals("")) {
                        showSnackbar(llLogin, "学号不能为空!");
                    } else if (etPassword.getText().toString().equals("")) {
                        showSnackbar(llLogin, "密码不能为空!");
                    } else {
                        number = cetNumber.getText().toString();
                        password = etPassword.getText().toString();
                        checkcode = cetCheckCode.getText().toString();
                        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("number", number);
                        editor.putString("password", password);
                        editor.apply();
                        if (!networkDiagnosis.checkNetwork(LoginEduActivity.this)) {
                            showSnackbar(llLogin, "网络连接错误!");
                        } else {
                            new Thread(login).start();
                        }
                    }
                }
            } else if (v == ivCheckCode) {
                Log.e("ivCheckCode", "change checkcode");
                new Thread(loadCheckCode).start();
            }
        }
    };

    @Override
    protected LoginEduPresenter onCreatePresenter() {
        return new LoginEduPresenter(this);
    }

    @Override
    public void showCheckCode(String cookie) {
        this.cookie = cookie;
        app.setEduCookie(cookie);
        Message msg = new Message();
        msg.obj = "checkcode";
        handler.sendMessage(msg);
    }

    @Override
    public void login(String cookie) {
        this.cookie = cookie;
        app.setEduCookie(cookie);
        presenter.home(cetNumber.getText().toString(), cookie);
    }

    @Override
    public void home(String number, String name) {
        app.setNumber(number);
        app.setName(name);
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("number", number);
        editor.putString("name", name);
        editor.apply();
        Intent intent = new Intent();
        intent.setClass(LoginEduActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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
