package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
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

    public SubMenuAdapter(Activity activity, List<TakeOutSubMenu> subMenus) {
        mActivity = activity;
        mSubMenus = subMenus;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        TakeOutSubMenu subMenu = mSubMenus.get(position);
        holder.mSubMenuTitle.setText(subMenu.getName());
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
