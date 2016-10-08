package com.example.daidaijie.syllabusapplication.takeout;

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
import android.view.inputmethod.InputMethodManager;
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
import com.example.daidaijie.syllabusapplication.model.TakeOutManager;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.widget.BuyPopWindow;
import com.example.daidaijie.syllabusapplication.widget.CallPhoneDialog;
import com.example.daidaijie.syllabusapplication.widget.MyItemAnimator;
import com.liaoinstan.springview.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SearchTakeOutActivity extends BaseActivity implements DishesAdapter.OnNumChangeListener {

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
    @BindView(R.id.bottomLayout)
    RelativeLayout mBottomLayout;
    @BindView(R.id.editLine)
    View mEditLine;

    private FrameLayout aniLayout;

    private TakeOutInfoBean mTakeOutInfoBean;

    private List<Dishes> mSearchDishes;

    private TakeOutBuyBean mTakeOutBuyBean;

    private DishesAdapter mDishesAdapter;

    private int mPosition;

    private String mKeyWord;

    public static final String EXTRA_POSITION = "com.example.daidaijie.syllabusapplication.activity" +
            ".SearchTakeOutActivity.mPosition";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        aniLayout = new FrameLayout(this);

        final ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
        decorView.addView(aniLayout);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);
        mTakeOutInfoBean = TakeOutManager.getInstance().getTakeOutInfoBeen().get(mPosition);
        mTakeOutBuyBean = mTakeOutInfoBean.getTakeOutBuyBean();

        mSearchDishes = new ArrayList<>();
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
                mKeyWord = s.toString();
                if (mKeyWord.trim().isEmpty()) {
                    mSearchDishes.clear();
                    mDishesAdapter.notifyDataSetChanged();
                    return;
                }
                Observable.from(mTakeOutInfoBean.getDishes())
                        .subscribeOn(Schedulers.computation())
                        .observeOn(Schedulers.computation())
                        .filter(new Func1<Dishes, Boolean>() {
                            @Override
                            public Boolean call(Dishes dishes) {
                                return dishes.getName().contains(mKeyWord);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Dishes>() {
                            int index = 0;

                            @Override
                            public void onStart() {
                                super.onStart();
                                mSearchDishes.clear();
                            }

                            @Override
                            public void onCompleted() {
                                mDishesAdapter.setKeyword(mKeyWord);
                                mDishesAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(Dishes dishes) {
                                dishes.mPos = index++;
                                mSearchDishes.add(dishes);
                            }
                        });
            }
        });
        mRemoveTextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setText("");
            }
        });

        mSearchEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                showInput();
            }
        }, 50);

        mKeyWord = "";
        mDishesAdapter = new DishesAdapter(this, mSearchDishes, mTakeOutBuyBean.getBuyMap(), mKeyWord);
        mDishesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDishesRecyclerView.setItemAnimator(new MyItemAnimator());
        mDishesRecyclerView.setAdapter(mDishesAdapter);

        mDishesAdapter.setOnNumChangeListener(this);

        mBottomBgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindows();
            }
        });

        showPrice();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTakeOutInfoBean.getPhoneList().length == 0) return;
                AlertDialog dialog = CallPhoneDialog.
                        createDialog(SearchTakeOutActivity.this, mTakeOutInfoBean.getPhoneList());
                dialog.show();
            }
        });

        setResult(RESULT_OK);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_search_take_out;
    }

    private void showInput() {
        mSearchEditText.setFocusable(true);
        mSearchEditText.setFocusableInTouchMode(true);
        mSearchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mSearchEditText, InputMethodManager.SHOW_FORCED);
    }

    private void hideInput() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
    }

    public static Intent getIntent(Context context, int position) {
        Intent intent = new Intent(context, SearchTakeOutActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    @Override
    public void onAddNum(View v, int position) {
        hideInput();
        Dishes dishes = mSearchDishes.get(position);
        mTakeOutBuyBean.addDishes(dishes);
        showPrice();

        mDishesAdapter.notifyItemChanged(position);
        int[] location = new int[2];
        v.getLocationInWindow(location);

        final TextView text = new TextView(this);
        text.setText(Html.fromHtml("<b>1</b>"));
        text.setGravity(Gravity.CENTER);
        GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.bg_add_dishes);
        drawable.setColor(ThemeModel.getInstance().colorPrimary);
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
        hideInput();
        Dishes dishes = mSearchDishes.get(position);
        mTakeOutBuyBean.removeDishes(dishes);
        showPrice();

        mDishesAdapter.notifyItemChanged(position);
    }

    private void showPrice() {
        mBuyNumTextView.setText(mTakeOutBuyBean.getNum() + "");
        mSumPriceTextView.setText("¥" + mTakeOutBuyBean.getSumPrice());
        if (mTakeOutBuyBean.getUnCalcNum() != 0) {
            mDivLine.setVisibility(View.VISIBLE);
            mUnCalcNumTextView.setVisibility(View.VISIBLE);
            mUnCalcNumTextView.setText("不可计价份数: " + mTakeOutBuyBean.getUnCalcNum());
        } else {
            mDivLine.setVisibility(View.GONE);
            mUnCalcNumTextView.setVisibility(View.GONE);
        }
    }


    private void showPopWindows() {
        hideInput();
        BuyPopWindow popWindow = new BuyPopWindow(this, mTakeOutBuyBean, mPosition);
        popWindow.setOnDataChangeListener(new BuyPopWindow.OnDataChangeListener() {
            @Override
            public void onChange(int position) {
                mDishesAdapter.notifyDataSetChanged();
                showPrice();
            }

            @Override
            public void onChangeAll() {
                mDishesAdapter.notifyDataSetChanged();
                showPrice();
            }
        });
        popWindow.show();
    }
}
