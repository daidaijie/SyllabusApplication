package com.example.daidaijie.syllabusapplication.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.example.daidaijie.syllabusapplication.bean.TakeOutBuyBean;
import com.example.daidaijie.syllabusapplication.util.StringUtil;
import com.orhanobut.logger.Logger;

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

    private TakeOutBuyBean mTakeOutBuyBean;

    private BuyAdatper mBuyAdatper;

    private TextView clearBuyTextView;

    public BuyPopWindow(Context context, TakeOutBuyBean been) {
        super(context, R.style.dialog);
        mContext = context;
        mTakeOutBuyBean = been;

    }

    public interface OnDataChangeListener {
        void onChange(int position);

        void onChangeAll();
    }

    OnDataChangeListener mOnDataChangeListener;

    public OnDataChangeListener getOnDataChangeListener() {
        return mOnDataChangeListener;
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_buy_takeout);

        initWindow();

        mBuyRecyclerView = (RecyclerView) findViewById(R.id.buyRecyclerView);
        mBuyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBuyAdatper = new BuyAdatper(mContext, mTakeOutBuyBean);
        mBuyRecyclerView.setAdapter(mBuyAdatper);
        mBuyRecyclerView.setItemAnimator(new MyItemAnimator());

        mBuyAdatper.setOnActionChangeListener(new BuyAdatper.OnActionChangeListener() {
            @Override
            public void onAdd(Dishes dishes, int position) {
                mTakeOutBuyBean.addDishes(dishes);
                mBuyAdatper.notifyItemChanged(position);
                if (mOnDataChangeListener != null) {
                    mOnDataChangeListener.onChange(dishes.mPos);
                }
            }

            @Override
            public void onRemove(Dishes dishes, int position) {
                boolean isNone = mTakeOutBuyBean.removeDishes(dishes);
                Logger.t("isNone").e(isNone + "");
                if (isNone) {
                    mBuyAdatper.notifyItemRemoved(position);
                    mBuyAdatper.notifyDataSetChanged();
                } else {
                    mBuyAdatper.notifyItemChanged(position);
                }

                if (mOnDataChangeListener != null) {
                    mOnDataChangeListener.onChange(dishes.mPos);
                }
            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setMessage("确定清空口袋?")
                .setNegativeButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTakeOutBuyBean.clear();
                        if (mOnDataChangeListener!=null){
                            mOnDataChangeListener.onChangeAll();
                        }
                        dialog.dismiss();
                        BuyPopWindow.this.dismiss();
                    }
                }).setPositiveButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        clearBuyTextView = (TextView) findViewById(R.id.clearBuyTextView);
        clearBuyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
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

        private TakeOutBuyBean mTakeOutBuyBean;

        public interface OnActionChangeListener {
            void onAdd(Dishes dishes, int position);

            void onRemove(Dishes dishes, int position);
        }

        private OnActionChangeListener mOnActionChangeListener;

        public OnActionChangeListener getOnActionChangeListener() {
            return mOnActionChangeListener;
        }

        public void setOnActionChangeListener(OnActionChangeListener onActionChangeListener) {
            mOnActionChangeListener = onActionChangeListener;
        }

        public BuyAdatper(Context context, TakeOutBuyBean bean) {
            mContext = context;
            mTakeOutBuyBean = bean;
        }

        public TakeOutBuyBean getTakeOutBuyBean() {
            return mTakeOutBuyBean;
        }

        public void setTakeOutBuyBean(TakeOutBuyBean takeOutBuyBean) {
            mTakeOutBuyBean = takeOutBuyBean;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_buy_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Dishes dishes = mTakeOutBuyBean.getDishesList().get(position);

            int num = mTakeOutBuyBean.getBuyMap().get(dishes);

            holder.mDishesNameTextView.setText(dishes.getName());
            if (StringUtil.isNumberic(dishes.getPrice())) {
                holder.mDishesPriceTextView.setText((Integer.parseInt(dishes.getPrice()) * num) + "");
            } else {
                holder.mDishesPriceTextView.setText(num + "×" + dishes.getPrice());
            }
            holder.mBuyNumTextView.setText(num + "");

            holder.mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnActionChangeListener != null) {
                        mOnActionChangeListener.onAdd(dishes, position);
                    }
                }
            });

            holder.mMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnActionChangeListener != null) {
                        mOnActionChangeListener.onRemove(dishes, position);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            if (mTakeOutBuyBean.getDishesList() == null) return 0;
            return mTakeOutBuyBean.getDishesList().size();
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
