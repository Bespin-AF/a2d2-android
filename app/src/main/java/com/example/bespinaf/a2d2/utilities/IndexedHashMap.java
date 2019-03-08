package com.example.bespinaf.a2d2.utilities;

import com.example.bespinaf.a2d2.models.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class IndexedHashMap<KeyType, ValueType> extends LinkedHashMap<KeyType, ValueType> {
    ArrayList<KeyType> awesomeArrayList;


    public IndexedHashMap(){
        awesomeArrayList = new ArrayList();
    }


    @Override
    public void clear(){
        super.clear();
        awesomeArrayList.clear();
    }


    @Override
    public ValueType put(KeyType key, ValueType value){
        awesomeArrayList.add(key);
        return super.put(key, value);
    }


    @Override
    public ValueType remove(Object key){
        awesomeArrayList.remove(key);
        return super.remove(key);
    }


    public ValueType get(int index){
        return super.get(awesomeArrayList.get(index));
    }


    public ValueType remove(int index){
        return super.remove(awesomeArrayList.remove(index));
    }


    public ValueType set(int index, ValueType value){
        return super.put(awesomeArrayList.get(index), value);
    }
}
