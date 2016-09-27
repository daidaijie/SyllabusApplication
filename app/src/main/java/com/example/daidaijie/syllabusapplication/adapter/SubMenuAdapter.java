package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.TakeOutSubMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/9/27.
 */

public class SubMenuAdapter extends RecyclerView.Adapter<SubMenuAdapter.ViewHolder> {

    Activity mActivity;

    List<TakeOutSubMenu> mSubMenus;

    private int selectItem;

    public interface OnItemClickListener {
        public void onClick(int position);
    }

    OnItemClickListener mOnItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    public SubMenuAdapter(Activity activity, List<TakeOutSubMenu> subMenus) {
        mActivity = activity;
        mSubMenus = subMenus;
        selectItem = 0;
    }

    public List<TakeOutSubMenu> getSubMenus() {
        return mSubMenus;
    }

    public void setSubMenus(List<TakeOutSubMenu> subMenus) {
        mSubMenus = subMenus;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_sub_menu, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TakeOutSubMenu subMenu = mSubMenus.get(position);
        if (selectItem == position) {
            holder.mSubMenuTitle.setText(Html.fromHtml("<b>" + subMenu.getName() + "</b>"));
            holder.mSubMenuLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.defaultLightBackground));
        } else {
            holder.mSubMenuTitle.setText(subMenu.getName());
            holder.mSubMenuLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.defaultDarkBackground));
        }

        holder.mSubMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mSubMenus == null) {
            return 0;
        }
        return mSubMenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subMenuTitle)
        TextView mSubMenuTitle;
        @BindView(R.id.subMenuLayout)
        LinearLayout mSubMenuLayout;
        @BindView(R.id.div_line)
        View mDivLine;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
