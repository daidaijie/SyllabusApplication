package com.example.daidaijie.syllabusapplication.syllabus.addlesson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.activity.SelectTimeActivity;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.model.AddLessonModel;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;

public class AddLessonGridActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.weekRecyclerView)
    RecyclerView mWeekRecyclerView;
    @BindView(R.id.singleTextView)
    TextView mSingleTextView;
    @BindView(R.id.doubleTextView)
    TextView mDoubleTextView;
    @BindView(R.id.allTextView)
    TextView mAllTextView;
    @BindView(R.id.selectTimeButton)
    FButton mSelectTimeButton;
    @BindView(R.id.hasSelectTimeTextView)
    TextView mHasSelectTimeTextView;

    public static final String EXTRA_POSITION = "com.example.daidaijie.syllabusapplication.activity" +
            ".AddLessonGridActivity.position";

    public static final int RESULT_OK = 200;

    public static final int RESULT_CANCEL = 201;

    private boolean isSingle;
    private boolean isDouble;
    private boolean isAll;

    private List<Boolean> selectWeeks;

    private WeekSelectAdapter mWeekSelectAdapter;

    private int mPosition;

    private static final int REQUEST_ADD_TIME = 203;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);
        selectWeeks = new ArrayList<>();
        selectWeeks.addAll(AddLessonModel.getInstance().mTimes.get(mPosition).selectWeeks);

        getSelectType();

        mWeekSelectAdapter = new WeekSelectAdapter();
        mWeekRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mWeekRecyclerView.setAdapter(mWeekSelectAdapter);

        mSelectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SelectTimeActivity.getIntent(AddLessonGridActivity.this, mPosition);
                startActivityForResult(intent, REQUEST_ADD_TIME);
            }
        });

        setSelectTimeText();

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_lesson_grid;
    }

    private void setSelectTimeText() {
        mHasSelectTimeTextView.setText(Html.fromHtml("<b>目前已选择时间</b><br/><br/>"
                + AddLessonModel.getInstance().mTimes.get(mPosition).toHtmlSelectTimeString()));
    }

    @OnClick({R.id.singleTextView, R.id.doubleTextView, R.id.allTextView})
    public void onClick(View view) {
        if (isSingle || isDouble || isAll) {
            for (int i = 0; i < 16; i++) {
                selectWeeks.set(i, false);
            }
        }
        switch (view.getId()) {
            case R.id.singleTextView: {
                if (isSingle) {
                    isSingle = false;
                } else {
                    for (int i = 0; i < 16; i++) {
                        if (i % 2 == 0) {
                            selectWeeks.set(i, true);
                        } else {
                            selectWeeks.set(i, false);
                        }
                    }
                    isSingle = true;
                    isDouble = isAll = false;
                }
            }
            break;
            case R.id.doubleTextView: {
                if (isDouble) {
                    isDouble = false;
                } else {
                    for (int i = 0; i < 16; i++) {
                        if (i % 2 == 1) {
                            selectWeeks.set(i, true);
                        } else {
                            selectWeeks.set(i, false);
                        }
                    }
                    isDouble = true;
                    isSingle = isAll = false;
                }
            }
            break;
            case R.id.allTextView: {
                if (isAll) {
                    isAll = false;
                } else {
                    for (int i = 0; i < 16; i++) {
                        selectWeeks.set(i, true);
                    }
                    isAll = true;
                    isSingle = isDouble = false;
                }
            }
            break;
        }
        setWeekType();
        mWeekSelectAdapter.notifyDataSetChanged();
    }


    private void getSelectType() {
        isAll = isDouble = isSingle = true;
        for (int i = 0; i < 16; i++) {
            if (!selectWeeks.get(i)) {
                isAll = false;
            }
            if (i % 2 == 0 && selectWeeks.get(i) != true || i % 2 == 1 && selectWeeks.get(i) != false) {
                isSingle = false;
            }
            if (i % 2 == 0 && selectWeeks.get(i) != false || i % 2 == 1 && selectWeeks.get(i) != true) {
                isDouble = false;
            }
        }

        setWeekType();
    }

    private void setWeekType() {
        if (isAll) {
            mAllTextView.setTextColor(getResources().getColor(R.color.material_white));
            mAllTextView.setBackgroundColor(ThemeUtil.getInstance().colorPrimary);
        } else {
            mAllTextView.setTextColor(getResources().getColor(R.color.defaultTextColor));
            mAllTextView.setBackgroundColor(getResources().getColor(R.color.material_white));
        }
        if (isSingle) {
            mSingleTextView.setTextColor(getResources().getColor(R.color.material_white));
            mSingleTextView.setBackgroundColor(ThemeUtil.getInstance().colorPrimary);
        } else {
            mSingleTextView.setTextColor(getResources().getColor(R.color.defaultTextColor));
            mSingleTextView.setBackgroundColor(getResources().getColor(R.color.material_white));
        }
        if (isDouble) {
            mDoubleTextView.setTextColor(getResources().getColor(R.color.material_white));
            mDoubleTextView.setBackgroundColor(ThemeUtil.getInstance().colorPrimary);
        } else {
            mDoubleTextView.setTextColor(getResources().getColor(R.color.defaultTextColor));
            mDoubleTextView.setBackgroundColor(getResources().getColor(R.color.material_white));
        }
    }

    public static Intent getIntent(Context context, int position) {
        Intent intent = new Intent(context, AddLessonGridActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_TIME && resultCode == SelectTimeActivity.RESULT_OK) {
            setSelectTimeText();
        }
    }

    public class WeekSelectAdapter extends RecyclerView.Adapter<WeekSelectAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(AddLessonGridActivity.this);
            View view = inflater.inflate(R.layout.item_select_week, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (selectWeeks.get(position)) {
                holder.mWeekTextView.setBackgroundColor(ThemeUtil.getInstance().colorPrimary);
                holder.mWeekTextView.setTextColor(getResources().getColor(R.color.material_white));
            } else {
                holder.mWeekTextView.setBackgroundColor(getResources().getColor(R.color.material_white));
                holder.mWeekTextView.setTextColor(getResources().getColor(R.color.defaultTextColor));
            }
            holder.mWeekTextView.setText("" + (position + 1));
            holder.mWeekTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectWeeks.get(position)) {
                        selectWeeks.set(position, false);
                    } else {
                        selectWeeks.set(position, true);
                    }
                    if (selectWeeks.get(position)) {
                        holder.mWeekTextView.setBackgroundColor(ThemeUtil.getInstance().colorPrimary);
                        holder.mWeekTextView.setTextColor(getResources().getColor(R.color.material_white));
                    } else {
                        holder.mWeekTextView.setBackgroundColor(getResources().getColor(R.color.material_white));
                        holder.mWeekTextView.setTextColor(getResources().getColor(R.color.defaultTextColor));
                    }
                    getSelectType();
                }
            });
        }

        @Override
        public int getItemCount() {
            return selectWeeks.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.weekTextView)
            TextView mWeekTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_content, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_finish) {
            boolean isFinish = false;
            for (boolean isSelect : selectWeeks) {
                if (isSelect) {
                    isFinish = true;
                    break;
                }
            }
            if (!isFinish) {
                showWarning("还没选择周数!");
                return true;
            }
            isFinish = false;
            for (List<Boolean> selectList : AddLessonModel.getInstance().mTimes.get(mPosition).mSelectTimes) {
                if (isFinish) break;
                for (boolean isSelect : selectList) {
                    if (isSelect) {
                        isFinish = true;
                        break;
                    }
                }
            }
            if (!isFinish) {
                showWarning("还没选择时间!");
                return true;
            }
            if (isFinish) {
                AddLessonModel.getInstance().mTimes.get(mPosition).selectWeeks = selectWeeks;
                setResult(RESULT_OK);
                this.finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCEL);
        super.onBackPressed();
    }

    public void showWarning(String msg) {
        SnackbarUtil.ShortSnackbar(mWeekRecyclerView, msg, SnackbarUtil.Warning).show();
    }
}
