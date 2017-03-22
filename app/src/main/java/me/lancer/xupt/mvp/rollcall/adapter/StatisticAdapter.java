package me.lancer.xupt.mvp.rollcall.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.rollcall.RollCallBean;

/**
 * Created by HuangFangzhi on 2016/12/20.
 */

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {

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

    private List<RollCallBean> statisticList;

    public StatisticAdapter(List<RollCallBean> statisticList) {
        this.statisticList = statisticList;
    }

    @Override
    public StatisticAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statistic_item, viewGroup, false);
        return new StatisticAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatisticAdapter.ViewHolder viewHolder, int position) {
        if (statisticList.get(position) != null) {
            viewHolder.tvName.setText(statisticList.get(position).getRollCallCourseName());

            int total = statisticList.get(position).getRollCallTotal();
            int attend = statisticList.get(position).getRollCallAttend();
            int absence = statisticList.get(position).getRollCallAbsence();
            int late = statisticList.get(position).getRollCallLate();
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry((float) (attend * 1.0 / total), "正常:" + attend));
            entries.add(new PieEntry((float) (late * 1.0 / total), "迟到:" + late));
            entries.add(new PieEntry((float) (absence * 1.0 / total), "缺勤:" + absence));
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(5f);
            dataSet.setSelectionShift(5f);
            dataSet.setValueLinePart1OffsetPercentage(80.f);
            dataSet.setValueLinePart1Length(0.25f);
            dataSet.setValueLinePart2Length(0.75f);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            ArrayList<Integer> colors = new ArrayList<>();
            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);
            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(12f);
            Legend l = viewHolder.pcStatistic.getLegend();
            l.setDrawInside(false);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setXEntrySpace(10f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            viewHolder.pcStatistic.animateY(1200, Easing.EasingOption.EaseInOutQuad);
            viewHolder.pcStatistic.setCenterText(statisticList.get(position).getRollCallCourseName());
            viewHolder.pcStatistic.setCenterTextSize(10f);
            viewHolder.pcStatistic.setData(data);
            viewHolder.pcStatistic.getDescription().setEnabled(false);
            viewHolder.pcStatistic.setDrawCenterText(true);
            viewHolder.pcStatistic.setDragDecelerationFrictionCoef(0.75f);
            viewHolder.pcStatistic.setDrawHoleEnabled(true);
            viewHolder.pcStatistic.setEntryLabelColor(Color.TRANSPARENT);
            viewHolder.pcStatistic.setEntryLabelTextSize(10f);
            viewHolder.pcStatistic.setHighlightPerTapEnabled(true);
            viewHolder.pcStatistic.setHoleColor(Color.TRANSPARENT);
            viewHolder.pcStatistic.setHoleRadius(45f);
            viewHolder.pcStatistic.setRotationAngle(45f);
            viewHolder.pcStatistic.setRotationEnabled(true);
            viewHolder.pcStatistic.setTransparentCircleAlpha(100);
            viewHolder.pcStatistic.setTransparentCircleColor(Color.WHITE);
            viewHolder.pcStatistic.setTransparentCircleRadius(50f);
            viewHolder.pcStatistic.setUsePercentValues(true);
            viewHolder.pcStatistic.invalidate();
        }
    }

    @Override
    public int getItemCount() {
        return statisticList == null ? 0 : statisticList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public PieChart pcStatistic;

        public ViewHolder(View rootView) {
            super(rootView);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
            pcStatistic = (PieChart) rootView.findViewById(R.id.pc_statistic);
        }
    }
}
