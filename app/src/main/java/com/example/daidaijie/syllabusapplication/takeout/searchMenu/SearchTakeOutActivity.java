package com.example.daidaijie.syllabusapplication.takeout.searchMenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.DishesAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Dishes;
import com.example.daidaijie.syllabusapplication.bean.TakeOutBuyBean;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutModelComponent;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.BuyPopWindow;
import com.example.daidaijie.syllabusapplication.widget.CallPhoneDialog;
import com.example.daidaijie.syllabusapplication.widget.MyItemAnimator;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SearchTakeOutActivity extends BaseActivity implements DishesAdapter.OnNumChangeListener, SearchTakeOutContract.view {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchEditText)
    EditText mSearchEditText;
    @BindView(R.id.removeTextImageView)
    ImageView mRemoveTextImageView;
    @BindView(R.id.dishesRecyclerView)
    RecyclerView mDishesRecyclerView;
    @BindView(R.id.sumPriceTextView)
    TextView mSumPriceTextView;
    @BindView(R.id.div_line)
    View mDivLine;
    @BindView(R.id.unCalcNumTextView)
    TextView mUnCalcNumTextView;
    @BindView(R.id.bottomBgLayout)
    LinearLayout mBottomBgLayout;
    @BindView(R.id.submitButton)
    Button mSubmitButton;
    @BindView(R.id.shoppingImg)
    ImageView mShoppingImg;
    @BindView(R.id.buyNumTextView)
    TextView mBuyNumTextView;
    @BindView(R.id.shoppingLayout)
    RelativeLayout mShoppingLayout;

    private FrameLayout aniLayout;

    private DishesAdapter mDishesAdapter;

    private static final String CLASS_NAME = SearchTakeOutActivity.class.getCanonicalName();


    public static final String EXTRA_OBJECT_ID = CLASS_NAME + ".objectID";

    @Inject
    SearchTakeOutPresenter mSearchTakeOutPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 添加布局来展示动画
         */
        aniLayout = new FrameLayout(this);
        final ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
        decorView.addView(aniLayout);

        setupTitleBar(mToolbar);

        DaggerSearchTakeOutComponent.builder()
                .takeOutModelComponent(TakeOutModelComponent.getInstance(mAppComponent))
                .searchTakeOutModule(new SearchTakeOutModule(this, getIntent().getStringExtra(EXTRA_OBJECT_ID)))
                .build().inject(this);

        mSearchTakeOutPresenter.start();
        mRemoveTextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setText("");
            }
        });


        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mRemoveTextImageView.setVisibility(View.GONE);
                } else {
                    mRemoveTextImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchTakeOutPresenter.search(s.toString());
            }
        });

        mDishesAdapter = new DishesAdapter(this, null, null, "");
        mDishesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDishesRecyclerView.setItemAnimator(new MyItemAnimator());
        mDishesRecyclerView.setAdapter(mDishesAdapter);

        mDishesAdapter.setOnNumChangeListener(this);


        mBottomBgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchTakeOutPresenter.showPopWindows();
            }
        });

        mSearchEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                showInput();
            }
        }, 50);


        setResult(RESULT_OK);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_search_take_out;
    }

    @Override
    public void showInput() {
        showInput(mSearchEditText);
    }

    @Override
    public void hideInput() {
        hideInput(mSearchEditText);
    }

    @Override
    public void setUpTakeOutInfo(final TakeOutInfoBean takeOutInfoBean) {
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takeOutInfoBean.getPhoneList().length == 0) return;
                AlertDialog dialog = CallPhoneDialog.
                        createDialog(SearchTakeOutActivity.this, takeOutInfoBean.getPhoneList());
                dialog.show();
            }
        });
        showPrice(takeOutInfoBean.getTakeOutBuyBean());
    }

    @Override
    public void showSearchResult(TakeOutBuyBean takeOutBuyBean, List<Dishes> dishes, String keyword) {
        mDishesAdapter.setDishesList(dishes);
        mDishesAdapter.setBuyMap(takeOutBuyBean.getBuyMap());
        mDishesAdapter.setKeyword(keyword);
        mDishesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showPopWindows(final TakeOutInfoBean takeOutInfoBean) {
        hideInput();
        BuyPopWindow popWindow = new BuyPopWindow(this, takeOutInfoBean);
        popWindow.setOnDataChangeListener(new BuyPopWindow.OnDataChangeListener() {
            @Override
            public void onChange(int position) {
                mDishesAdapter.notifyDataSetChanged();
                showPrice(takeOutInfoBean.getTakeOutBuyBean());
            }

            @Override
            public void onChangeAll() {
                mDishesAdapter.notifyDataSetChanged();
                showPrice(takeOutInfoBean.getTakeOutBuyBean());
            }
        });
        popWindow.show();
    }

    public static Intent getIntent(Context context, String objectID) {
        Intent intent = new Intent(context, SearchTakeOutActivity.class);
        intent.putExtra(EXTRA_OBJECT_ID, objectID);
        return intent;
    }

    @Override
    public void onAddNum(View v, int position) {
        hideInput();
        mSearchTakeOutPresenter.addDish(mDishesAdapter.getDishesList().get(position).mPos);
        mDishesAdapter.notifyItemChanged(position);

        int[] location = new int[2];
        v.getLocationInWindow(location);

        final TextView text = new TextView(this);
        text.setText(Html.fromHtml("<b>1</b>"));
        text.setGravity(Gravity.CENTER);
        GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.bg_add_dishes);
        drawable.setColor(ThemeUtil.getInstance().colorPrimary);
        text.setBackgroundDrawable(drawable);
        text.setTextColor(getResources().getColor(R.color.material_white));
        text.setTextSize(12);

        aniLayout.addView(text);
        int width = DensityUtil.dip2px(this, 24);
        text.getLayoutParams().width = width;
        text.getLayoutParams().height = width;

        int lf = location[0];
        int tf = location[1];

        text.setX(lf);
        text.setY(tf);

        int[] shoppingLocation = new int[2];
        mShoppingImg.getLocationInWindow(shoppingLocation);


        ObjectAnimator anix = ObjectAnimator.ofFloat(
                text, "x", lf, shoppingLocation[0]
        );

        ObjectAnimator aniy = ObjectAnimator.ofFloat(
                text, "y", tf, shoppingLocation[1]
        );
        anix.setInterpolator(new LinearInterpolator());
        aniy.setInterpolator(new AccelerateInterpolator());
        AnimatorSet dumpAni = new AnimatorSet();
        dumpAni.play(anix).with(aniy);
        dumpAni.setDuration(500);
        dumpAni.start();


        dumpAni.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                aniLayout.removeView(text);
                ObjectAnimator aniScaleX = ObjectAnimator.ofFloat(
                        mShoppingLayout, "scaleX", 0.7f, 1.0f
                );
                ObjectAnimator aniScaleY = ObjectAnimator.ofFloat(
                        mShoppingLayout, "scaleY", 0.7f, 1.0f
                );
                AnimatorSet scaleAni = new AnimatorSet();
                scaleAni.play(aniScaleX).with(aniScaleY);
                scaleAni.setDuration(300);
                scaleAni.setInterpolator(new AnticipateOvershootInterpolator());
                scaleAni.start();
            }
        });

    }

    @Override
    public void onReduceNum(int position) {
        mSearchTakeOutPresenter.reduceDish(mDishesAdapter.getDishesList().get(position).mPos);

        mDishesAdapter.notifyItemChanged(position);
    }

    @Override
    public void showPrice(TakeOutBuyBean takeOutBuyBean) {
        mBuyNumTextView.setText(takeOutBuyBean.getNum() + "");
        mSumPriceTextView.setText("¥" + takeOutBuyBean.getSumPrice());
        if (takeOutBuyBean.getUnCalcNum() != 0) {
            mDivLine.setVisibility(View.VISIBLE);
            mUnCalcNumTextView.setVisibility(View.VISIBLE);
            mUnCalcNumTextView.setText("不可计价份数: " + takeOutBuyBean.getUnCalcNum());
        } else {
            mDivLine.setVisibility(View.GONE);
            mUnCalcNumTextView.setVisibility(View.GONE);
        }
    }


    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mDishesRecyclerView, msg, SnackbarUtil.Alert
        ).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideInput(mSearchEditText);
    }

}
