/**
 Copyright 2013 Toshihiro.Yagi

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package jp.mydns.sys1yagi.android.viewsticker;

import android.app.Activity;
import android.support.v4.app.Fragment;
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

    private Activity mActivity;

    private Fragment mFragmentv4;

    private int mScrollViewId;

    private int mContainerId;

    private ViewGroup wrap(final View target, final ScrollView monitored,
            final ViewGroup container) {
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
                        container.addView(target);
                    } else if (top >= 0 && indexOfChild(target) < 0) {
                        container.removeView(target);
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

    private void offset(View target, int top) {
        for (Map.Entry<View, Closure> entry : sObjectMap.entrySet()) {
            if (!entry.getKey().equals(target)) {
                Closure closure = entry.getValue();
                closure.offset(top);
            }
        }
    }

    public static ViewSticker starch(Activity activity, int scrollViewId, int containerId) {
        ViewSticker sticker = new ViewSticker();

        sticker.mActivity = activity;
        sticker.mScrollViewId = scrollViewId;
        sticker.mContainerId = containerId;

        return sticker;
    }

    public static ViewSticker starch(Fragment fragmentv4, int scrollViewId, int containerId) {
        ViewSticker sticker = new ViewSticker();

        sticker.mFragmentv4 = fragmentv4;
        sticker.mScrollViewId = scrollViewId;
        sticker.mContainerId = containerId;

        return sticker;
    }

    private View findViewById(int id) {
        if (mActivity != null) {
            return mActivity.findViewById(id);
        } else if (mFragmentv4 != null) {
            return mFragmentv4.getView().findViewById(id);
        }
        return null;
    }

    public void stick(final View target) {
        FrameLayout container = (FrameLayout) findViewById(mContainerId);
        ScrollView monitored = (ScrollView) findViewById(mScrollViewId);
        final ViewGroup stuffing = wrap(target, monitored, container);
        sObjectMap.put(target, new Closure(target, monitored, stuffing, container));
    }

    public void peeler(final View target) {
        //TODO
    }
}
