package me.lancer.xupt.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.score.ScoreBean;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private List<ScoreBean> scoreList;

    public ScoreAdapter(List<ScoreBean> scoreList) {
        this.scoreList = scoreList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.score_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tvName.setText(scoreList.get(position).getScoreName());
        viewHolder.tvProperty.setText("性质: " + scoreList.get(position).getScoreProperty());
        viewHolder.tvValue.setText("成绩: " + scoreList.get(position).getScoreValue());
        viewHolder.tvCredit.setText("学分: " + scoreList.get(position).getScoreCredit());
        viewHolder.tvGPA.setText("绩点: " + scoreList.get(position).getScoreGPA());
    }

    @Override
    public int getItemCount() {
        return scoreList == null ? 0 : scoreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvProperty, tvValue, tvCredit, tvGPA;

        public ViewHolder(View rootView) {
            super(rootView);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
            tvProperty = (TextView) rootView.findViewById(R.id.tv_property);
            tvValue = (TextView) rootView.findViewById(R.id.tv_value);
            tvCredit = (TextView) rootView.findViewById(R.id.tv_credit);
            tvGPA = (TextView) rootView.findViewById(R.id.tv_gpa);
        }
    }
}
