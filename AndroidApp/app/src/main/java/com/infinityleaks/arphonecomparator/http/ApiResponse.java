package com.infinityleaks.arphonecomparator.http;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {

    private int id;

    private T data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
