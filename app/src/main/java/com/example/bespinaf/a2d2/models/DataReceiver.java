package com.example.bespinaf.a2d2.models;

import java.util.HashMap;

public interface DataReceiver{

    default void onDataChanged(DataSource dataSource, HashMap<String, Object> data){}
}
