package me.lancer.xupt.mvp.reviewer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.lancer.xupt.R;
import me.lancer.xupt.mvp.reviewer.bean.ReviewerBean;
import me.lancer.xupt.mvp.reviewer.activity.ReviewerDetailActivity;

public class ReviewerAdapter extends RecyclerView.Adapter<ReviewerAdapter.ViewHolder> {

    private List<ReviewerBean> reviewerList;
    private RequestQueue mQueue;

    public ReviewerAdapter(Context context, List<ReviewerBean> reviewerList) {
        this.reviewerList = reviewerList;
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reviewer_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        if (reviewerList.get(position) != null) {
            viewHolder.tvTitle.setText(reviewerList.get(position).getTitle());
            viewHolder.tvContent.setText(reviewerList.get(position).getContent());
            ImageRequest imageRequest = new ImageRequest(
                    reviewerList.get(position).getImg(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            viewHolder.ivImg.setImageBitmap(response);
                        }
                    }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    viewHolder.ivImg.setImageResource(R.mipmap.ic_pictures_no);
                }
            });
            mQueue.add(imageRequest);
        }
    }

    @Override
    public int getItemCount() {
        return reviewerList == null ? 0 : reviewerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImg;
        public TextView tvTitle, tvContent;

        public ViewHolder(View rootView) {
            super(rootView);
            ivImg = (ImageView) rootView.findViewById(R.id.iv_img);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvContent = (TextView) rootView.findViewById(R.id.tv_content);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("title", reviewerList.get(getPosition()).getTitle());
                    intent.putExtra("url", reviewerList.get(getPosition()).getHref());
                    intent.putExtra("img", reviewerList.get(getPosition()).getImg());
                    intent.setClass(v.getContext(), ReviewerDetailActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
