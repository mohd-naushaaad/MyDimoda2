package com.mydimoda.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mydimoda.R;
import com.mydimoda.model.DemoModelForLook;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/13/2018.
 */

public class LookAdapter extends RecyclerView.Adapter<LookAdapter.LookHolder> {
    public interface ClickListnerOfLook {
        void onClickOfLike(int pos);

        void onClickofClose(int pos);
    }

    private ClickListnerOfLook listnerOfLook;
    private List<DemoModelForLook> lookList = new ArrayList<>();
    private LookImagesAdp lookImagesAdp;
    private Context mContext;

    public LookAdapter(List<DemoModelForLook> lookList, Context mContext, ClickListnerOfLook listnerOfLook) {
        this.lookList = lookList;
        this.mContext = mContext;
        this.listnerOfLook = listnerOfLook;
    }

    @Override
    public LookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_looks, parent, false);
        return new LookHolder(view);
    }

    @Override
    public void onBindViewHolder(LookHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return lookList.size();
    }

    public class LookHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_img_look)
        RecyclerView rvImgLook;
        /*@BindView(R.id.iv_like)
        ImageView ivLike;
        @BindView(R.id.iv_close)
        ImageView ivClose;*/

        /*@OnClick({R.id.iv_like, R.id.iv_close})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.iv_like:
                    listnerOfLook.onClickOfLike(getAdapterPosition());
                    break;
                case R.id.iv_close:
                    listnerOfLook.onClickofClose(getAdapterPosition());
                    break;
            }
        }*/

        public LookHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            lookImagesAdp = new LookImagesAdp(lookList.get(position).getList());
            rvImgLook.setLayoutManager(new GridLayoutManager(mContext, 2));
            rvImgLook.setAdapter(lookImagesAdp);
//            ivLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.like_btn_sel));
//            ivClose.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.dismiss_btn_sel));
            /*if (lookList.get(position).isLiked()) {
                ivLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.like_fill));
            } else {
                ivLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.like));
            }
            if (lookList.get(position).isColsed()) {
                ivClose.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.close_fill));
            } else {
                ivClose.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.close));
            }*/
        }
    }
}
