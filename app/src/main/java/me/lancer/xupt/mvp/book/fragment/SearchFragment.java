package me.lancer.xupt.mvp.book.fragment;

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
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.base.fragment.PresenterFragment;
import me.lancer.xupt.mvp.book.BookBean;
import me.lancer.xupt.mvp.book.BookPresenter;
import me.lancer.xupt.mvp.reviewer.bean.ReviewerBean;
import me.lancer.xupt.mvp.book.IBookView;
import me.lancer.xupt.mvp.reviewer.adapter.ReviewerAdapter;
import me.lancer.xupt.mvp.book.adapter.SearchAdapter;

/**
 * Created by HuangFangzhi on 2016/12/18.
 */

public class SearchFragment extends PresenterFragment<BookPresenter> implements IBookView {

    EditText etSearch;
    SwipeRefreshLayout srlResult;
    RecyclerView rvResult;
    LinearLayout llLibary;

    SearchAdapter searchAdapter;

    LinearLayoutManager llm0;
    List<BookBean> resultList = new ArrayList<>();

    String keyword;
    int type = 1, pager = 0, last = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    Log.e(getString(R.string.log), (String) msg.obj);
                    break;
                case 3:
                    if (msg.obj != null) {
                        resultList = (List<BookBean>) msg.obj;
                        searchAdapter = new SearchAdapter(resultList);
                        rvResult.setAdapter(searchAdapter);
                        srlResult.setVisibility(View.VISIBLE);
                        llLibary.setVisibility(View.GONE);
                    }
                    srlResult.setRefreshing(false);
                    break;
//                case 4:
//                    if (msg.obj != null) {
//                        if (pager == 0) {
//                            resultList.clear();
//                            reviewerList = (List<ReviewerBean>) msg.obj;
//                            reviewerAdapter = new ReviewerAdapter(getActivity(), reviewerList);
//                        } else {
//                            reviewerList.addAll((List<ReviewerBean>) msg.obj);
//                            for (int i = 0; i < 10; i++) {
//                                reviewerAdapter.notifyItemInserted(pager + i);
//                            }
//                        }
//                        llLibary.setVisibility(View.VISIBLE);
//                        srlResult.setVisibility(View.GONE);
//                    }
//                    srlResult.setRefreshing(false);
//                    break;
            }
        }
    };

    Runnable search = new Runnable() {
        @Override
        public void run() {
            presenter.search(keyword);
            keyword = "";
        }
    };

    Runnable reviewer = new Runnable() {
        @Override
        public void run() {
            presenter.reviewer(type, pager);
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

        initView(view);
        initData();
    }

    private void initData() {
        new Thread(reviewer).start();
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
                if (keyword != null && !keyword.equals("")) {
                    srlResult.setVisibility(View.VISIBLE);
                    llLibary.setVisibility(View.GONE);
                    srlResult.setRefreshing(true);
                    new Thread(search).start();
                } else {
                    srlResult.setVisibility(View.GONE);
                    llLibary.setVisibility(View.VISIBLE);
                    srlResult.setRefreshing(false);
                }
            }
        });

        llLibary = (LinearLayout) view.findViewById(R.id.ll_libary);
        srlResult = (SwipeRefreshLayout) view.findViewById(R.id.srl_result);
        srlResult.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        srlResult.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                keyword = etSearch.getText().toString();
                if (keyword != null && !keyword.equals("")) {
                    srlResult.setVisibility(View.VISIBLE);
                    llLibary.setVisibility(View.GONE);
                    srlResult.setRefreshing(true);
                    new Thread(search).start();
                } else {
                    srlResult.setVisibility(View.GONE);
                    llLibary.setVisibility(View.VISIBLE);
                }
            }
        });
        srlResult.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        rvResult = (RecyclerView) view.findViewById(R.id.rv_result);
        llm0 = new LinearLayoutManager(getContext());
        rvResult.setLayoutManager(llm0);
        rvResult.setItemAnimator(new DefaultItemAnimator());
        rvResult.setHasFixedSize(true);
        searchAdapter = new SearchAdapter(resultList);
        rvResult.setAdapter(searchAdapter);
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            srlResult.setVisibility(View.GONE);
            llLibary.setVisibility(View.VISIBLE);
            new Thread(reviewer).start();
        }
    };

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
    public void reviewer(List<ReviewerBean> list) {
//        Message msg = new Message();
//        msg.what = 4;
//        msg.obj = list;
//        handler.sendMessage(msg);
    }

    @Override
    public void view(ReviewerBean item) {

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
