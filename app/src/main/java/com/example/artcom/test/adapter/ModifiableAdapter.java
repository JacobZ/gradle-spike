package com.example.artcom.test.adapter;

import android.widget.ListAdapter;

interface ModifiableAdapter<T> extends ListAdapter {
    public void add(T item);
    public void addAtIndex(T item, int index);
    public void remove(T item);
}
