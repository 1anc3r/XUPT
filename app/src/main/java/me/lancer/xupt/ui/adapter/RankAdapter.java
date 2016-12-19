package me.lancer.xupt.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.book.BookBean;
import me.lancer.xupt.mvp.score.ScoreBean;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<BookBean> bookList;
    private String type;

    public RankAdapter(List<BookBean> bookList, String type) {
        this.bookList = bookList;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rank_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (bookList.get(position) != null) {
            viewHolder.tvName.setText(bookList.get(position).getBookMainTitle());
            String bornum = "";
            switch (type) {
                case "1":
                    bornum = " 借阅量:" + bookList.get(position).getBookBorNum();
                    break;
                case "2":
                    bornum = " 检索量:" + bookList.get(position).getBookBorNum();
                    break;
                case "3":
                    bornum = " 收藏量:" + bookList.get(position).getBookBorNum();
                    break;
                case "4":
                    bornum = " 书评量:" + bookList.get(position).getBookBorNum();
                    break;
                case "5":
                    bornum = " 查看量:" + bookList.get(position).getBookBorNum();
                    break;
            }
            viewHolder.tvBorNum.setText(bornum);
        }
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvBorNum;

        public ViewHolder(View rootView) {
            super(rootView);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
            tvBorNum = (TextView) rootView.findViewById(R.id.tv_bor_num);
        }
    }
}
