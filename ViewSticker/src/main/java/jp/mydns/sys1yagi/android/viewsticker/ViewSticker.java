package jp.mydns.sys1yagi.android.viewsticker;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import java.util.HashMap;
import java.util.Map;

public class ViewSticker {

    private final static String TAG = ViewSticker.class.getSimpleName();

    private static class Closure {

        View mTarget;

        ScrollView mMonitored;

        ViewGroup mStuffing;

        ViewGroup mRootView;

        Closure(View target, ScrollView monitored, ViewGroup stuffing, ViewGroup rootView) {
            mTarget = target;
            mMonitored = monitored;
            mStuffing = stuffing;
            mRootView = rootView;
        }

        void offset(int top) {
            if (top >= 0) {
                if (mStuffing.indexOfChild(mTarget) < 0) {
                    int access = top - mTarget.getHeight();
                    if (access <= 0) {
                        mTarget.layout(0, access, mTarget.getWidth(),
                                mTarget.getHeight() + access);
                    } else {
                        mTarget.layout(0, 0, mTarget.getWidth(),
                                mTarget.getHeight());
                    }
                }
            }
        }
    }

    private final static Map<View, Closure> sObjectMap = new HashMap<View, Closure>();

    private static ViewGroup wrap(final View target, final ScrollView monitored,
            final ViewGroup rootView) {
        ViewGroup parent = (ViewGroup) target.getParent();
        int index = parent.indexOfChild(target);
        parent.removeView(target);
        final int[] dimension = new int[2];

        FrameLayout stuffing = new FrameLayout(target.getContext()) {
            ViewTreeObserver.OnScrollChangedListener mObserver
                    = new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    monitored.getLocationOnScreen(dimension);
                    int offset = dimension[1];
                    getLocationOnScreen(dimension);
                    int top = dimension[1] - offset;
                    offset(target, top);
                    if (top < 0 && indexOfChild(target) >= 0) {
                        removeView(target);
                        rootView.addView(target);
                    } else if (top >= 0 && indexOfChild(target) < 0) {
                        rootView.removeView(target);
                        addView(target);
                    }
                }
            };

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
                int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
                measureChildWithMargins(target, widthMeasureSpec, 0, heightMeasureSpec, 0);
                setMeasuredDimension(widthSpecSize, heightSpecSize + target.getMeasuredHeight());
            }

            @Override
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                monitored.getViewTreeObserver().addOnScrollChangedListener(mObserver);
            }

            @Override
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                monitored.getViewTreeObserver().removeOnScrollChangedListener(mObserver);
            }
        };
        stuffing.addView(target);
        parent.addView(stuffing, index);
        return stuffing;
    }

    private static void offset(View target, int top) {
        for (Map.Entry<View, Closure> entry : sObjectMap.entrySet()) {
            if (!entry.getKey().equals(target)) {
                Closure closure = entry.getValue();
                closure.offset(top);
            }
        }
    }

    public static void stick(final View target, final ScrollView monitored,
            final ViewGroup rootView) {
        final ViewGroup stuffing = wrap(target, monitored, rootView);
        sObjectMap.put(target, new Closure(target, monitored, stuffing, rootView));
    }

    public static void peeler(final View target, final ScrollView monitored,
            final ViewGroup rootView) {
        //TODO
    }
}
