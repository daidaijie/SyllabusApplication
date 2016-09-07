package com.example.daidaijie.syllabusapplication.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.LibItemAdapter;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.model.LibraryModel;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.RecyclerViewEmptySupport;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.libRecyclerView)
    RecyclerViewEmptySupport mLibRecyclerView;
    @BindView(R.id.emptyTextView)
    TextView mEmptyTextView;
    @BindView(R.id.refreshLibLayout)
    SwipeRefreshLayout mRefreshLibLayout;

    private int mPosition;

    private String mkeyword;

    private String mTag;

    private String mOB;

    private String mSF;

    private List<LibraryBean> mLibraryBeen;

    private LibItemAdapter mLibItemAdapter;

    private static final String EXTRA_POS = "com.example.daidaijie.syllabusapplication.activity" +
            ".LibraryFragment.mPosition";

    private static final String EXTRA_TAG = "com.example.daidaijie.syllabusapplication.activity" +
            ".LibraryFragment.mTag";

    private static final String EXTRA_KEYWORD = "com.example.daidaijie.syllabusapplication.activity" +
            ".LibraryFragment.mkeyword";

    private static final String EXTRA_OB = "com.example.daidaijie.syllabusapplication.activity" +
            ".LibraryFragment.mOB";

    private static final String EXTRA_SF = "com.example.daidaijie.syllabusapplication.activity" +
            ".LibraryFragment.mSF";


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
        mkeyword = args.getString(EXTRA_KEYWORD);
        mTag = args.getString(EXTRA_TAG);
        mSF = args.getString(EXTRA_SF);
        mOB = args.getString(EXTRA_OB);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        ButterKnife.bind(this, view);

        mLibRecyclerView.setEmptyView(mEmptyTextView);
        mLibItemAdapter = new LibItemAdapter(getActivity(), mLibraryBeen);
        mLibRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLibRecyclerView.setAdapter(mLibItemAdapter);

        mRefreshLibLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLibLayout.setOnRefreshListener(this);
        mRefreshLibLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLibLayout.setRefreshing(true);
                getLibInfo();
            }
        }, 50);


        return view;
    }


    public void getLibInfo() {
        try {
            LibraryModel.getInstance().getLibraryBy(mTag, mkeyword, mSF, mOB, mPosition + 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .map(new Func1<String, List<LibraryBean>>() {
                        @Override
                        public List<LibraryBean> call(String s) {
                            List<LibraryBean> libraryBeen = new ArrayList<>();

                            Element body = Jsoup.parseBodyFragment(s).body();
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
                        }

                        @Override
                        public void onError(Throwable e) {
                            mRefreshLibLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
