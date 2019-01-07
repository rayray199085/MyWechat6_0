package com.project.stephencao.mywechat6_0.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.project.stephencao.mywechat6_0.R;

public class MyBottomBarView extends View {
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
    private Bitmap mIcon;
    private int mColor = Color.parseColor("#ff45e01a");
    private String mTextContent = "Wechat";
    private Canvas mCanvas;
    private Paint mPaint;
    private Bitmap mBitmap;
    private float mAlpha;
    private Rect mIconRect;
    private Rect mTextBound;
    private Paint mTextPaint;
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_ALPHA = "status_alpha";

    public MyBottomBarView(Context context) {
        this(context, null);
    }

    public MyBottomBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyBottomBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyBottomBarView);
        int indexCount = array.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.MyBottomBarView_icon: {
                    BitmapDrawable drawable = (BitmapDrawable) array.getDrawable(attr);
                    mIcon = drawable.getBitmap();
                    break;
                }
                case R.styleable.MyBottomBarView_color: {
                    mColor = array.getColor(attr, Color.parseColor("#ff45e01a"));
                    break;
                }
                case R.styleable.MyBottomBarView_text: {
                    mTextContent = array.getString(attr);
                    break;
                }
                case R.styleable.MyBottomBarView_text_size: {
                    mTextSize = array.getDimension(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    break;
                }

            }
        }
        array.recycle();
        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.parseColor("#ff555555"));
        mTextPaint.getTextBounds(mTextContent, 0, mTextContent.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingBottom() - getPaddingTop() - mTextBound.height());
        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = (getMeasuredHeight() - mTextBound.height()) / 2 - iconWidth / 2;
        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mIcon, null, mIconRect, null);
        int alpha = (int) Math.ceil(255 * mAlpha); // 0~255
        prepareTargetBitmap(alpha);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        drawSourceText(canvas, alpha);
        drawTargetText(canvas, alpha);
    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        canvas.drawText(mTextContent, mIconRect.left + (mIconRect.width() - mTextBound.width()) / 2.0f,
                mIconRect.bottom + mTextBound.height(),
                mTextPaint);
    }

    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(Color.parseColor("#ff555555"));
        mTextPaint.setAlpha(255 - alpha);
        canvas.drawText(mTextContent, mIconRect.left + (mIconRect.width() - mTextBound.width()) / 2.0f,
                mIconRect.bottom + mTextBound.height(),
                mTextPaint);
    }

    private void prepareTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIcon, null, mIconRect, mPaint);
    }

    public void setIconAndTextAlphaValue(float value) {
        mAlpha = value;
        invalidateView();
    }

    private void invalidateView() {
        // check whether or not in the UI thread
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getFloat(STATUS_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState()); // save system instance data
        bundle.putFloat(STATUS_ALPHA, mAlpha);
        return bundle;
    }
}
