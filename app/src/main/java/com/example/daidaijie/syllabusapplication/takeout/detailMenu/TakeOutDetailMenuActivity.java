package com.example.daidaijie.syllabusapplication.takeout.detailMenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.DishesAdapter;
import com.example.daidaijie.syllabusapplication.adapter.SubMenuAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.TakeOutBuyBean;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutModelComponent;
import com.example.daidaijie.syllabusapplication.takeout.searchMenu.SearchTakeOutActivity;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.BuyPopWindow;
import com.example.daidaijie.syllabusapplication.widget.CallPhoneDialog;
import com.example.daidaijie.syllabusapplication.widget.MyItemAnimator;

import javax.inject.Inject;

import butterknife.BindView;

public class TakeOutDetailMenuActivity extends BaseActivity implements TakeOutDetailContract.view, SwipeRefreshLayout.OnRefreshListener, DishesAdapter.OnNumChangeListener {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.dishesRecyclerView)
    RecyclerView mDishesRecyclerView;
    @BindView(R.id.tv_sticky_header_view)
    LinearLayout mTvStickyHeaderView;
    @BindView(R.id.stickyTextView)
    TextView mStickyTextView;
    @BindView(R.id.subMenuRecyclerView)
    RecyclerView mSubMenuRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.takeoutNameTextView)
    TextView mTakeoutNameTextView;
    @BindView(R.id.longNumberTextView)
    TextView mLongNumberTextView;
    @BindView(R.id.conditionTextView)
    TextView mConditionTextView;
    @BindView(R.id.shortNumberTextView)
    TextView mShortNumberTextView;
    @BindView(R.id.shoppingImg)
    ImageView mShoppingImg;
    @BindView(R.id.buyNumTextView)
    TextView mBuyNumTextView;
    @BindView(R.id.shoppingLayout)
    RelativeLayout mShoppingLayout;
    @BindView(R.id.bottomBgLayout)
    LinearLayout mBottomBgLayout;
    @BindView(R.id.sumPriceTextView)
    TextView mSumPriceTextView;
    @BindView(R.id.div_line)
    View mDivLine;
    @BindView(R.id.unCalcNumTextView)
    TextView mUnCalcNumTextView;
    @BindView(R.id.submitButton)
    Button mSubmitButton;
    FrameLayout aniLayout;


    private DishesAdapter mTakeOutMenuAdapter;

    private SubMenuAdapter mSubMenuAdapter;

    private LinearLayoutManager mLinearLayoutManager;

    private int mIndex;
    private boolean move;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private CollapsingToolbarLayoutState state;

    private static final String CLASS_NAME = TakeOutDetailMenuActivity.class.getCanonicalName();

    public static final String EXTRA_OBJECT_ID = CLASS_NAME + ".mObjectID";

    public static final int REQUEST_SEARCH = 205;

    @Inject
    TakeOutDetailPresenter mTakeOutDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 添加外层布局，用于动画展示
         */
        aniLayout = new FrameLayout(this);
        final ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
        decorView.addView(aniLayout);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        /**
         * 设置AppBar展开的属性
         */
        mTitleTextView.setVisibility(View.GONE);
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        mTitleTextView.setVisibility(View.GONE);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        mTitleTextView.setVisibility(View.VISIBLE);
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        mTitleTextView.setVisibility(View.GONE);
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });

        DaggerTakeOutDetailComponent.builder()
                .takeOutModelComponent(TakeOutModelComponent.getInstance(mAppComponent))
                .takeOutDetailModule(new TakeOutDetailModule(this, getIntent().getStringExtra(EXTRA_OBJECT_ID)))
                .build().inject(this);

        setupRecyclerView();

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mTakeOutDetailPresenter.start();
            }
        });

        mBottomBgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakeOutDetailPresenter.showPopWindows();
            }
        });

        setResult(RESULT_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK) {
            mTakeOutMenuAdapter.notifyDataSetChanged();
            mTakeOutDetailPresenter.showPrice();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setUpTakeOutInfo(final TakeOutInfoBean takeOutInfoBean) {
        mTitleTextView.setText(takeOutInfoBean.getName());
        mTakeoutNameTextView.setText(takeOutInfoBean.getName());
        mLongNumberTextView.setText("长号 : " + takeOutInfoBean.getLong_number());
        mShortNumberTextView.setText("短号 : " + takeOutInfoBean.getShort_number());
        mConditionTextView.setText("备注 : " + takeOutInfoBean.getCondition());
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takeOutInfoBean.getPhoneList().length == 0) return;
                AlertDialog dialog = CallPhoneDialog.
                        createDialog(TakeOutDetailMenuActivity.this, takeOutInfoBean.getPhoneList());
                dialog.show();
            }
        });
        showPrice(takeOutInfoBean.getTakeOutBuyBean());
    }

    @Override
    public void setMenuList(TakeOutInfoBean takeOutInfoBean) {
        mSubMenuAdapter.setSubMenus(takeOutInfoBean.getTakeOutSubMenus());
        mTakeOutMenuAdapter.setDishesList(takeOutInfoBean.getDishes());
        mTakeOutMenuAdapter.setBuyMap(takeOutInfoBean.getTakeOutBuyBean().getBuyMap());
        mSubMenuAdapter.notifyDataSetChanged();
        mTakeOutMenuAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        mSubMenuAdapter = new SubMenuAdapter(this, null);
        mSubMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSubMenuRecyclerView.setAdapter(mSubMenuAdapter);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mTakeOutMenuAdapter = new DishesAdapter(this, null, null);
        mDishesRecyclerView.setLayoutManager(mLinearLayoutManager);
        mDishesRecyclerView.setAdapter(mTakeOutMenuAdapter);
        mDishesRecyclerView.setItemAnimator(new MyItemAnimator());

        mDishesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Get the sticky information from the topmost view of the screen.
                View stickyInfoView = recyclerView.findChildViewUnder(
                        mTvStickyHeaderView.getMeasuredWidth() / 2, 5);

                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null
                        && mSubMenuAdapter.getSubMenus() != null) {
                    int subPos = Integer.parseInt(String.valueOf(stickyInfoView.getContentDescription()));
                    mStickyTextView.setText(mSubMenuAdapter.getSubMenus().get(subPos).getName());
                    if (subPos != mSubMenuAdapter.getSelectItem()) {
                        mSubMenuRecyclerView.scrollToPosition(subPos);
                        mSubMenuAdapter.setSelectItem(subPos);
                        mSubMenuAdapter.notifyDataSetChanged();
                    }
                }

                // Get the sticky view's translationY by the first view below the sticky's height.
                View transInfoView = recyclerView.findChildViewUnder(
                        mTvStickyHeaderView.getMeasuredWidth() / 2, mTvStickyHeaderView.getMeasuredHeight() + 1);

                if (transInfoView != null && transInfoView.getTag() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - mTvStickyHeaderView.getMeasuredHeight();
                    if (transViewStatus == mTakeOutMenuAdapter.HAS_STICKY_VIEW) {
                        // If the first view below the sticky's height scroll off the screen,
                        // then recovery the sticky view's translationY.
                        if (transInfoView.getTop() > 0) {
                            mTvStickyHeaderView.setTranslationY(dealtY);
                        } else {
                            mTvStickyHeaderView.setTranslationY(0);
                        }
                    } else if (transViewStatus == mTakeOutMenuAdapter.NONE_STICKY_VIEW) {
                        mTvStickyHeaderView.setTranslationY(0);
                    }
                }

            }
        });

        mTakeOutMenuAdapter.setOnNumChangeListener(this);

        mSubMenuAdapter.setOnItemClickListener(new SubMenuAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (mSubMenuAdapter.getSubMenus() == null) return;
                int pos = mSubMenuAdapter
                        .getSubMenus().get(position).getFirstItemPos();
                moveToPosition(pos);
            }
        });

        mDishesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < mDishesRecyclerView.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = mDishesRecyclerView.getChildAt(n).getTop();
                        //最后的移动
                        mDishesRecyclerView.scrollBy(0, top);
                    }
                }
            }
        });
    }

    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mDishesRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = mDishesRecyclerView.getChildAt(n - firstItem).getTop();
            mDishesRecyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            mDishesRecyclerView.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            mIndex = n;
            move = true;
        }
    }

    @Override
    public void onAddNum(View v, int position) {

        mTakeOutDetailPresenter.addDish(position);

        mTakeOutMenuAdapter.notifyItemChanged(position);

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
        mTakeOutDetailPresenter.reduceDish(position);
        mTakeOutMenuAdapter.notifyItemChanged(position);
    }


    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mDishesRecyclerView, msg, SnackbarUtil.Alert
        ).show();
    }

    @Override
    public void showRefresh(boolean isShow) {
        mSwipeRefreshLayout.setRefreshing(isShow);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_take_out_detail_menu;
    }

    public static Intent getIntent(Context context, String objectID) {
        Intent intent = new Intent(context, TakeOutDetailMenuActivity.class);
        intent.putExtra(EXTRA_OBJECT_ID, objectID);
        return intent;
    }

    @Override
    public void onRefresh() {
        mTakeOutDetailPresenter.loadData();
    }

    @Override
    public void showPopWindows(final TakeOutInfoBean takeOutInfoBean) {

        BuyPopWindow popWindow = new BuyPopWindow(this, takeOutInfoBean);
        popWindow.setOnDataChangeListener(new BuyPopWindow.OnDataChangeListener() {
            @Override
            public void onChange(int position) {
                mTakeOutMenuAdapter.notifyItemChanged(position);
                showPrice(takeOutInfoBean.getTakeOutBuyBean());
            }

            @Override
            public void onChangeAll() {
                mTakeOutMenuAdapter.notifyDataSetChanged();
                showPrice(takeOutInfoBean.getTakeOutBuyBean());
            }
        });
        popWindow.show();
    }

    @Override
    public void toSearch(String objectID) {
        Intent intent = SearchTakeOutActivity.getIntent(TakeOutDetailMenuActivity.this, objectID);
        startActivityForResult(intent, REQUEST_SEARCH);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_takeout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search_takeout) {
            mTakeOutDetailPresenter.toSearch();
        }
        return super.onOptionsItemSelected(item);
    }
}
