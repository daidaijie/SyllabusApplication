package com.example.daidaijie.syllabusapplication.bean;

import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by daidaijie on 2016/9/24.
 */

public class TakeOutInfoBean extends RealmObject {

    /**
     * condition : 20元起送
     * createdAt : 2016-09-24 19:45:22
     * long_number : 15876151250
     * name : 北味家园
     * objectId : 458036b090
     * short_number : 661250
     * updatedAt : 2016-09-24 19:45:22
     */

    @PrimaryKey
    private String objectId;

    private String condition;
    private String createdAt;
    private String long_number;
    private String name;
    private String short_number;
    private String updatedAt;
    private String menu;

    @Ignore
    private TakeOutBuyBean mTakeOutBuyBean;

    @Ignore
    private List<TakeOutSubMenu> mTakeOutSubMenus;

    @Ignore
    private List<Dishes> mDishes;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLong_number() {
        return long_number;
    }

    public void setLong_number(String long_number) {
        this.long_number = long_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getShort_number() {
        return short_number;
    }

    public void setShort_number(String short_number) {
        this.short_number = short_number;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 将menu的json数据转化为List
     *
     * @return 是否存在menu
     */
    public boolean loadTakeOutSubMenus() {
        if (menu == null || menu.trim().isEmpty()) {
            return false;
        }
        mTakeOutSubMenus = GsonUtil.getDefault().fromJson(menu, new TypeToken<List<TakeOutSubMenu>>() {
        }.getType());
        mDishes = new ArrayList<>();
        for (int i = 0; i < mTakeOutSubMenus.size(); i++) {
            TakeOutSubMenu subMenu = mTakeOutSubMenus.get(i);
            for (int j = 0; j < subMenu.getDishes().size(); j++) {
                if (j == 0) subMenu.setFirstItemPos(mDishes.size());
                Dishes dishes = subMenu.getDishes().get(j);
                dishes.sticky = subMenu.getName();
                dishes.subMenuPos = i;
                dishes.mPos = mDishes.size();
                mDishes.add(dishes);
            }
        }
        return true;
    }

    public List<TakeOutSubMenu> getTakeOutSubMenus() {
        return mTakeOutSubMenus;
    }

    public void setTakeOutSubMenus(List<TakeOutSubMenu> takeOutSubMenus) {
        mTakeOutSubMenus = takeOutSubMenus;
    }

    public List<Dishes> getDishes() {
        if (mDishes == null) {
            this.loadTakeOutSubMenus();
        }
        return mDishes;
    }

    public void setDishes(List<Dishes> dishes) {
        mDishes = dishes;
    }

    public TakeOutBuyBean getTakeOutBuyBean() {
        if (mTakeOutBuyBean == null) {
            mTakeOutBuyBean = new TakeOutBuyBean();
        }
        return mTakeOutBuyBean;
    }

    public void setTakeOutBuyBean(TakeOutBuyBean takeOutBuyBean) {
        mTakeOutBuyBean = takeOutBuyBean;
    }

    public String[] getPhoneList() {
        List<String> stringList = new ArrayList<>();
        String[] longNums = long_number.split("/");
        for (String phone : longNums) {
            phone = phone.trim();
            if (!phone.isEmpty()) {
                stringList.add(phone);
            }
        }
        String[] shortNums = short_number.split("/");
        for (String phone : shortNums) {
            phone = phone.trim();
            if (!phone.isEmpty()) {
                stringList.add(phone);
            }
        }

        String[] phones = new String[stringList.size()];

        stringList.toArray(phones);

        return phones;
    }
}
