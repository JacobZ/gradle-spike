package com.example.artcom.test.adapter;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

public class ListViewHandler<T> {


    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 15000;

    private ListView mListView;
    private ModifiableAdapter mAdapter;
    private Map<Long, Integer> mItemIdTopMap;

    public ListViewHandler(ListView mListView, ModifiableAdapter mAdapter) {
        this.mListView = mListView;
        this.mAdapter = mAdapter;
        mItemIdTopMap = new HashMap<Long, Integer>();
    }

    public void removeItem(View view, T item) {
//        mAdapter.remove(item);
        animateRemoval(view, item);
    }

    public void addItem(View view, T item) {
        mAdapter.add(item);
    }

    private void gatherItemTopCoordinate(View viewToRemove) {
        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        for (int i = 0; i < mListView.getChildCount(); ++i) {
            View child = mListView.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = mAdapter.getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
    }

    private void animateRemoval(final View viewToRemove, final T item) {
        gatherItemTopCoordinate(viewToRemove);

//        viewToRemove.animate().scaleY(0f).setDuration(MOVE_DURATION).withEndAction(new Runnable() {
//            @Override
//            public void run() {
//                mAdapter.remove(item);
//            }
//        });

        final ViewTreeObserver observer = mListView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = mListView.getFirstVisiblePosition();
                for (int i = 0; i < mListView.getChildCount(); ++i) {
                    final View child = mListView.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = mAdapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        mListView.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + mListView.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    mListView.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }
}
