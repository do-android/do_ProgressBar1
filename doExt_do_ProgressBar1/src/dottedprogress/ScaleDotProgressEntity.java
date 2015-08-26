package dottedprogress;

import java.util.List;

public class ScaleDotProgressEntity {
	private int pointNum;
	private List<Integer> colors;
	public ScaleDotProgressEntity(int pointNum, List<Integer> colors) {
		super();
		this.pointNum = pointNum;
		this.colors = colors;
	}
	public int getPointNum() {
		return pointNum;
	}
	public void setPointNum(int pointNum) {
		this.pointNum = pointNum;
	}
	public List<Integer> getColors() {
		return colors;
	}
	public void setColors(List<Integer> colors) {
		this.colors = colors;
	}
	@Override
	public String toString() {
		return "ScaleDotProgressEntity [pointNum=" + pointNum + ", colors=" + colors + "]";
	}
	
	
	
}
