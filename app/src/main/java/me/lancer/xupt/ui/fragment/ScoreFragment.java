package me.lancer.xupt.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.score.IScoreView;
import me.lancer.xupt.mvp.score.ScoreBean;
import me.lancer.xupt.mvp.score.ScorePresenter;
import me.lancer.xupt.ui.adapter.TermAdapter;
import me.lancer.xupt.ui.application.ApplicationInstance;
import me.lancer.xupt.ui.view.cardstackview.CardStackView;

public class ScoreFragment extends PresenterFragment<ScorePresenter> implements IScoreView {

    private ApplicationInstance app;

    CardStackView cardStackView;
    TermAdapter cardStackAdapter;
    ProgressDialog pdLogin;

    List<Integer> colorList = new ArrayList<>();
    List<ScoreBean> scoreList = new ArrayList<>();
    List<String> termList = new ArrayList<>();
    Map<String, List<ScoreBean>> termMap = new TreeMap<>();

    public static Integer[] palette = new Integer[]{
            R.color.blue,
            R.color.teal,
            R.color.green,
            R.color.yellow,
            R.color.orange,
            R.color.red,
            R.color.pink,
            R.color.purple,
    };

    String number, name, cookie;

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
                    Log.e("log", (String) msg.obj);
                    break;
                case 3:
                    app.setScore(false);
                    cardStackAdapter = new TermAdapter(getActivity(), termList, termMap);
                    cardStackView.setAdapter(cardStackAdapter);
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    cardStackAdapter.updateData(colorList);
                                }
                            }, 200
                    );
                    break;
            }
        }
    };

    Runnable loadScore = new Runnable() {
        @Override
        public void run() {
            presenter.loadScore(number, name, cookie, app.isScore());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        cardStackView = (CardStackView) view.findViewById(R.id.csv_score);
        cardStackView.setItemExpendListener(new CardStackView.ItemExpendListener() {
            @Override
            public void onItemExpend(boolean expend) {

            }
        });
        cardStackAdapter = new TermAdapter(getActivity(), termList, termMap);
        cardStackView.setAdapter(cardStackAdapter);
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        cardStackAdapter.updateData(colorList);
                    }
                }, 200
        );
        pdLogin = new ProgressDialog(getActivity());
        pdLogin.setMessage("正在加载成绩...");
        pdLogin.setCancelable(false);
    }

    private void initData() {
        app = (ApplicationInstance) getActivity().getApplication();
        number = app.getNumber();
        name = app.getName();
        cookie = app.getEduCookie();
        new Thread(loadScore).start();
    }

    @Override
    protected ScorePresenter onCreatePresenter() {
        return new ScorePresenter(ScoreFragment.this);
    }

    @Override
    public void showScore(List<ScoreBean> list) {
        scoreList = list;
        for (ScoreBean item : scoreList) {
            String termStr = item.getScoreYear() + "学年" + item.getScoreTerm() + "学期";
            if (termMap.get(termStr) == null) {
                List<ScoreBean> itemList = new ArrayList<>();
                itemList.add(item);
                termList.add(termStr);
                termMap.put(termStr, itemList);
            } else {
                termMap.get(termStr).add(item);
            }
        }
        for (int i = 0; i < termMap.size(); i++) {
            colorList.add(palette[i]);
        }
        Message msg = new Message();
        msg.what = 3;
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
