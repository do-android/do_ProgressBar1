package doext.dottedprogress;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xingkong on 8/23/15.
 */
public class ScaleDotProgressBar extends View {
	private float mDotSize;
	private float mSpacing;
	private int mJumpingSpeed = 350;

	private int mNumberOfDots;
	private Paint mPaint;
	private int mPaddingLeft;
	private Handler mHandler;
	private ArrayList<Integer> realColors;
	private List<Integer> colors;
	private double arrZooms[];
	private int maxPointIndex;
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			maxPointIndex++;
			maxPointIndex = maxPointIndex % mNumberOfDots;
			ScaleDotProgressBar.this.invalidate();
			mHandler.postDelayed(mRunnable, mJumpingSpeed);
		}
	};

	public ScaleDotProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScaleDotProgressBar(Context mContext, ScaleDotProgressEntity entity) {
		super(mContext);
		mHandler = new Handler();
		realColors = new ArrayList<Integer>();
		mNumberOfDots = entity.getPointNum();
		mSpacing = 0;
		colors = entity.getColors();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		updateZoomData(maxPointIndex, mNumberOfDots);
		for (int i = 0; i < mNumberOfDots; i++) {
			int x = (int) (getPaddingLeft() + mPaddingLeft + mSpacing / 2 + i * (mSpacing + mDotSize));
			mPaint.setColor(realColors.get(i));
			canvas.drawCircle(x + mDotSize / 2, getPaddingTop() + mDotSize / 2, (int) (mDotSize * arrZooms[i] / 2), mPaint);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

		mDotSize = parentHeight < parentWidth ? parentHeight : parentWidth;

		// 计算最多显示几个小球
		int maxCountOfDots = getDotRealSize(parentWidth, parentHeight);
		mNumberOfDots = Math.min(mNumberOfDots, maxCountOfDots);
		
		int calculatedHeight = (int) mDotSize;
		// 计算小球可以选颜色组
		initPointColors();
		// 初始化每个小球大小
		arrZooms = new double[mNumberOfDots];
		updateZoomData(0, mNumberOfDots);
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.setMeasuredDimension((int)(mDotSize*mNumberOfDots), calculatedHeight);
	}

	private int getDotRealSize(int parentWidth, int parentHeight) {
		int maxLength = Math.max(parentWidth, parentHeight);
		float cellsize = mDotSize + mSpacing;
		if (cellsize <= 0) {
			return 0;
		} else {
			return (int) Math.ceil((maxLength * 1.0) / cellsize);
		}
	}

	public void startProgress() {
		maxPointIndex = -1;
		mHandler.removeCallbacks(mRunnable);
		mHandler.post(mRunnable);
	}

	public void stopProgress() {
		mHandler.removeCallbacks(mRunnable);
		invalidate();
	}

	public void initPointColors() {
		if (colors == null || colors.size() <= 0 || realColors == null) {
			return;
		}
		int size = colors.size();
		if (size == mNumberOfDots) {
			for (int color : colors) {
				realColors.add(color);
			}
		} else if (size > mNumberOfDots) {
			for (int i = 0; i < mNumberOfDots; i++) {
				realColors.add(colors.get(i));
			}
		} else if (size < mNumberOfDots) {
			for (int i = 0; i < mNumberOfDots; i++) {
				realColors.add(colors.get(i % size));
			}
		}

	}

	private void updateZoomData(int current, int totleNums) {
		double baseZoom = 0.85;
		if (current < 0 || totleNums < 1) {
			baseZoom = 1;
		}
		for (int i = 0; i < arrZooms.length; i++) {
			if (i == current) {
				arrZooms[i] = 1.00;
			} else if (i > current) {
				double scral = Math.pow(baseZoom, (i - current) + 1);
				BigDecimal b = new BigDecimal(scral);
				// 保留两位小数
				arrZooms[i] = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			} else if (i < current) {
				double scral = Math.pow(baseZoom, (current - i) + 1);
				BigDecimal b = new BigDecimal(scral);
				arrZooms[i] = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		}

	}
	public void setColors(List<Integer> list){
		if(colors!=null){
			realColors.clear();
			colors.clear();
			colors.addAll(list);
		}
		initPointColors();
		startProgress();
	}
	public void destroy() {
		if (mHandler != null && mRunnable != null) {
			mHandler.removeCallbacks(mRunnable);
		}
		if (realColors != null) {
			realColors.clear();
		}
		if (colors != null) {
			colors.clear();
		}
	}

}
