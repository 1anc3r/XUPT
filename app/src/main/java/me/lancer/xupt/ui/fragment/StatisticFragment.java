package me.lancer.xupt.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.rollcall.IRollCallView;
import me.lancer.xupt.mvp.rollcall.RollCallBean;
import me.lancer.xupt.mvp.rollcall.RollCallPresenter;
import me.lancer.xupt.ui.adapter.StatisticAdapter;
import me.lancer.xupt.ui.application.ApplicationInstance;

public class StatisticFragment extends PresenterFragment<RollCallPresenter> implements IRollCallView {

    ApplicationInstance app = new ApplicationInstance();

    SwipeRefreshLayout srlStatistic;
    RecyclerView rvStatistic;
//    ProgressDialog pdLogin;

    List<RollCallBean> statisticList = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    pdLogin.show();
                    break;
                case 1:
//                    pdLogin.dismiss();
                    break;
                case 2:
                    Log.e(getString(R.string.log), (String) msg.obj);
                    break;
                case 3:
                    statisticList = (List<RollCallBean>) msg.obj;
                    if (msg.obj != null) {
                        StatisticAdapter adapter = new StatisticAdapter(statisticList);
                        rvStatistic.setAdapter(adapter);
                    } else {
//                        pdLogin.dismiss();
                    }
                    srlStatistic.setRefreshing(false);
                    break;
            }
        }
    };

    Runnable getStatistic = new Runnable() {
        @Override
        public void run() {
            presenter.getStatistic(app.getCardCookie0(), app.getCardCookie1());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        srlStatistic = (SwipeRefreshLayout) view.findViewById(R.id.srl_statistic);
        srlStatistic.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        srlStatistic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlStatistic.setRefreshing(true);
                new Thread(getStatistic).start();
            }
        });
//        srlStatistic.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        rvStatistic = (RecyclerView) view.findViewById(R.id.rv_statistic);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvStatistic.setLayoutManager(llm);
        rvStatistic.setItemAnimator(new DefaultItemAnimator());
        rvStatistic.setHasFixedSize(true);
        StatisticAdapter adapter = new StatisticAdapter(statisticList);
        rvStatistic.setAdapter(adapter);
        srlStatistic.setRefreshing(true);
//        pdLogin = new ProgressDialog(getActivity());
//        pdLogin.setMessage(getString(R.string.statistic_loading));
//        pdLogin.setCancelable(false);
    }

    private void initData() {
        app = (ApplicationInstance) getActivity().getApplication();
        if (app.isRollcall()) {
            new Thread(getStatistic).start();
        }
    }

    @Override
    protected RollCallPresenter onCreatePresenter() {
        return new RollCallPresenter(StatisticFragment.this);
    }

    @Override
    public void showDetail(List<RollCallBean> list) {

    }

    @Override
    public void showStatistic(List<RollCallBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void appeal(String log) {

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
