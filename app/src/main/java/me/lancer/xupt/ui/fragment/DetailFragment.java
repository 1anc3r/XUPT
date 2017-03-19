package me.lancer.xupt.ui.fragment;

import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.lancer.xupt.R;

import me.lancer.xupt.mvp.PresenterFragment;
import me.lancer.xupt.mvp.rollcall.IRollCallView;
import me.lancer.xupt.mvp.rollcall.RollCallBean;
import me.lancer.xupt.mvp.rollcall.RollCallPresenter;
import me.lancer.xupt.ui.adapter.BookDetailAdapter;
import me.lancer.xupt.ui.application.ApplicationInstance;

public class DetailFragment extends PresenterFragment<RollCallPresenter> implements IRollCallView {

    ApplicationInstance app = new ApplicationInstance();

    LinearLayout llDetail;
    TextView tvStart, tvEnd;
    RadioGroup rgTime;
    RadioButton rbDay, rbWeek, rbMonth;
    CheckBox cbAttend, cbLate, cbAbsence;
    SwipeRefreshLayout srlDetail;
    RecyclerView rvDetail;
//    ProgressDialog pdLogin;

    List<RollCallBean> detailList = new ArrayList<>();

    String start = "", end = "", status = "1", flag = "1a2a3", page = "1";

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

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
                    detailList = (List<RollCallBean>) msg.obj;
                    if (detailList!=null) {
                        BookDetailAdapter adapter = new BookDetailAdapter(DetailFragment.this, detailList);
                        rvDetail.setAdapter(adapter);
                        srlDetail.setRefreshing(false);
                    }else {
//                        pdLogin.dismiss();
                    }
                    break;
                case 4:
                    Log.e(getString(R.string.log), (String) msg.obj);
                    showSnackbar(llDetail, (String) msg.obj);
                    break;
            }
        }
    };

    Runnable getDetail = new Runnable() {
        @Override
        public void run() {
            presenter.getDetail(start, end, status, flag, page, app.getCardCookie0(), app.getCardCookie1());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        llDetail = (LinearLayout) view.findViewById(R.id.ll_detail);
        tvStart = (TextView) view.findViewById(R.id.tv_start);
        tvStart.setOnClickListener(vOnClickListener);
        tvEnd = (TextView) view.findViewById(R.id.tv_end);
        tvEnd.setOnClickListener(vOnClickListener);
        rgTime = (RadioGroup) view.findViewById(R.id.rg_time);
        rgTime.setOnCheckedChangeListener(rOnCheckedChangeListener);
        rbDay = (RadioButton) view.findViewById(R.id.rb_day);
        rbWeek = (RadioButton) view.findViewById(R.id.rb_week);
        rbMonth = (RadioButton) view.findViewById(R.id.rb_month);
        cbAttend = (CheckBox) view.findViewById(R.id.cb_attend);
        cbAttend.setOnCheckedChangeListener(cOnCheckChangeListner);
        cbLate = (CheckBox) view.findViewById(R.id.cb_late);
        cbLate.setOnCheckedChangeListener(cOnCheckChangeListner);
        cbAbsence = (CheckBox) view.findViewById(R.id.cb_absence);
        cbAbsence.setOnCheckedChangeListener(cOnCheckChangeListner);
        srlDetail = (SwipeRefreshLayout) view.findViewById(R.id.srl_detail);
        srlDetail.setColorSchemeResources(R.color.blue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.red, R.color.pink, R.color.purple);
        srlDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlDetail.setRefreshing(true);
                new Thread(getDetail).start();
            }
        });
//        srlDetail.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        rvDetail = (RecyclerView) view.findViewById(R.id.rv_detail);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvDetail.setLayoutManager(llm);
        rvDetail.setItemAnimator(new DefaultItemAnimator());
        rvDetail.setHasFixedSize(true);
        BookDetailAdapter adapter = new BookDetailAdapter(this, detailList);
        rvDetail.setAdapter(adapter);
        srlDetail.setRefreshing(true);
//        pdLogin = new ProgressDialog(getActivity());
//        pdLogin.setMessage(getString(R.string.detail_loading));
//        pdLogin.setCancelable(false);
    }

    private void initData() {
        app = (ApplicationInstance) getActivity().getApplication();
        start = end = format.format(calendar.getTime());
        tvStart.setText(start);
        tvEnd.setText(end);
        if (app.isRollcall()) {
            new Thread(getDetail).start();
        }
    }

    private View.OnClickListener vOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == tvStart) {
                try {
                    calendar.setTime(format.parse(start));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        start = format.format(calendar.getTime());
                        tvStart.setText(start);
                        new Thread(getDetail).start();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
            } else if (v == tvEnd) {
                try {
                    calendar.setTime(format.parse(end));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        end = format.format(calendar.getTime());
                        tvEnd.setText(end);
                        new Thread(getDetail).start();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener rOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.rb_day && rbDay.isChecked()) {
                calendar.setTime(new Date());
                start = end = format.format(calendar.getTime());
                tvStart.setText(start);
                tvEnd.setText(end);
                status = "1";
                new Thread(getDetail).start();
            } else if (checkedId == R.id.rb_week && rbWeek.isChecked()) {
                calendar.setTime(new Date());
                end = format.format(calendar.getTime());
                calendar.add(Calendar.DATE, -7);
                start = format.format(calendar.getTime());
                tvStart.setText(start);
                tvEnd.setText(end);
                status = "2";
                new Thread(getDetail).start();
            } else if (checkedId == R.id.rb_month && rbMonth.isChecked()) {
                calendar.setTime(new Date());
                end = format.format(calendar.getTime());
                calendar.add(Calendar.MONTH, -1);
                start = format.format(calendar.getTime());
                tvStart.setText(start);
                tvEnd.setText(end);
                status = "3";
                new Thread(getDetail).start();
            }
        }
    };

    private CheckBox.OnCheckedChangeListener cOnCheckChangeListner = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getFlag();
            new Thread(getDetail).start();
        }
    };

    private void getFlag() {
        flag = "";
        if (cbAttend.isChecked()) {
            flag += "1";
        }
        if (cbLate.isChecked()) {
            if (flag.length() == 0) {
                flag += "2";
            } else {
                flag += "a2";
            }
        }
        if (cbAbsence.isChecked()) {
            if (flag.length() == 0) {
                flag += "3";
            } else {
                flag += "a3";
            }
        }
    }

    public void appea1(RollCallBean rollcall, String remark, String state, String cookie0, String cookie1) {
        presenter.appeal(rollcall, remark, state, cookie0, cookie1);
    }

    @Override
    protected RollCallPresenter onCreatePresenter() {
        return new RollCallPresenter(DetailFragment.this);
    }

    @Override
    public void showDetail(List<RollCallBean> list) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    @Override
    public void showStatistic(List<RollCallBean> list) {

    }

    @Override
    public void appeal(String log) {
        Message msg = new Message();
        msg.what = 4;
        msg.obj = log;
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
