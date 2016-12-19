package me.lancer.xupt.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.lancer.xupt.R;

public class StrAdapter extends RecyclerView.Adapter<StrAdapter.ViewHolder> {

    private List<String> strList;

    public StrAdapter(List<String> strList) {
        this.strList = strList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.str_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (strList.get(position) != null) {
            viewHolder.tvName.setText(strList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return strList == null ? 0 : strList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;

        public ViewHolder(View rootView) {
            super(rootView);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
        }
    }
}
