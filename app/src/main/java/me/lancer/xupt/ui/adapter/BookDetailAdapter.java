package me.lancer.xupt.ui.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.rollcall.RollCallBean;
import me.lancer.xupt.ui.application.ApplicationInstance;
import me.lancer.xupt.ui.fragment.DetailFragment;
import me.lancer.xupt.ui.view.ClearEditText;

/**
 * Created by HuangFangzhi on 2016/12/20.
 */

public class BookDetailAdapter extends RecyclerView.Adapter<BookDetailAdapter.ViewHolder> {

    private List<RollCallBean> detailList;
    private DetailFragment context;

    AlertDialog appealDialog;
    private RollCallBean rollcall;
    private String remark = "", state = "1";

    public BookDetailAdapter(DetailFragment context, List<RollCallBean> detailList) {
        this.context = context;
        this.detailList = detailList;
    }

    @Override
    public BookDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detail_item, viewGroup, false);
        return new BookDetailAdapter.ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(BookDetailAdapter.ViewHolder viewHolder, int position) {
        if (detailList.get(position) != null) {
            rollcall = detailList.get(position);
            viewHolder.tvName.setText(detailList.get(position).getRollCallCourseName());
            viewHolder.tvTime.setText(detailList.get(position).getRollCallTime() + "节");
            String termRaw = detailList.get(position).getRollCallTerm();
            String[] terms = termRaw.split("-");
            viewHolder.tvTerm.setText(terms[0] + "-" + terms[1] + "学年" + terms[2] + "学期");
            String dateRaw = detailList.get(position).getRollCallDate();
            String[] dates = dateRaw.split("-");
            viewHolder.tvDate.setText(dates[0] + "年" + dates[1] + "月" + dates[2] + "日");
            viewHolder.tvBuild.setText(detailList.get(position).getRollCallBuild());
            viewHolder.tvRoom.setText(detailList.get(position).getRollCallRoom());
            if (detailList.get(position).getRollCallState() == 1) {
                viewHolder.btnAppeal.setVisibility(View.GONE);
                viewHolder.btnState.setText("正常");
                viewHolder.btnState.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
            } else if (detailList.get(position).getRollCallState() == 2) {
                viewHolder.btnAppeal.setVisibility(View.GONE);
                viewHolder.btnState.setText("迟到");
                viewHolder.btnState.setBackgroundTintList(context.getResources().getColorStateList(R.color.yellow));
            } else if (detailList.get(position).getRollCallState() == 3) {
                viewHolder.btnState.setText("缺勤");
                viewHolder.btnState.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
                if (!CompareDate(detailList.get(position).getRollCallDate())
                        && detailList.get(position).getRollCallState() == 3) {
                    viewHolder.btnAppeal.setVisibility(View.VISIBLE);
                    viewHolder.btnAppeal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAppealDialog(rollcall);
                        }
                    });
                }
            } else {
                viewHolder.btnAppeal.setVisibility(View.GONE);
                viewHolder.btnState.setText("待签");
                viewHolder.btnState.setBackgroundTintList(context.getResources().getColorStateList(R.color.yellow));
            }
        }
    }

    private boolean CompareDate(String timeRaw) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, -7);
            Date line = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date ball = format.parse(timeRaw);
            if (line.getTime() > ball.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private void showAppealDialog(final RollCallBean rollcall) {
        View appealDialogView = LayoutInflater.from(context.getActivity()).inflate(R.layout.appeal_dialog, null);
        TextView tvName = (TextView) appealDialogView.findViewById(R.id.tv_name);
        tvName.setText(rollcall.getRollCallCourseName());
        TextView tvDate = (TextView) appealDialogView.findViewById(R.id.tv_date);
        tvDate.setText(rollcall.getRollCallDate());
        RadioGroup rgState = (RadioGroup) appealDialogView.findViewById(R.id.rg_state);
        rgState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_attend) {
                    state = "1";
                } else if (checkedId == R.id.rb_late) {
                    state = "2";
                } else if (checkedId == R.id.rb_leave) {
                    state = "3";
                }
            }
        });
        final ClearEditText cetRemark = (ClearEditText) appealDialogView.findViewById(R.id.cet_remark);
        Button btnAppeal = (Button) appealDialogView.findViewById(R.id.btn_appeal);
        btnAppeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        remark = cetRemark.getText().toString();
                        context.appea1(rollcall, remark, state,
                                ((ApplicationInstance) context.getActivity().getApplication()).getCardCookie0(),
                                ((ApplicationInstance) context.getActivity().getApplication()).getCardCookie1());
                        appealDialog.dismiss();
                    }
                }).start();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
        builder.setView(appealDialogView);
        appealDialog = builder.create();
        appealDialog.show();
    }

    @Override
    public int getItemCount() {
        return detailList == null ? 0 : detailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTerm, tvDate, tvName, tvTime, tvRoom, tvBuild;
        public Button btnState, btnAppeal;

        public ViewHolder(View rootView) {
            super(rootView);
            tvTerm = (TextView) rootView.findViewById(R.id.tv_term);
            tvDate = (TextView) rootView.findViewById(R.id.tv_date);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
            tvTime = (TextView) rootView.findViewById(R.id.tv_time);
            tvRoom = (TextView) rootView.findViewById(R.id.tv_room);
            tvBuild = (TextView) rootView.findViewById(R.id.tv_build);
            btnState = (Button) rootView.findViewById(R.id.btn_state);
            btnAppeal = (Button) rootView.findViewById(R.id.btn_appeal);
        }
    }
}
