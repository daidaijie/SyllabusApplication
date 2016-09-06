package com.example.daidaijie.syllabusapplication.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.model.LibraryModel;
import com.example.daidaijie.syllabusapplication.service.LibraryService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class LibraryFragment extends Fragment {

    List<LibraryBean> mLibraryBeen;
    @BindView(R.id.text)
    TextView mText;

    private int mPosition;

    private String mkeyword;

    private String mTag;

    private static final String EXTRA_POS = "com.example.daidaijie.syllabusapplication.activity" +
            ".LibraryFragment.mPosition";

    private static final String EXTRA_TAG = "com.example.daidaijie.syllabusapplication.activity" +
            ".LibraryFragment.mTag";

    private static final String EXTRA_KEYWORD = "com.example.daidaijie.syllabusapplication.activity" +
            ".LibraryFragment.mkeyword";


    public static LibraryFragment newInstance(String tag, String keyword, int position) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_POS, position);
        args.putString(EXTRA_TAG, tag);
        args.putString(EXTRA_KEYWORD, keyword);
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        ButterKnife.bind(this, view);

        try {
            final LibraryService libraryService = LibraryModel.getInstance().mRetrofit.create(LibraryService.class);

            LibraryModel.getInstance().getLibraryBy(mTag, mkeyword, mPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .map(new Func1<String, List<LibraryBean>>() {
                        @Override
                        public List<LibraryBean> call(String s) {
                            List<LibraryBean> libraryBeen = new ArrayList<LibraryBean>();

                            Element body = Jsoup.parseBodyFragment(s).body();
                            Element table = body.select("table.tb").first();
                            Elements items = table.getElementsByTag("tr");
                            items.remove(0);

                            for (Element item : items) {
                                LibraryBean libraryBean = new LibraryBean();

                                Elements tdItem = item.getElementsByTag("td");

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
                            StringBuilder sb = new StringBuilder();
                            for (LibraryBean libraryBean : mLibraryBeen) {
                                sb.append(libraryBean.getName() + "\n");
                            }
                            mText.setText(sb.toString());
                        }

                        @Override
                        public void onError(Throwable e) {
                            mText.setText(e.getMessage());
                        }

                        @Override
                        public void onNext(List<LibraryBean> libraryBeen) {
                            mLibraryBeen = libraryBeen;
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return view;
    }

}
