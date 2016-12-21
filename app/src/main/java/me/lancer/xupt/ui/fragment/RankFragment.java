package me.lancer.xupt.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.book.BookBean;
import me.lancer.xupt.mvp.book.BookPresenter;
import me.lancer.xupt.mvp.book.IBookView;
import me.lancer.xupt.ui.adapter.RankAdapter;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class RankFragment extends PresenterFragment<BookPresenter> implements IBookView {

    SwipeRefreshLayout srlRank;
    RecyclerView rvRank;

    List<BookBean> bookList = new ArrayList<>();

    String type, size;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    Log.e("log", (String) msg.obj);
                    break;
                case 3:
                    if (msg.obj != null) {
                        bookList = (List<BookBean>) msg.obj;
                        RankAdapter adapter = new RankAdapter(bookList, type);
                        rvRank.setAdapter(adapter);
                    }
                    srlRank.setRefreshing(false);
                    break;
            }
        }
    };

    Runnable rank = new Runnable() {
        @Override
        public void run() {
            presenter.rank(type, size);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rank, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
    }

    private void initData() {
        type = "1";
        size = "20";
    }

    private void initView(View view) {
        srlRank = (SwipeRefreshLayout) view.findViewById(R.id.srl_rank);
        srlRank.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        srlRank.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlRank.setRefreshing(true);
                new Thread(rank).start();
            }
        });
        srlRank.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        rvRank = (RecyclerView) view.findViewById(R.id.rv_rank);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvRank.setLayoutManager(llm);
        rvRank.setItemAnimator(new DefaultItemAnimator());
        rvRank.setHasFixedSize(true);
        RankAdapter adapter = new RankAdapter(bookList, type);
        rvRank.setAdapter(adapter);
        srlRank.setRefreshing(true);
        new Thread(rank).start();
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
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
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
