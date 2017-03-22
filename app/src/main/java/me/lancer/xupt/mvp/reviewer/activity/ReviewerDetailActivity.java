package me.lancer.xupt.mvp.reviewer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;
import java.util.Map;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.base.activity.PresenterActivity;
import me.lancer.xupt.mvp.book.BookBean;
import me.lancer.xupt.mvp.book.BookPresenter;
import me.lancer.xupt.mvp.reviewer.bean.ReviewerBean;
import me.lancer.xupt.mvp.book.IBookView;
import me.lancer.xupt.ui.application.mApp;

public class ReviewerDetailActivity extends PresenterActivity<BookPresenter> implements IBookView {

    mApp app = new mApp();

    LinearLayout llReviewerDetail;
    Toolbar toolbar;
    ImageView ivBook;
    TextView tvTime, tvTitle, tvDetail;
    RatingBar rbRating;
    ProgressDialog pdLogin;

    ReviewerBean brItem = new ReviewerBean();

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
                    showSnackbar(llReviewerDetail, (String) msg.obj);
                    break;
                case 3:
                    if (msg.obj != null) {
                        ReviewerBean brTemp = (ReviewerBean) msg.obj;
                        brItem.setRating(brTemp.getRating());
                        brItem.setTime(brTemp.getTime());
                        brItem.setDetail("\u3000\u3000" + brTemp.getDetail().replace(" ", "\n\u3000\u3000"));
                        tvTime.setText("日期 : " + brItem.getTime());
                        tvDetail.setText(brItem.getDetail());
                        rbRating.setRating(Float.parseFloat(brItem.getRating()));
                        pdLogin.dismiss();
                    }
                    break;
            }
        }
    };

    Runnable view = new Runnable() {
        @Override
        public void run() {
            presenter.view(brItem.getHref());
        }
    };

    @Override
    protected BookPresenter onCreatePresenter() {
        return new BookPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewer_detail);

        initData();
        initView();
    }

    private void initData() {
        app = (mApp) this.getApplication();
        Intent intent = getIntent();
        brItem.setTitle(intent.getStringExtra("title"));
        brItem.setHref(intent.getStringExtra("url"));
        brItem.setImg(intent.getStringExtra("img"));
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("书评详情");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llReviewerDetail = (LinearLayout) findViewById(R.id.ll_reviewer_detail);
        ivBook = (ImageView) findViewById(R.id.iv_book);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        rbRating = (RatingBar) findViewById(R.id.rb_rating);
        tvTitle.setText(brItem.getTitle());
        pdLogin = new ProgressDialog(this);
        pdLogin.setMessage(getString(R.string.book_loading));
        pdLogin.setCancelable(false);
        showImage(brItem.getImg());
        new Thread(view).start();
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
    public void search(List<BookBean> list) {

    }

    @Override
    public void reviewer(List<ReviewerBean> list) {

    }

    @Override
    public void view(ReviewerBean item) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = item;
        handler.sendMessage(msg);
    }

    @Override
    public void rank(List<BookBean> list) {

    }

    @Override
    public void detail(Map<String, List<BookBean>> map) {

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
