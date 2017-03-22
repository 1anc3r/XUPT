package me.lancer.xupt.mvp.book.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.book.BookBean;

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.ViewHolder> {

    private List<BookBean> bookList;

    public CircleAdapter(List<BookBean> bookList) {
        this.bookList = bookList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.circle_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (bookList.get(position) != null) {
            viewHolder.tvSort.setText(bookList.get(position).getBookSort());
            viewHolder.tvDepartment.setText(bookList.get(position).getBookDepartment());
            viewHolder.tvState.setText(bookList.get(position).getBookState());
        }
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvSort, tvDepartment, tvState;

        public ViewHolder(View rootView) {
            super(rootView);
            tvSort = (TextView) rootView.findViewById(R.id.tv_sort);
            tvDepartment = (TextView) rootView.findViewById(R.id.tv_department);
            tvState = (TextView) rootView.findViewById(R.id.tv_state);
        }
    }
}
