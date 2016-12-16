package me.lancer.xupt.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.score.ScoreBean;
import me.lancer.xupt.ui.view.cardstackview.CardStackView;
import me.lancer.xupt.ui.view.cardstackview.StackAdapter;

public class TermAdapter extends StackAdapter<Integer> {

    private static List<String> termList;
    private static Map<String, List<ScoreBean>> termMap;

    public TermAdapter(Context context, List<String> termList, Map<String, List<ScoreBean>> termMap) {
        super(context);
        this.termList = termList;
        this.termMap = termMap;
    }

    @Override
    public void bindView(Integer data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof ColorItemViewHolder) {
            ColorItemViewHolder h = (ColorItemViewHolder) holder;
            h.onBind(data, position);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        if (viewType == R.layout.term_item) {
                view = getLayoutInflater().inflate(R.layout.term_item, parent, false);
                return new ColorItemViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
            return R.layout.term_item;
    }

    static class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;
        RecyclerView rvSocre;

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            rvSocre = (RecyclerView) view.findViewById(R.id.rv_score);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            rvSocre.setLayoutManager(llm);
            rvSocre.setItemAnimator(new DefaultItemAnimator());
            rvSocre.setHasFixedSize(true);

        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(Integer data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(termList.get(position));
            ScoreAdapter adapter = new ScoreAdapter(termMap.get(termList.get(position)));
            rvSocre.setAdapter(adapter);
        }
    }
}
