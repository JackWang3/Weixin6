package com.imooc.imooc_weixin6_0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ChangeColorIconWithText extends View {
	private int mColor = 0xFF45C01A;
	private Bitmap mIconBitmap;
	private String mText = "΢��";
	private int mTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Paint mPaint;
	private float mAlpha;
	private Rect mIconRect;
	private Rect mTextBound;
	private Paint mTextPaint;
	private static final String INSTANCE_STATUS = "intance_status";
	private static final String STATUS_ALPHA = "status_alpha";

	public ChangeColorIconWithText(Context context) {
		this(context, null);
	}

	public ChangeColorIconWithText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	/**
	 * ��ȡ�Զ������Ե�ֵ
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ChangeColorIconWithText(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = context.obtainStyledAttributes(
				attrs, R.styleable.ChangeColorIconWithText);
		int n = a.getIndexCount();
		
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.ChangeColorIconWithText_icon:
				BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
				mIconBitmap = drawable.getBitmap();
				break;
			case R.styleable.ChangeColorIconWithText_color:
				mColor = a.getColor(attr, 0xFF45C01A);
				break;
			case R.styleable.ChangeColorIconWithText_text:
				mText = a.getString(attr);
				break;
			case R.styleable.ChangeColorIconWithText_text_size:
				mTextSize = (int) a.getDimension(attr, TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 12, 
							getResources().getDisplayMetrics()));
				break;
			}
		}
		a.recycle();
		
		mTextBound = new Rect();
		mTextPaint = new Paint();
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(0Xff555555);
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), 
				getMeasuredHeight() - getPaddingTop() - 
				getPaddingBottom() - mTextBound.height());
		int left = getMeasuredWidth() / 2 - iconWidth / 2;
		int top = (getMeasuredHeight() - mTextBound.height()) / 2 -
				iconWidth / 2;
		mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
		int alpha = (int) Math.ceil(255 * mAlpha);
		//�ڴ���׼��mBitmap��setAlpha����ɫ��xfermode��ͼ��
		setupTargetBitmap(alpha);
		//1������ԭ�ı�; 2�����Ʊ�ɫ���ı�
		drawSourceText(canvas, alpha);
		drawTargetText(canvas, alpha);
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}
	
	/**
	 * ���Ʊ�ɫ���ı�
	 * @param canvas
	 * @param alpha
	 */
	private void drawTargetText(Canvas canvas, int alpha) {
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(alpha);
	}

	/**
	 * ����ԭ�ı�
	 * @param canvas
	 * @param alpha
	 */
	private void drawSourceText(Canvas canvas, int alpha) {
		mTextPaint.setColor(0xff333333);
		mTextPaint.setAlpha(255-alpha);
		int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
		int y = mIconRect.bottom + mTextBound.height();
		canvas.drawText(mText, x, y, mTextPaint);
	}

	/**
	 * ���ڴ��л��ƿɱ�ɫ��Icon
	 */
	private void setupTargetBitmap(int alpha) {
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setAlpha(alpha);
		mCanvas.drawRect(mIconRect, mPaint);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
	}
	
	public void setIconAlpha(float alpha) {
		this.mAlpha = alpha;
		invalidateView();
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
		bundle.putFloat(STATUS_ALPHA, mAlpha);
		return bundle;
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
	
	/**
	 * �ػ�
	 */
	private void invalidateView() {
		if (Looper.getMainLooper() == Looper.myLooper()) {
			invalidate();
		} else {
			postInvalidate();
		}
	}
}
