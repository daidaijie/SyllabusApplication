package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.TakeOutMenuAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutContract;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import javax.inject.Inject;

import butterknife.BindView;

public class TakeOutActivity extends BaseActivity implements TakeOutContract.TakeOutView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.menuListRecyclerView)
    RecyclerView mMenuListRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    TakeOutMenuAdapter mTakeOutMenuAdapter;

    /**
     * 必须使用实体类,否则Dagger2无法找到构造函数
     */
    @Inject
    TakeOutPresenter mTakeOutPresenter;

    private static final String RESULT_POSITION = "com.example.daidaijie.syllabusapplication.activity" +
            ".TakeOutActivity.resultPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerTakeOutComponent.builder()
                .appComponent(mAppComponent)
                .takeOutModule(new TakeOutModule(this))
                .build().inject(this);

        mTakeOutPresenter.start();

        /*mTakeOutMenuAdapter = new TakeOutMenuAdapter(this, TakeOutManager.getInstance().getTakeOutInfoBeen());
        mMenuListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMenuListRecyclerView.setAdapter(mTakeOutMenuAdapter);



        if (TakeOutManager.getInstance().getTakeOutInfoBeen().size() == 0) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getTakeOutInfo();
                }
            });
        }*/

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_take_out;
    }

    /*  private void getTakeOutInfo() {
          TakeOutInfoApi service = BmobModel.getInstance().mRetrofit.create(TakeOutInfoApi.class);
          service.getTokenResult("name,long_number,short_number,condition")
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<BmobResult<TakeOutInfoBean>>() {
                      @Override
                      public void onCompleted() {
                          mTakeOutMenuAdapter.setTakeOutInfoBeen(TakeOutManager.getInstance().getTakeOutInfoBeen());
                          mTakeOutMenuAdapter.notifyDataSetChanged();
                          mSwipeRefreshLayout.setRefreshing(false);
                      }

                      @Override
                      public void onError(Throwable e) {
                          mSwipeRefreshLayout.setRefreshing(false);
                          showFailMessage("获取失败");
                      }

                      @Override
                      public void onNext(BmobResult<TakeOutInfoBean> bmobResult) {
                          if (bmobResult.getResults().size() != 0) {
                              List<TakeOutInfoBean> takeOutInfoBeen = bmobResult.getResults();
                              for (TakeOutInfoBean takeOutInfoBean : takeOutInfoBeen) {
                                  TakeOutInfoBean hasBean = TakeOutManager.getInstance().getBeanByID(takeOutInfoBean.getObjectId());
                                  if (hasBean != null) {
                                      takeOutInfoBean.setMenu(hasBean.getMenu());
                                      takeOutInfoBean.setTakeOutSubMenus(hasBean.getTakeOutSubMenus());
                                  }
                              }
                              TakeOutManager.getInstance().setTakeOutInfoBeen(takeOutInfoBeen);
                          }
                      }
                  });
      }
  */
    @Override
    public void onRefresh() {
        mTakeOutPresenter.loadData();
    }

    public static Intent getIntent(int position) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_POSITION, position);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 206 & resultCode == RESULT_OK) {
            int resultPos = data.getIntExtra(RESULT_POSITION, -1);
            if (resultPos != -1) {
                mTakeOutMenuAdapter.notifyItemChanged(resultPos);
            } else {
                mTakeOutMenuAdapter.notifyDataSetChanged();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mMenuListRecyclerView, msg, SnackbarUtil.Alert)
                .show();
    }
}
