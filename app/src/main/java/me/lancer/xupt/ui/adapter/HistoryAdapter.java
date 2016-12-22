package me.lancer.xupt.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.book.BookBean;
import me.lancer.xupt.ui.activity.BookDetailActivity;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<BookBean> bookList;

    public HistoryAdapter(List<BookBean> bookList) {
        this.bookList = bookList;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);
        return new HistoryAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder viewHolder, int position) {
        if (bookList.get(position) != null) {
            viewHolder.tvName.setText(bookList.get(position).getBookMainTitle());
            String info = "";
            String[] date = bookList.get(position).getBookDate().split("-");
            info += "于" + date[0] + "年" + date[1] + "月" + date[2] + "日" + bookList.get(position).getBookState();
            viewHolder.tvInfo.setText(info);
        }
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvInfo;

        public ViewHolder(View rootView) {
            super(rootView);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
            tvInfo = (TextView) rootView.findViewById(R.id.tv_info);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("key", 1);
                    intent.putExtra("value", bookList.get(getPosition()).getBookBarCode());
                    intent.setClass(v.getContext(), BookDetailActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
