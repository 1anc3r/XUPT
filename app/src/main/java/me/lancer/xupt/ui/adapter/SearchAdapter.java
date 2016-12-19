package me.lancer.xupt.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.book.BookBean;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<BookBean> bookList;

    public SearchAdapter(List<BookBean> bookList) {
        this.bookList = bookList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (bookList.get(position) != null) {
            viewHolder.tvName.setText(bookList.get(position).getBookMainTitle());
            if (bookList.get(position).getBookAuthor().length() > 0) {
                viewHolder.tvAuthor.setText(bookList.get(position).getBookAuthor());
            }
        }
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvAuthor;

        public ViewHolder(View rootView) {
            super(rootView);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
            tvAuthor = (TextView) rootView.findViewById(R.id.tv_author);
        }
    }
}
