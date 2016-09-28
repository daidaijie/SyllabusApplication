package com.example.daidaijie.syllabusapplication.bean;

import com.example.daidaijie.syllabusapplication.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by daidaijie on 2016/9/28.
 */

public class TakeOutBuyBean {

    private int num;

    private int sumPrice;

    private int unCalcNum;

    private Map<Dishes,Integer> mBuyMap;

    public TakeOutBuyBean() {
        num = 0;
        sumPrice = 0;
        unCalcNum = 0;
        mBuyMap = new LinkedHashMap<>();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Map<Dishes, Integer> getBuyMap() {
        return mBuyMap;
    }

    public void setBuyMap(Map<Dishes, Integer> buyMap) {
        mBuyMap = buyMap;
    }

    public void addDishes(Dishes dishes){
        if (mBuyMap.get(dishes) != null) {
            mBuyMap.put(dishes, mBuyMap.get(dishes) + 1);
        } else {
            mBuyMap.put(dishes, 1);
        }
        num++;
        if (StringUtil.isNumberic(dishes.getPrice())){
            sumPrice += Integer.parseInt(dishes.getPrice());
        }else{
            unCalcNum++;
        }
    }

    public void removeDishes(Dishes dishes){
        if (mBuyMap.get(dishes) != null) {
            mBuyMap.put(dishes, mBuyMap.get(dishes) - 1);
            if (mBuyMap.get(dishes) <= 0) {
                mBuyMap.remove(dishes);
            }
            num--;
            if (StringUtil.isNumberic(dishes.getPrice())){
                sumPrice -= Integer.parseInt(dishes.getPrice());
            }else{
                unCalcNum--;
            }
        }
    }

    public int getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(int sumPrice) {
        this.sumPrice = sumPrice;
    }

    public int getUnCalcNum() {
        return unCalcNum;
    }

    public void setUnCalcNum(int unCalcNum) {
        this.unCalcNum = unCalcNum;
    }
}
