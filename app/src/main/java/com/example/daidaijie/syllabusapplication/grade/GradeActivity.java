package com.example.daidaijie.syllabusapplication.grade;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.GradeListAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.GradeInfo;
import com.example.daidaijie.syllabusapplication.bean.SemesterGrade;
import com.example.daidaijie.syllabusapplication.user.UserComponent;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class GradeActivity extends BaseActivity implements GradeContract.view, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "GradeActivity";

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.gradeListRecycleList)
    RecyclerView mGradeListRecycleList;
    @BindView(R.id.refreshGradeLayout)
    SwipeRefreshLayout mRefreshGradeLayout;
    @BindView(R.id.header)
    RecyclerViewHeader mHeader;
    @BindView(R.id.sumNumTextView)
    TextView mSumNumTextView;
    @BindView(R.id.sumCreditTextView)
    TextView mSumCreditTextView;
    @BindView(R.id.sumGpaTextView)
    TextView mSumGpaTextView;

    private GradeInfo mGradeInfo;

    private GradeListAdapter mGradeListAdapter;

    private MenuItem mExpandMenuItem;

    private boolean isAllExpand;

    @Inject
    GradePresenter mGradePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerGradeComponent.builder()
                .userComponent(UserComponent.getINSTANCE())
                .gradeModule(new GradeModule(this))
                .build().inject(this);

        mGradeListAdapter = new GradeListAdapter(this, null);

        mGradeListRecycleList.setLayoutManager(new LinearLayoutManager(this));
        mGradeListRecycleList.setAdapter(mGradeListAdapter);

        mRefreshGradeLayout.setOnRefreshListener(this);
        setupSwipeRefreshLayout(mRefreshGradeLayout);

        mHeader.attachTo(mGradeListRecycleList);
        setHeader();

        mRefreshGradeLayout.post(new Runnable() {
            @Override
            public void run() {
                mGradePresenter.start();
            }
        });

        isAllExpand = false;

    }

    private void getGrade() {
        /*GradeApi gradeApi = RetrofitUtil.getDefault().create(GradeApi.class);
        gradeApi.getGrade(User.getInstance().getAccount(), User.getInstance().getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GradeInfo>() {
                    @Override
                    public void onCompleted() {
                        showSuccessBanner();
                        mRefreshGradeLayout.setRefreshing(false);
                        mGradeInfo.trimList();
                        setExpandMenu(isAllExpand);
                        mGradeInfo.setIsExpands(isAllExpand);
                        mGradeListAdapter.setGradeInfo(mGradeInfo);
                        mGradeListAdapter.notifyDataSetChanged();
                        setHeader();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showFailBannner();
                        mRefreshGradeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(GradeInfo gradeInfo) {
                        mGradeInfo = gradeInfo;
                    }
                });*/
    }

    private void setHeader() {
        if (mGradeInfo != null) {
            mSumCreditTextView.setText(String.format("%.1f", mGradeInfo.getAllCredit()));
            mSumNumTextView.setText(mGradeInfo.getAllSize() + "");
            mSumGpaTextView.setText(String.format("%.2f", mGradeInfo.getAllGpa()));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_grade, menu);
        mExpandMenuItem = menu.findItem(R.id.action_expand);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if (mGradeInfo == null) {
            return true;
        }

        isAllExpand = !isAllExpand;
        setExpandMenu(isAllExpand);
        mGradeInfo.setIsExpands(isAllExpand);
        mGradeListAdapter.notifyDataSetChanged();*/

        return super.onOptionsItemSelected(item);
    }

    private void setExpandMenu(boolean flag) {
        mExpandMenuItem.setTitle(!flag ? "展开" : "收起");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_grade;
    }

    @Override
    public void onRefresh() {
        mGradePresenter.loadData();
    }


    @Override
    public void showSuccessMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mGradeListRecycleList,
                msg,
                SnackbarUtil.Confirm
        ).show();
    }

    @Override
    public void showInfoMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mGradeListRecycleList,
                msg,
                SnackbarUtil.Info
        ).show();
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.LongSnackbar(
                mGradeListRecycleList,
                msg,
                SnackbarUtil.Alert
        ).setAction("再次获取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    @Override
    public void showFresh(boolean isShow) {
        mRefreshGradeLayout.setRefreshing(isShow);
    }

    @Override
    public void setData(List<SemesterGrade> semesterGrades) {
        mGradeListAdapter.setSemesterGrades(semesterGrades);
        mGradeListAdapter.notifyDataSetChanged();
    }
}
