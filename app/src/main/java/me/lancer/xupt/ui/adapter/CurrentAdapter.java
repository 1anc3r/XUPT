package me.lancer.xupt.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.book.BookBean;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.ViewHolder> {

    private List<BookBean> bookList;

    public CurrentAdapter(List<BookBean> bookList) {
        this.bookList = bookList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.current_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (bookList.get(position) != null) {
            if (bookList.get(position).isBookCanRenew()) {
                viewHolder.tvName.setText(bookList.get(position).getBookMainTitle() + "[可续借]");
            }else{
                viewHolder.tvName.setText(bookList.get(position).getBookMainTitle() + "[不可续借]");
            }
            String info = "";
            char[] date = bookList.get(position).getBookDate().toCharArray();
            info += "请于"+date[0]+date[1]+date[2]+date[3]+"年"+date[4]+date[5]+"月"+date[6]+date[7]+"日还书至"+bookList.get(position).getBookDepartment();
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
        }
    }
}
