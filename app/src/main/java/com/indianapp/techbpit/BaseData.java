package com.indianapp.techbpit;

public class BaseData<T> {
    private T object;

    public BaseData(T object) {
        this.object = object;
    }

    public T getBaseData() {
        return object;
    }
}
