package com.example.daidaijie.syllabusapplication.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Dishes;
import com.example.daidaijie.syllabusapplication.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/9/23.
 */

public class BuyPopWindow extends Dialog {


    private View mView;

    private Context mContext;

    private View mEmptyView;

    private RecyclerView mBuyRecyclerView;

    private Map<Dishes, Integer> mDishesMap;

    private BuyAdatper mBuyAdatper;

    public BuyPopWindow(Context context, Map<Dishes, Integer> map) {
        super(context, R.style.dialog);
        mContext = context;
        mDishesMap = map;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_buy_takeout);

        initWindow();

        mBuyRecyclerView = (RecyclerView) findViewById(R.id.buyRecyclerView);
        mBuyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBuyAdatper = new BuyAdatper(mContext, mDishesMap);
        mBuyRecyclerView.setAdapter(mBuyAdatper);
    }

    private void initWindow() {
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        this.setCanceledOnTouchOutside(true);

    }

    public static class BuyAdatper extends RecyclerView.Adapter<BuyAdatper.ViewHolder> {

        Context mContext;

        List<Dishes> mDishesList;

        private Map<Dishes, Integer> mDishesMap;

        public BuyAdatper(Context context, Map<Dishes, Integer> map) {
            mContext = context;
            mDishesMap = map;
            mDishesList = new ArrayList<>();
            mDishesList.addAll(mDishesMap.keySet());
        }

        public Map<Dishes, Integer> getDishesMap() {
            return mDishesMap;
        }

        public void setDishesMap(Map<Dishes, Integer> dishesMap) {
            mDishesMap = dishesMap;
            mDishesList = new ArrayList<>();
            mDishesList.addAll(mDishesMap.keySet());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_buy_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Dishes dishes = mDishesList.get(position);

            int num = mDishesMap.get(dishes);

            holder.mDishesNameTextView.setText(dishes.getName());
            if (StringUtil.isNumberic(dishes.getPrice())) {
                holder.mDishesPriceTextView.setText((Integer.parseInt(dishes.getPrice()) * num) + "");
            } else {
                holder.mDishesPriceTextView.setText(num + "×" + dishes.getPrice());
            }
            holder.mBuyNumTextView.setText(num + "");
        }

        @Override
        public int getItemCount() {
            if (mDishesList == null) return 0;
            return mDishesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.dishesNameTextView)
            TextView mDishesNameTextView;
            @BindView(R.id.dishesPriceTextView)
            TextView mDishesPriceTextView;
            @BindView(R.id.minusButton)
            ImageButton mMinusButton;
            @BindView(R.id.buyNumTextView)
            TextView mBuyNumTextView;
            @BindView(R.id.addButton)
            ImageButton mAddButton;
            @BindView(R.id.div_line)
            View mDivLine;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


}
