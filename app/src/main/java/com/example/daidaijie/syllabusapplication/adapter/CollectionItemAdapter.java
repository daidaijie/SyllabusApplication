package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.CollectionBean;
import com.example.daidaijie.syllabusapplication.bean.CollectionInfo;
import com.example.daidaijie.syllabusapplication.bean.Semester;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/9/17.
 */
public class CollectionItemAdapter extends RecyclerView.Adapter<CollectionItemAdapter.ViewHolder> {

    private Activity mActivity;

    private CollectionInfo mCollectionInfo;

    public CollectionItemAdapter(Activity activity, CollectionInfo collectionInfo) {
        mActivity = activity;
        mCollectionInfo = collectionInfo;
    }

    public CollectionInfo getCollectionInfo() {
        return mCollectionInfo;
    }

    public void setCollectionInfo(CollectionInfo collectionInfo) {
        mCollectionInfo = collectionInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_collection, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        position = mCollectionInfo.getCollection_ids().size() - position - 1;
        final CollectionBean collectionBean = mCollectionInfo.getCollection_ids().get(position);
        holder.mCollectionIdTextView.setText("编号 : " + collectionBean.getCollection_id());
        Semester semester = new Semester(collectionBean.getStart_year(), collectionBean.getSeason());
        holder.mCollectionSemesterTextView.setText("学期 : "
                + semester.getYearString() + " " + semester.getSeasonString());

        holder.mCardItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String[] items = new String[]{"复制编号"};

                AlertDialog dialog = new AlertDialog.Builder(mActivity)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    ClipboardManager myClipboard;
                                    myClipboard = (ClipboardManager)
                                            mActivity.getSystemService(mActivity.CLIPBOARD_SERVICE);
                                    ClipData clipData;
                                    clipData = ClipData.newPlainText("text"
                                            , collectionBean.getCollection_id());
                                    myClipboard.setPrimaryClip(clipData);
                                }
                            }
                        })
                        .create();
                dialog.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mCollectionInfo == null) {
            return 0;
        }
        return mCollectionInfo.getCollection_ids().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.collectionIdTextView)
        TextView mCollectionIdTextView;
        @BindView(R.id.collectionSemesterTextView)
        TextView mCollectionSemesterTextView;
        @BindView(R.id.collectionNumTextView)
        TextView mCollectionNumTextView;
        @BindView(R.id.cardItemLayout)
        CardView mCardItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
