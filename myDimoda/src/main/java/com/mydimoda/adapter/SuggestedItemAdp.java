package com.mydimoda.adapter;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mydimoda.R;
import com.mydimoda.customView.SqureImageView;
import com.mydimoda.model.SuggestedModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/12/2018.
 */

public class SuggestedItemAdp extends RecyclerView.Adapter<SuggestedItemAdp.SuggestedItemHolder> {
    public interface suggestedItemClickListner {
        void onClickofItem(int pos);
    }

    private Context mContext;
    private List<SuggestedModel> list = new ArrayList();
    private suggestedItemClickListner suggestedItemClickListner;

    public SuggestedItemAdp(Context mContext, List list, suggestedItemClickListner suggestedItemClickListner) {
        this.mContext = mContext;
        this.list = list;
        this.suggestedItemClickListner = suggestedItemClickListner;
    }

    @Override
    public SuggestedItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_merchandise, parent, false);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        params.height = parent.getMeasuredHeight() / 3;
        view.setLayoutParams(params);
        return new SuggestedItemHolder(view);
    }

    @Override
    public void onBindViewHolder(SuggestedItemHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class SuggestedItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_main)
        SqureImageView imgMain;
        @BindView(R.id.img_back)
        SqureImageView imgBack;
        @BindView(R.id.rl_suggested_item)
        RelativeLayout rlSuggestedItem;

        @OnClick(R.id.rl_suggested_item)
        public void onViewClicked() {
            suggestedItemClickListner.onClickofItem(getAdapterPosition());
        }

        public SuggestedItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setData(int position) {
            if (list.get(position).isSelected()) {
                imgBack.setVisibility(View.VISIBLE);
                imgBack.setImageDrawable(mContext.getDrawable(R.drawable.over_icon));
            } else {
                imgBack.setVisibility(View.GONE);
            }
        }
    }
}
