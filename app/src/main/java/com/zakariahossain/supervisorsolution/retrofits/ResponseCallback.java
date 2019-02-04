package com.zakariahossain.supervisorsolution.retrofits;

public interface ResponseCallback<T> {
    void onSuccess(T data);
    void onError(Throwable th);
}
