package com.example.asionbo.myapplication.utils;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;


/**
 * Created by asionbo on 2017/10/13.
 */

public enum  RxBus {
    INSTANCE;
    private Relay<Object> mBus = null;

    RxBus() {
        mBus = PublishRelay.create().toSerialized();
    }

    public void post(Object event){
        mBus.accept(event);
    }

    public <T> Observable<T> tObserverable(Class<T> eventType){
        return mBus.ofType(eventType);
    }

    public io.reactivex.Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }
}
