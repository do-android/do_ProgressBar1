package dottedprogress;

import android.graphics.Bitmap;

public class DottedProgressEntity {
	private int pointNum;
	private Bitmap defaultImage;
	private Bitmap changeImage;
	public DottedProgressEntity(int pointNum, Bitmap defaultImage, Bitmap changeImage) {
		super();
		this.pointNum = pointNum;
		this.defaultImage = defaultImage;
		this.changeImage = changeImage;
	}
	public int getPointNum() {
		return pointNum;
	}
	public void setPointNum(int pointNum) {
		this.pointNum = pointNum;
	}
	public Bitmap getDefaultImage() {
		return defaultImage;
	}
	public void setDefaultImage(Bitmap defaultImage) {
		this.defaultImage = defaultImage;
	}
	public Bitmap getChangeImage() {
		return changeImage;
	}
	public void setChangeImage(Bitmap changeImage) {
		this.changeImage = changeImage;
	}
	@Override
	public String toString() {
		return "DottedProgressEntity [pointNum=" + pointNum + ", defaultImage=" + defaultImage + ", changeImage=" + changeImage + "]";
	}
	
	
	
}
