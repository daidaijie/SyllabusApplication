package com.example.daidaijie.syllabusapplication.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThemePickerFragment extends DialogFragment {

    public static final String DIALOG_THEME_PICKER = ThemePickerFragment.class.getCanonicalName();

    private ThemePickerAdapter mThemePickerAdapter;

    public interface OnItemClickListener {
        void onClick(String name);
    }

    OnItemClickListener mOnItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_theme_picker, null);
        RecyclerView mThemePickerRecyclerView = (RecyclerView) view.findViewById(R.id.themePickerRecyclerView);

        mThemePickerAdapter = new ThemePickerAdapter(getActivity());
        mThemePickerRecyclerView.setAdapter(mThemePickerAdapter);
        mThemePickerRecyclerView.setLayoutManager(new GridLayoutManager(
                getActivity(), 5
        ));


        mThemePickerAdapter.setOnItemClickListener(new ThemePickerAdapter.OnItemClickListener() {
            @Override
            public void onClick(String name) {
                mOnItemClickListener.onClick(name);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("选择主题")
                .setView(view)
                .setPositiveButton("取消", null)
                .create();
    }


    public static class ThemePickerAdapter extends RecyclerView.Adapter<ThemePickerAdapter.ViewHolder> {

        private Activity mActivity;

        public ThemePickerAdapter(Activity activity) {
            mActivity = activity;
        }

        public interface OnItemClickListener {
            void onClick(String name);
        }

        OnItemClickListener mOnItemClickListener;

        public OnItemClickListener getOnItemClickListener() {
            return mOnItemClickListener;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            View view = inflater.inflate(R.layout.item_theme_picker, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final ThemeUtil.ThemeBean themeBean = ThemeUtil.getInstance().mThemeBeen.get(position);
            GradientDrawable shape = (GradientDrawable) mActivity.getResources()
                    .getDrawable(R.drawable.bg_theme_picker);

            shape.setColor(mActivity.getResources().getColor(themeBean.colorPrimary));
            holder.mThemePickerView.setBackground(shape);
            holder.mThemePickerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(themeBean.name);
                }
            });
            holder.mShowCurrent.setVisibility(themeBean.name.equals(ThemeUtil.getInstance().mCurrentThemeName) ?
                    View.VISIBLE : View.GONE);

        }

        @Override
        public int getItemCount() {
            return ThemeUtil.getInstance().mThemeBeen.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.themePickerView)
            View mThemePickerView;
            @BindView(R.id.showCurrent)
            ImageView mShowCurrent;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


}
