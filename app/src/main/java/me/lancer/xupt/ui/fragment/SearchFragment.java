package me.lancer.xupt.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.book.BookBean;
import me.lancer.xupt.mvp.book.BookPresenter;
import me.lancer.xupt.mvp.book.IBookView;
import me.lancer.xupt.ui.adapter.SearchAdapter;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class SearchFragment extends PresenterFragment<BookPresenter> implements IBookView {

    EditText etSearch;
    SwipeRefreshLayout srlResult;
    RecyclerView rvResult;

    List<BookBean> resultList = new ArrayList<>();

    String keyword;

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
                        resultList = (List<BookBean>) msg.obj;
                        SearchAdapter searchAdapter = new SearchAdapter(resultList);
                        rvResult.setAdapter(searchAdapter);
                        srlResult.setRefreshing(false);
                    } else {
                        srlResult.setRefreshing(false);
                    }
                    break;
            }
        }
    };

    Runnable search = new Runnable() {
        @Override
        public void run() {
            presenter.search(keyword);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView(view);
    }

    private void initData() {

    }

    private void initView(View view) {
        etSearch = (EditText) view.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = etSearch.getText().toString();
                if (!keyword.equals("")) {
                    srlResult.setVisibility(View.VISIBLE);
                    srlResult.setRefreshing(true);
                    new Thread(search).start();
                } else {
                    srlResult.setVisibility(View.GONE);
                    srlResult.setRefreshing(false);
                }
            }
        });
        srlResult = (SwipeRefreshLayout) view.findViewById(R.id.srl_result);
        srlResult.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        srlResult.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                keyword = etSearch.getText().toString();
                if (keyword != null) {
                    srlResult.setRefreshing(true);
                    new Thread(search).start();
                }
            }
        });
        srlResult.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        rvResult = (RecyclerView) view.findViewById(R.id.rv_result);
        LinearLayoutManager llm0 = new LinearLayoutManager(getContext());
        rvResult.setLayoutManager(llm0);
        rvResult.setItemAnimator(new DefaultItemAnimator());
        rvResult.setHasFixedSize(true);
        SearchAdapter searchAdapter = new SearchAdapter(resultList);
        rvResult.setAdapter(searchAdapter);
    }

    @Override
    protected BookPresenter onCreatePresenter() {
        return new BookPresenter(this);
    }

    @Override
    public void search(List<BookBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
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
