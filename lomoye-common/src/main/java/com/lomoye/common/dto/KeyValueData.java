package com.lomoye.common.dto;

/**
 * Created by tommy on 2015/5/31.
 */
public class KeyValueData<K, V> {
    private K key;
    private V value;

    public KeyValueData() {
    }

    public KeyValueData(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
