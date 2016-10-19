package com.example.daidaijie.syllabusapplication.officeAutomation;

import com.example.daidaijie.syllabusapplication.bean.OABean;
import com.example.daidaijie.syllabusapplication.bean.OAFileBean;
import com.example.daidaijie.syllabusapplication.bean.OARead;
import com.example.daidaijie.syllabusapplication.bean.OASearchBean;
import com.example.daidaijie.syllabusapplication.retrofitApi.OAApi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/17.
 */

public class OAModel implements IOAModel {

    OAApi mOAApi;

    OASearchBean mOASearchBean;

    Map<Integer, List<OABean>> mOAListMap;

    Realm mRealm;

    public OAModel(OAApi OAApi, OASearchBean OASearchBean, Realm realm) {
        mOAApi = OAApi;
        mOASearchBean = OASearchBean;
        mRealm = realm;
        mOAListMap = new HashMap<>();
    }

    @Override
    public Observable<List<OABean>> getLibrary(final int position) {
        return Observable.concat(getLibraryFromMemory(position), getLibraryFromNet(position))
                .takeFirst(new Func1<List<OABean>, Boolean>() {
                    @Override
                    public Boolean call(List<OABean> oaBeen) {
                        return oaBeen != null;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<OABean>> getLibraryFromMemory(final int position) {
        return Observable.create(new Observable.OnSubscribe<List<OABean>>() {
            @Override
            public void call(Subscriber<? super List<OABean>> subscriber) {
                subscriber.onNext(mOAListMap.get(position));
                subscriber.onCompleted();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<OABean>> getLibraryFromNet(final int position) {
        return mOAApi.getOAInfo("undefined",
                mOASearchBean.getSubcompanyId(),
                mOASearchBean.getKeyword(),
                position * 10,
                (position + 1) * 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<OABean>, Observable<List<OABean>>>() {
                    @Override
                    public Observable<List<OABean>> call(List<OABean> oaBeen) {
                        Iterator<OABean> it = oaBeen.iterator();
                        while (it.hasNext()) {
                            final OABean oaBean = it.next();
                            if (oaBean.getDOCVALIDDATE() == null
                                    || oaBean.getDOCVALIDTIME() == null) {
                                it.remove();
                            }
                        }
                        setupRead(oaBeen);
                        mOAListMap.put(position, oaBeen);
                        return Observable.just(oaBeen);
                    }
                });
    }

    private void setupRead(final List<OABean> oaBeen) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (OABean oaBean : oaBeen) {
                    RealmResults<OARead> results =
                            realm.where(OARead.class).equalTo(
                                    "id", oaBean.getID()
                            ).findAll();
                    if (results.size() != 0) {
                        oaBean.setRead(results.first().isRead());
                    } else {
                        oaBean.setRead(false);
                    }
                }
            }
        });
    }

    @Override
    public void setRead(final OABean oaBean, final boolean isRead) {
        oaBean.setRead(isRead);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                OARead oaRead = new OARead(oaBean.getID(), isRead);
                mRealm.copyToRealmOrUpdate(oaRead);
            }
        });
    }

    @Override
    public void clearRead() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (List<OABean> oaBeen : mOAListMap.values()) {
                    for (OABean oaBean : oaBeen) {
                        oaBean.setRead(false);
                    }
                }
                mRealm.where(OARead.class).findAll().deleteAllFromRealm();
            }
        });
    }

    @Override
    public OABean getOABean(int position, int subPosition) {
        return mOAListMap.get(position).get(subPosition);
    }

    @Override
    public Observable<List<OAFileBean>> getOAFileListFromNet(int id) {
        return mOAApi.getOAFileList("undefined", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
