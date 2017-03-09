package com.chinamobile.shop.utils;

import android.content.Context;
import android.util.SparseArray;

import com.chinamobile.shop.bean.ShoppingCart;
import com.chinamobile.shop.bean.Wares;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于保存购物车数据
 */
public class CartProvider {

    private static CartProvider mInstance;

    public static final String CART_JSON="cart_json";

    private SparseArray<ShoppingCart> datas =null;

    private  Context mContext;

    private CartProvider(Context context){

        mContext = context;
       datas = new SparseArray<>(10);
        listToSparse();
    }

    public static CartProvider getmInstance(Context context){
        if (mInstance == null){
            mInstance = new CartProvider(context);
        }
        return mInstance;
    }

    /**
     * 购物车添加数据
     * @param cart
     */
    public void put(ShoppingCart cart){

       ShoppingCart temp =  datas.get(cart.getId().intValue());

        if(temp !=null){
            temp.setCount(temp.getCount()+1);
        }
        else{
            temp = cart;
            temp.setCount(1);
        }
        datas.put(cart.getId().intValue(),temp);

        commit();

    }

    /**
     * 购物车添加数据
     * @param wares
     */
    public void put(Wares wares){

        ShoppingCart temp =  convertData(wares);

        put(temp);

    }

    /**
     * 购物车更新数据
     * @param cart
     */
    public void update(ShoppingCart cart){

        datas.put(cart.getId().intValue(),cart);
        commit();
    }

    /**
     * 购物车删除数据
     * @param cart
     */
    public void delete(ShoppingCart cart){
        datas.delete(cart.getId().intValue());
        commit();
    }

    /**
     * 获取购物车所有数据
     * @return
     */
    public List<ShoppingCart> getAll(){

        return  getDataFromLocal();
    }

    /**
     * 将SparseArray据添加到内存
     */
    public void commit(){
        List<ShoppingCart> carts = sparseToList();

        PreferencesUtils.putString(mContext,CART_JSON,JSONUtil.toJSON(carts));
    }


    /**
     * 将SparseArray转化成List
     * @return
     */
    private List<ShoppingCart> sparseToList(){

        int size = datas.size();

        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i=0;i<size;i++){

            list.add(datas.valueAt(i));
        }
        return list;

    }

    /**
     * 将本地的数据添加到SparseArray
     */
    private void listToSparse(){

        List<ShoppingCart> carts =  getDataFromLocal();

        if(carts!=null && carts.size()>0){

            for (ShoppingCart cart:carts) {

                datas.put(cart.getId().intValue(),cart);
            }
        }

    }

    /**
     * 从SharePreference获取本地数据
     * @return
     */
    public  List<ShoppingCart> getDataFromLocal(){

        String json = PreferencesUtils.getString(mContext,CART_JSON);
        List<ShoppingCart> carts = new ArrayList<>();
        if(json !=null ){

            carts = JSONUtil.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());

        }
        return  carts;

    }

    public ShoppingCart convertData(Wares item){

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }

}
