package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.activity.OADetailActivity;
import com.example.daidaijie.syllabusapplication.bean.OABean;
import com.example.daidaijie.syllabusapplication.bean.OARead;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by daidaijie on 2016/8/23.
 */
public class OAItemAdapter extends RecyclerView.Adapter<OAItemAdapter.ViewHolder> {

    Activity mActivity;

    List<OABean> mOABeen;


    public OAItemAdapter(Activity activity, List<OABean> OABeen) {
        mActivity = activity;
        mOABeen = OABeen;
    }

    public List<OABean> getOABeen() {
        return mOABeen;
    }

    public void setOABeen(List<OABean> OABeen) {
        mOABeen = OABeen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_oa_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final OABean oaBean = mOABeen.get(position);

        if (oaBean.isRead()) {
            holder.mOATitleTextView.setTextColor(ColorUtils.setAlphaComponent(ThemeModel.getInstance().colorPrimary, 136));
            holder.mOASubTextView.setTextColor(ColorUtils.setAlphaComponent(
                    mActivity.getResources().getColor(R.color.defaultTextColor), 136));
        } else {
            holder.mOATitleTextView.setTextColor(ThemeModel.getInstance().colorPrimary);
            holder.mOASubTextView.setTextColor(mActivity.getResources().getColor(R.color.defaultTextColor));
        }

        holder.mOASubTextView.setText("" + oaBean.getSUBCOMPANYNAME());
        holder.mOATimeTextView.setText("" + oaBean.getDOCVALIDDATE() + " "
                + oaBean.getDOCVALIDTIME().substring(0, oaBean.getDOCVALIDTIME().length() - 3));
        holder.mOATitleTextView.setText("" + oaBean.getDOCSUBJECT());

        holder.mOaCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = OADetailActivity.getIntent(mActivity, oaBean);
                mActivity.startActivity(intent);
                if (oaBean.isRead()) {
                    return;
                }

                OARead.save(oaBean);
                oaBean.setRead(true);
                holder.mOATitleTextView.setTextColor(ColorUtils.setAlphaComponent(ThemeModel.getInstance().colorPrimary, 136));
                holder.mOASubTextView.setTextColor(ColorUtils.setAlphaComponent(
                        mActivity.getResources().getColor(R.color.defaultTextColor), 136));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mOABeen == null) {
            return 0;
        }
        return mOABeen.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.oASubTextView)
        TextView mOASubTextView;
        @BindView(R.id.oATimeTextView)
        TextView mOATimeTextView;
        @BindView(R.id.oATitleTextView)
        TextView mOATitleTextView;
        @BindView(R.id.oaCardItem)
        CardView mOaCardItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
