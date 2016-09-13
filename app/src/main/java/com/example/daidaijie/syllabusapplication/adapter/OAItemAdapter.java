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
import com.example.daidaijie.syllabusapplication.activity.OADetailActivity;
import com.example.daidaijie.syllabusapplication.bean.OABean;
import com.example.daidaijie.syllabusapplication.bean.OARead;

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
        final Realm realm = Realm.getInstance(mActivity);

        OARead results = realm.where(OARead.class)
                .equalTo("id", oaBean.getID()).findFirst();
        if (results != null && results.isRead()) {
            holder.mOATitleTextView.setTextColor(mActivity.getResources().getColor(R.color.defaultShowColor));
        }

        holder.mOASubTextView.setText("" + oaBean.getSUBCOMPANYNAME());
        holder.mOATimeTextView.setText("" + oaBean.getDOCVALIDDATE() + " "
                + oaBean.getDOCVALIDTIME().substring(0, oaBean.getDOCVALIDTIME().length() - 3));
        holder.mOATitleTextView.setText("" + oaBean.getDOCSUBJECT());

        holder.mOaCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realm.beginTransaction();

                OARead oaRead = realm.createObject(OARead.class);
                oaRead.setId(oaBean.getID());
                oaRead.setRead(true);

                realm.commitTransaction();
                holder.mOATitleTextView.setTextColor(mActivity.getResources().getColor(R.color.defaultShowColor));
                Intent intent = OADetailActivity.getIntent(mActivity, oaBean);
                mActivity.startActivity(intent);
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
