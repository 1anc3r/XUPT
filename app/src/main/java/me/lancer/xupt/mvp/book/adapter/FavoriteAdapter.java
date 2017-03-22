package me.lancer.xupt.mvp.book.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.book.BookBean;
import me.lancer.xupt.mvp.book.activity.BookDetailActivity;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<BookBean> bookList;

    public FavoriteAdapter(List<BookBean> bookList) {
        this.bookList = bookList;
    }

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favorite_item, viewGroup, false);
        return new FavoriteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.ViewHolder viewHolder, int position) {
        if (bookList.get(position) != null) {
            viewHolder.tvName.setText(bookList.get(position).getBookMainTitle());
            viewHolder.tvAuthor.setText(bookList.get(position).getBookAuthor());
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
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("key", 0);
                    intent.putExtra("value", bookList.get(getPosition()).getBookId());
                    intent.setClass(v.getContext(), BookDetailActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
