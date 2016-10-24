package com.example.daidaijie.syllabusapplication.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Dishes;
import com.example.daidaijie.syllabusapplication.bean.TakeOutBuyBean;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;
import com.example.daidaijie.syllabusapplication.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/9/23.
 */

public class BuyPopWindow extends Dialog {


    private Activity mContext;

    private RecyclerView mBuyRecyclerView;

    private TakeOutInfoBean mTakeOutInfoBean;

    private BuyAdatper mBuyAdatper;

    private TextView clearBuyTextView;

    private TextView mBuyNumTextView;
    private TextView mSumPriceTextView;
    private TextView mUnCalcNumTextView;
    private View mDivLine;
    private FrameLayout mRootLayout;
    private Button mSubmitButton;

    public BuyPopWindow(Activity context, TakeOutInfoBean takeOutInfoBean) {
        super(context, R.style.dialog);
        mContext = context;
        mTakeOutInfoBean = takeOutInfoBean;
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
        mBuyAdatper = new BuyAdatper(mContext, mTakeOutInfoBean.getTakeOutBuyBean());
        mBuyRecyclerView.setAdapter(mBuyAdatper);
        mBuyRecyclerView.setItemAnimator(new MyItemAnimator());

        mBuyAdatper.setOnActionChangeListener(new BuyAdatper.OnActionChangeListener() {
            @Override
            public void onAdd(Dishes dishes, int position) {
                mTakeOutInfoBean.getTakeOutBuyBean().addDishes(dishes);
                mBuyAdatper.notifyItemChanged(position);
                showPrice();
                if (mOnDataChangeListener != null) {
                    mOnDataChangeListener.onChange(dishes.mPos);
                }
            }

            @Override
            public void onRemove(Dishes dishes, int position) {
                boolean isNone = mTakeOutInfoBean.getTakeOutBuyBean().removeDishes(dishes);
                if (mOnDataChangeListener != null) {
                    mOnDataChangeListener.onChange(dishes.mPos);
                }
                if (mTakeOutInfoBean.getTakeOutBuyBean().getBuyMap().size() == 0) {
                    BuyPopWindow.this.dismiss();
                    return;
                }
                if (isNone) {
                    mBuyAdatper.notifyDataSetChanged();
                } else {
                    mBuyAdatper.notifyItemChanged(position);
                }

                showPrice();
            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setMessage("确定清空口袋?")
                .setNegativeButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTakeOutInfoBean.getTakeOutBuyBean().clear();
                        if (mOnDataChangeListener != null) {
                            mOnDataChangeListener.onChangeAll();
                        }
                        showPrice();
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

        mBuyNumTextView = (TextView) findViewById(R.id.buyNumTextView);
        mSumPriceTextView = (TextView) findViewById(R.id.sumPriceTextView);
        mUnCalcNumTextView = (TextView) findViewById(R.id.unCalcNumTextView);
        mDivLine = findViewById(R.id.div_line);

        showPrice();

        mRootLayout = (FrameLayout) findViewById(R.id.rootLayout);
        mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyPopWindow.this.dismiss();

            }
        });

        mSubmitButton = (Button) findViewById(R.id.submitButton);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTakeOutInfoBean.getPhoneList().length == 0)
                    return;
                AlertDialog dialog = CallPhoneDialog.
                        createDialog(mContext, mTakeOutInfoBean.getPhoneList());
                dialog.show();
            }
        });
    }


    private void initWindow() {
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);

        int devideHeight = mContext.getWindowManager().getDefaultDisplay().getHeight();
        devideHeight -= getStatusBarHeight();

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = devideHeight;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        this.setCanceledOnTouchOutside(true);
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private void showPrice() {
        mBuyNumTextView.setText(mTakeOutInfoBean.getTakeOutBuyBean().getNum() + "");
        mSumPriceTextView.setText("¥" + mTakeOutInfoBean.getTakeOutBuyBean().getSumPrice());
        if (mTakeOutInfoBean.getTakeOutBuyBean().getUnCalcNum() != 0) {
            mDivLine.setVisibility(View.VISIBLE);
            mUnCalcNumTextView.setVisibility(View.VISIBLE);
            mUnCalcNumTextView.setText("不可计价份数: " + mTakeOutInfoBean.getTakeOutBuyBean().getUnCalcNum());
        } else {
            mDivLine.setVisibility(View.GONE);
            mUnCalcNumTextView.setVisibility(View.GONE);
        }
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

            GradientDrawable addDrawable = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.bg_add_dishes);
            GradientDrawable minusDrawable = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.bg_minus_dishes);

            addDrawable.setColor(ThemeUtil.getInstance().colorPrimary);
            holder.mAddButton.setBackgroundDrawable(addDrawable);

            minusDrawable.setStroke(DensityUtil.dip2px(mContext, 1), ThemeUtil.getInstance().colorPrimary);
            holder.mMinusButton.setBackgroundDrawable(minusDrawable);

            holder.mDishesNameTextView.setText(dishes.getName());
            if (StringUtil.isPrice(dishes.getPrice())) {
                holder.mDishesPriceTextView.setText((Double.parseDouble(dishes.getPrice()) * num) + "");
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
