package com.likeits.sanyou.listeners;


public interface MyObserver<T> {

        void onHttpCompleted(T t);
        void onHttpError(Throwable e);

}
