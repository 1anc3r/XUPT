package me.lancer.xupt.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.book.BookBean;
import me.lancer.xupt.mvp.book.BookPresenter;
import me.lancer.xupt.mvp.book.IBookView;
import me.lancer.xupt.ui.adapter.CircleAdapter;
import me.lancer.xupt.ui.adapter.SearchAdapter;
import me.lancer.xupt.ui.application.ApplicationInstance;

public class BookDetailActivity extends PresenterActivity<BookPresenter> implements IBookView {

    ApplicationInstance app = new ApplicationInstance();

    LinearLayout llBookDetail;
    Toolbar toolbar;
    ImageView ivBook;
    TextView tvName, tvAuthor, tvPublish, tvSubject, tvTotal, tvAvaliable;
    RecyclerView rvCircle, rvRefer;
    ProgressDialog pdLogin;

    Map<String, List<BookBean>> map = new HashMap<>();
    List<BookBean> detailList, circleList, referList;

    int key;
    String value;

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
                    showSnackbar(llBookDetail, (String) msg.obj);
                    break;
                case 3:
                    if (msg.obj != null) {
                        map = (Map<String, List<BookBean>>) msg.obj;
                        detailList = map.get(getString(R.string.detail));
                        circleList = map.get(getString(R.string.circle));
                        referList = map.get(getString(R.string.refer));
                        showImage(detailList.get(0).getBookImage());
                        tvName.setText(detailList.get(0).getBookMainTitle());
                        tvAuthor.setText(getString(R.string.Author) + detailList.get(0).getBookAuthor());
                        tvPublish.setText(getString(R.string.publish) + detailList.get(0).getBookPublish());
                        tvSubject.setText(getString(R.string.subject) + detailList.get(0).getBookSubject());
                        tvTotal.setText(getString(R.string.total) + detailList.get(0).getBookTotal());
                        tvAvaliable.setText(getString(R.string.available) + detailList.get(0).getBookAvailable());
                        CircleAdapter adapterCircle = new CircleAdapter(circleList);
                        rvCircle.setAdapter(adapterCircle);
                        SearchAdapter adapterRefer = new SearchAdapter(referList);
                        rvRefer.setAdapter(adapterRefer);
                        pdLogin.dismiss();
                    }
                    break;
            }
        }
    };

    Runnable detail = new Runnable() {
        @Override
        public void run() {
            presenter.detail(key, value);
        }
    };

    Runnable addFavorite = new Runnable() {
        @Override
        public void run() {
            presenter.addFavorite(detailList.get(0).getBookId(), app.getLibCookie());
        }
    };

    Runnable delFavorite = new Runnable() {
        @Override
        public void run() {
            presenter.delFavorite(detailList.get(0).getBookId(), app.getNumber(), app.getLibCookie());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initData();
        initView();
    }

    private void initData() {
        app = (ApplicationInstance) this.getApplication();
        Intent intent = getIntent();
        key = intent.getIntExtra(getString(R.string.key), 0);
        value = intent.getStringExtra(getString(R.string.value));
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.book_detail);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.menu_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_add_favorite:
                        new Thread(addFavorite).start();
                        break;
                    case R.id.menu_del_favorite:
                        new Thread(delFavorite).start();
                        break;
                    case R.id.menu_renew:
                        showToast(mActivity, getString(R.string.lazy));
                        break;
                }
                return true;
            }
        });
        llBookDetail = (LinearLayout) findViewById(R.id.ll_book_detail);
        ivBook = (ImageView) findViewById(R.id.iv_book);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvAuthor = (TextView) findViewById(R.id.tv_author);
        tvPublish = (TextView) findViewById(R.id.tv_publish);
        tvSubject = (TextView) findViewById(R.id.tv_subject);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        tvAvaliable = (TextView) findViewById(R.id.tv_avaliable);
        rvCircle = (RecyclerView) findViewById(R.id.rv_circle);
        LinearLayoutManager llmCircle = new LinearLayoutManager(this);
        llmCircle.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCircle.setLayoutManager(llmCircle);
        rvCircle.setItemAnimator(new DefaultItemAnimator());
        rvCircle.setHasFixedSize(true);
        CircleAdapter adapterCircle = new CircleAdapter(circleList);
        rvCircle.setAdapter(adapterCircle);
        rvRefer = (RecyclerView) findViewById(R.id.rv_refer);
        LinearLayoutManager llmRefer = new LinearLayoutManager(this);
        rvRefer.setLayoutManager(llmRefer);
        rvRefer.setItemAnimator(new DefaultItemAnimator());
        rvRefer.setHasFixedSize(true);
        SearchAdapter adapterRefer = new SearchAdapter(referList);
        rvRefer.setAdapter(adapterRefer);
        pdLogin = new ProgressDialog(this);
        pdLogin.setMessage(getString(R.string.book_loading));
        pdLogin.setCancelable(false);
        new Thread(detail).start();
    }

    private void showImage(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ivBook.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ivBook.setImageResource(R.mipmap.ic_launcher);
            }
        });
        queue.add(imageRequest);
    }

    @Override
    protected BookPresenter onCreatePresenter() {
        return new BookPresenter(this);
    }

    @Override
    public void search(List<BookBean> list) {

    }

    @Override
    public void rank(List<BookBean> list) {

    }

    @Override
    public void detail(Map<String, List<BookBean>> map) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = map;
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
