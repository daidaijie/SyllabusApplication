package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.stuLibrary.bookDetail.BookDetailActivity;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/9/6.
 */
public class LibItemAdapter extends RecyclerView.Adapter<LibItemAdapter.ViewHolder> {

    Activity mActivity;

    List<LibraryBean> mLibraryBeen;

    public interface OnLibItemSelectCallBack {
        void onLibSelect(int position);
    }

    OnLibItemSelectCallBack mOnLibItemSelectCallBack;

    public OnLibItemSelectCallBack getOnLibItemSelectCallBack() {
        return mOnLibItemSelectCallBack;
    }

    public void setOnLibItemSelectCallBack(OnLibItemSelectCallBack onLibItemSelectCallBack) {
        mOnLibItemSelectCallBack = onLibItemSelectCallBack;
    }

    public LibItemAdapter(Activity activity, List<LibraryBean> libraryBeen) {
        mActivity = activity;
        mLibraryBeen = libraryBeen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_library_info, parent, false);

        return new ViewHolder(view);
    }

    public List<LibraryBean> getLibraryBeen() {
        return mLibraryBeen;
    }

    public void setLibraryBeen(List<LibraryBean> libraryBeen) {
        mLibraryBeen = libraryBeen;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final LibraryBean libraryBean = mLibraryBeen.get(position);

        holder.mLibTitleTextView.setText(libraryBean.getName());
        holder.mLibAuthorTextview.setText(libraryBean.getAuthor());
        holder.mLibPressTextView.setText(libraryBean.getPress());
        holder.mLibTimeTextView.setText(libraryBean.getPressDate());

        int allNum = libraryBean.getAllNum();
        int nowNum = libraryBean.getNowNum();

        if (nowNum == 0) {
            holder.mLibNumStateTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_lib_no_num));
        } else {
            holder.mLibNumStateTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_lib_num));
        }

        holder.mLibNumStateTextView.setText(nowNum + " / " + allNum);

        holder.mLibCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLibItemSelectCallBack != null) {
                    mOnLibItemSelectCallBack.onLibSelect(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mLibraryBeen == null) {
            return 0;
        }
        return mLibraryBeen.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        List<LibraryBean> mLibraryBeen;
        @BindView(R.id.libTitleTextView)
        TextView mLibTitleTextView;
        @BindView(R.id.libAuthorTextview)
        TextView mLibAuthorTextview;
        @BindView(R.id.libPressTextView)
        TextView mLibPressTextView;
        @BindView(R.id.libTimeTextView)
        TextView mLibTimeTextView;
        @BindView(R.id.libCardItem)
        CardView mLibCardItem;
        @BindView(R.id.libNumStateTextView)
        TextView mLibNumStateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
