package com.example.daidaijie.syllabusapplication.stuLibrary;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.LibItemAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseFragment;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.event.LibPageCountEvent;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.libRecyclerView)
    RecyclerView mLibRecyclerView;
    @BindView(R.id.emptyTextView)
    TextView mEmptyTextView;
    @BindView(R.id.refreshLibLayout)
    SwipeRefreshLayout mRefreshLibLayout;

    private int mPosition;

    private String mKeyword;

    private String mTag;

    private String mOB;

    private String mSF;

    private List<LibraryBean> mLibraryBeen;

    private LibItemAdapter mLibItemAdapter;

    public static final String CLASS_NAME = "com.example.daidaijie.syllabusapplication" +
            ".stuLibrary.LibraryFragment";

    private static final String EXTRA_POS = CLASS_NAME + ".mPosition";

    private static final String EXTRA_TAG = CLASS_NAME + ".mTag";

    private static final String EXTRA_KEYWORD = CLASS_NAME + ".mKeyword";

    private static final String EXTRA_OB = CLASS_NAME + ".mOB";

    private static final String EXTRA_SF = CLASS_NAME + ".mSF";


    public static LibraryFragment newInstance(String tag, String keyword, String sf, String ob, int position) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_POS, position);
        args.putString(EXTRA_TAG, tag);
        args.putString(EXTRA_KEYWORD, keyword);
        args.putString(EXTRA_SF, sf);
        args.putString(EXTRA_OB, ob);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mPosition = args.getInt(EXTRA_POS, 0);
        mKeyword = args.getString(EXTRA_KEYWORD);
        mTag = args.getString(EXTRA_TAG);
        mSF = args.getString(EXTRA_SF);
        mOB = args.getString(EXTRA_OB);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setupSwipeRefreshLayout(mRefreshLibLayout);
        mRefreshLibLayout.setOnRefreshListener(this);

        if (LibraryModel.getInstance().mStoreQueryMap.get(mPosition) == null) {
            mRefreshLibLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLibLayout.setRefreshing(true);
                    getLibInfo();
                }
            }, 50);
        } else {
            mLibraryBeen = LibraryModel.getInstance().mStoreQueryMap.get(mPosition);
        }

        mLibItemAdapter = new LibItemAdapter(mActivity, mLibraryBeen);
        mLibRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mLibRecyclerView.setAdapter(mLibItemAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_library;
    }


    public void getLibInfo() {
        try {
            mEmptyTextView.setText("");
            LibraryModel.getInstance().getLibraryBy(mTag, mKeyword, mSF, mOB, mPosition + 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .map(new Func1<String, List<LibraryBean>>() {
                        @Override
                        public List<LibraryBean> call(String s) {
                            List<LibraryBean> libraryBeen = new ArrayList<>();

                            Element body = Jsoup.parseBodyFragment(s).body();

                            if (!LibraryModel.getInstance().isGetCount) {
                                Element countString = body.select("span#ctl00_ContentPlaceHolder1_countlbl").first();

                                if (!countString.text().trim().isEmpty()) {
                                    EventBus.getDefault().post(new LibPageCountEvent(Integer.parseInt(countString.text().trim())));
                                }

                            }
                            Element table = body.select("table.tb").first();
                            Elements items = table.getElementsByTag("tr");
                            items.remove(0);

                            for (Element item : items) {
                                LibraryBean libraryBean = new LibraryBean();

                                Elements tdItem = item.getElementsByTag("td");

                                Element href = tdItem.get(1).getElementsByTag("span").first().getElementsByTag("a").first();
                                libraryBean.setUrl(href.attr("href"));

                                libraryBean.setName(tdItem.get(1).text());
                                libraryBean.setAuthor(tdItem.get(2).text());
                                libraryBean.setPress(tdItem.get(3).text());
                                libraryBean.setPressDate(tdItem.get(4).text());
                                libraryBean.setTakeNum(tdItem.get(5).text());
                                libraryBean.setAllNum(Integer.parseInt(tdItem.get(6).text()));
                                libraryBean.setNowNum(Integer.parseInt(tdItem.get(7).text()));
                                libraryBeen.add(libraryBean);
                            }
                            return libraryBeen;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<LibraryBean>>() {
                        @Override
                        public void onCompleted() {
                            mRefreshLibLayout.setRefreshing(false);
                            mLibItemAdapter.setLibraryBeen(mLibraryBeen);
                            mLibItemAdapter.notifyDataSetChanged();
                            LibraryModel.getInstance().mStoreQueryMap.put(mPosition, mLibraryBeen);
                            if (mLibraryBeen.size() == 0) {
                                mEmptyTextView.setText("查找不到对应图书");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mRefreshLibLayout.setRefreshing(false);
                            SnackbarUtil.LongSnackbar(
                                    mRefreshLibLayout, "获取失败", SnackbarUtil.Alert
                            ).show();
                        }

                        @Override
                        public void onNext(List<LibraryBean> libraryBeen) {
                            mLibraryBeen = libraryBeen;
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        getLibInfo();
    }
}
