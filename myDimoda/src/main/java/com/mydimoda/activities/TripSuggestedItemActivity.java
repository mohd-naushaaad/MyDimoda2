package com.mydimoda.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mydimoda.R;
import com.mydimoda.adapter.SuggestedItemAdp;
import com.mydimoda.model.SuggestedModel;
import com.mydimoda.widget.cropper.util.FontsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Parth on 2/12/2018.
 */

public class TripSuggestedItemActivity extends AppCompatActivity implements SuggestedItemAdp.suggestedItemClickListner {
    @BindView(R.id.back_txt)
    TextView backTxt;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title_view)
    TextView titleView;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.rv_items)
    RecyclerView rvItems;
    private SuggestedItemAdp suggestedItemAdp;
    private List<SuggestedModel> list;
    private int lastSelectedPos = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchandise);
        ButterKnife.bind(this);
        FontsUtil.setExistenceLight(this, titleView);
        FontsUtil.setExistenceLight(this, backTxt);
        setUpAdp();
        makeStaticList(15);

    }

    private void makeStaticList(int i) {
        for (int j = 0; j < i; j++) {
            list.add(new SuggestedModel());
        }
        suggestedItemAdp.notifyDataSetChanged();
    }

    private void setUpAdp() {
        list = new ArrayList();
        suggestedItemAdp = new SuggestedItemAdp(this, list, this);
        rvItems.setLayoutManager(new GridLayoutManager(this, 3));
        rvItems.setAdapter(suggestedItemAdp);
    }

    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onClickofItem(int pos) {
        refreshViewItem(pos);
    }

    private void refreshViewItem(int pos) {
        list.get(pos).setSelected(!list.get(pos).isSelected());
        suggestedItemAdp.notifyItemChanged(pos);
        if (list.get(pos).isSelected()) {
            if (lastSelectedPos != -1) {
                list.get(lastSelectedPos).setSelected(!list.get(lastSelectedPos).isSelected());
                suggestedItemAdp.notifyItemChanged(lastSelectedPos);
            }
            lastSelectedPos = pos;
        } else {
            lastSelectedPos = -1;
        }
    }
}
