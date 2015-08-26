package doext.dottedprogress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by igortrncic on 6/18/15.
 */
public class DottedProgressBar extends View {
    private float mDotSize;
    private float mSpacing;
    private int mJumpingSpeed;
    private int mEmptyDotsColor;
    private int mActiveDotColor;
    private Drawable mActiveDot;
    private Drawable mInactiveDot;

    private boolean isInProgress;
    private boolean isActiveDrawable = false;
    private boolean isInactiveDrawable = false;

    private int mActiveDotIndex;

    private int mNumberOfDots;
    private Paint mPaint;
    private int mPaddingLeft;
    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mNumberOfDots != 0)
                mActiveDotIndex = (mActiveDotIndex + 1) % mNumberOfDots;
            DottedProgressBar.this.invalidate();
            mHandler.postDelayed(mRunnable, mJumpingSpeed);
        }
    };


    public DottedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DottedProgressBar(Context mContext,DottedProgressEntity entity) {
		super(mContext);
		 isInProgress = false;
         mHandler = new Handler();
         isActiveDrawable = true;
         isInactiveDrawable = true;
         mInactiveDot = new BitmapDrawable(entity.getDefaultImage());
         mActiveDot = new BitmapDrawable(entity.getChangeImage());
         mNumberOfDots = entity.getPointNum();
         mSpacing = 0;
         mActiveDotIndex = 0;
         mJumpingSpeed = 500;
         mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
         mPaint.setStyle(Paint.Style.FILL);
	}

	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mNumberOfDots; i++) {
            int x = (int) (getPaddingLeft() + mPaddingLeft + mSpacing / 2 + i * (mSpacing + mDotSize));
            if (isInactiveDrawable) {
                mInactiveDot.setBounds(x, getPaddingTop(), (int) (x + mDotSize), getPaddingTop() + (int) mDotSize);
               // mInactiveDot.setBounds(0, getPaddingTop(), (int) (x + mDotSize), getPaddingTop() + (int) mDotSize);
            	mInactiveDot.draw(canvas);
            } else {
                mPaint.setColor(mEmptyDotsColor);
                canvas.drawCircle(x + mDotSize / 2,
                        getPaddingTop() + mDotSize / 2, mDotSize / 2, mPaint);
            }
        }
        if (isInProgress) {
            int x = (int) (getPaddingLeft() + mPaddingLeft + mSpacing / 2 + mActiveDotIndex * (mSpacing + mDotSize));
            if (isActiveDrawable) {
                mActiveDot.setBounds(x, getPaddingTop(), (int) (x + mDotSize), getPaddingTop() + (int) mDotSize);
                mActiveDot.draw(canvas);
            } else {
                mPaint.setColor(mActiveDotColor);
                canvas.drawCircle(x + mDotSize / 2,
                        getPaddingTop() + mDotSize / 2, mDotSize / 2, mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
       
        mDotSize = parentHeight<parentWidth?parentHeight:parentWidth;
        
        //计算最多显示几个小球
        int maxCountOfDots = getDotRealSize(parentWidth,parentHeight);
        mNumberOfDots = Math.min(mNumberOfDots, maxCountOfDots);
        
        int calculatedHeight = (int) mDotSize;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension((int)((mDotSize+mSpacing)*mNumberOfDots), calculatedHeight);
    }



    private int getDotRealSize(int parentWidth, int parentHeight) {
    	int maxLength= Math.max(parentWidth, parentHeight);
    	float cellsize= mDotSize+mSpacing;
    	if(cellsize<=0){
    		return 0;
    	}else{
    		return (int) Math.ceil((maxLength *1.0) / cellsize);
    	}
	}

	public void startProgress() {
        isInProgress = true;
        mActiveDotIndex = -1;
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    public void stopProgress() {
        isInProgress = false;
        mHandler.removeCallbacks(mRunnable);
        invalidate();
    }
    public void setPointNum(int count){
    	mNumberOfDots = count;
    }
    public void setDefaultImage(Bitmap bitmap){
    	mInactiveDot = new BitmapDrawable(bitmap);
    	invalidate();
    }
    public void setChangeImage(Bitmap bitmap){
    	mActiveDot = new BitmapDrawable(bitmap);
    	invalidate();
    	
    }
    public void destroy(){
    	if(mActiveDot!=null){
    		mActiveDot.setCallback(null);
    		mActiveDot = null;
    	}
    	if(mInactiveDot!=null){
    		mInactiveDot.setCallback(null);
    		mInactiveDot = null;
    	}
    }

}
