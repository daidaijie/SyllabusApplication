package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.DishesAdapter;
import com.example.daidaijie.syllabusapplication.adapter.SubMenuAdapter;
import com.example.daidaijie.syllabusapplication.bean.Dishes;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.bean.TakeOutSubMenu;
import com.example.daidaijie.syllabusapplication.model.BmobModel;
import com.example.daidaijie.syllabusapplication.model.TakeOutModel;
import com.example.daidaijie.syllabusapplication.service.TakeOutMenuDetailService;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TakeOutDetailMenuActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

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
    @BindView(R.id.activity_take_out_detail_menu)
    LinearLayout mActivityTakeOutDetailMenu;

    public static final String EXTRA_POSITION = "com.example.daidaijie.syllabusapplication.activity" +
            ".TakeOutDetailMenuActivity";
    @BindView(R.id.subMenuRecyclerView)
    RecyclerView mSubMenuRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int mPosition;

    private TakeOutInfoBean mTakeOutInfoBean;

    private List<Dishes> mDishesList;

    private DishesAdapter mTakeOutMenuAdapter;

    private SubMenuAdapter mSubMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);

        mTakeOutInfoBean = TakeOutModel.getInstance().getTakeOutInfoBeen().get(mPosition);
        if (mTakeOutInfoBean.getTakeOutSubMenus() == null) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getDetailMenu();
                }
            });
        } else {
            mDishesList = mTakeOutInfoBean.getDishes();
        }

        mTakeOutMenuAdapter = new DishesAdapter(this, mDishesList);
        mDishesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDishesRecyclerView.setAdapter(mTakeOutMenuAdapter);


        mSubMenuAdapter = new SubMenuAdapter(this, mTakeOutInfoBean.getTakeOutSubMenus());
        mSubMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSubMenuRecyclerView.setAdapter(mSubMenuAdapter);


        mDishesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Get the sticky information from the topmost view of the screen.
                View stickyInfoView = recyclerView.findChildViewUnder(
                        mTvStickyHeaderView.getMeasuredWidth() / 2, 5);

                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    mStickyTextView.setText(String.valueOf(stickyInfoView.getContentDescription()));
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

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    private void getDetailMenu() {
        TakeOutMenuDetailService service = BmobModel.getInstance().mRetrofit.create(TakeOutMenuDetailService.class);
        service.getTokenResult(mTakeOutInfoBean.getObjectId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TakeOutInfoBean>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mDishesList = mTakeOutInfoBean.getDishes();

                        mTakeOutMenuAdapter.setDishesList(mDishesList);
                        mTakeOutMenuAdapter.notifyDataSetChanged();

                        mSubMenuAdapter.setSubMenus(mTakeOutInfoBean.getTakeOutSubMenus());
                        mSubMenuAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        showFailMessage("获取失败");
                    }

                    @Override
                    public void onNext(TakeOutInfoBean takeOutInfoBean) {
                        mTakeOutInfoBean = takeOutInfoBean;
                        mTakeOutInfoBean.loadTakeOutSubMenus();
                        for (TakeOutSubMenu subMenu : mTakeOutInfoBean.getTakeOutSubMenus()) {
                            Logger.t("subMenu").e(subMenu.getName());
                        }
                        TakeOutModel.getInstance().getTakeOutInfoBeen().set(mPosition, mTakeOutInfoBean);
                        TakeOutModel.getInstance().save();
                    }
                });
    }

    private void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mDishesRecyclerView, msg, SnackbarUtil.Alert
        ).show();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_take_out_detail_menu;
    }

    public static Intent getIntent(Context context, int position) {
        Intent intent = new Intent(context, TakeOutDetailMenuActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }


    @Override
    public void onRefresh() {
        getDetailMenu();
    }
}
