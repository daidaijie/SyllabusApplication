package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.DishesAdapter;
import com.example.daidaijie.syllabusapplication.bean.Dishes;
import com.example.daidaijie.syllabusapplication.bean.TakeOutBuyBean;

import java.util.List;

import butterknife.BindView;

public class SearchTakeOutActivity extends BaseActivity {

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

    private TakeOutBuyBean mTakeOutBuyBean;

    private List<Dishes> mDishes;

    private List<Dishes> mSearchDishes;

    private DishesAdapter mDishesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


}
