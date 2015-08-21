package doext.define;

import core.object.DoUIModule;
import core.object.DoProperty;
import core.object.DoProperty.PropertyDataType;


public abstract class do_ProgressBar1_MAbstract extends DoUIModule{

	protected do_ProgressBar1_MAbstract() throws Exception {
		super();
	}
	
	/**
	 * 初始化
	 */
	@Override
	public void onInit() throws Exception{
        super.onInit();
        //注册属性
		this.registProperty(new DoProperty("changeImage", PropertyDataType.String, "", false));
		this.registProperty(new DoProperty("defaultImage", PropertyDataType.String, "", false));
		this.registProperty(new DoProperty("pointColors", PropertyDataType.String, "", false));
		this.registProperty(new DoProperty("pointNum", PropertyDataType.Number, "0", false));
		this.registProperty(new DoProperty("style", PropertyDataType.String, "normal", true));
	}
}