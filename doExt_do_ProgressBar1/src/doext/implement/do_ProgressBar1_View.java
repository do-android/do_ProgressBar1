package doext.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.FrameLayout;
import core.DoServiceContainer;
import core.helper.DoIOHelper;
import core.helper.DoImageLoadHelper;
import core.helper.DoTextHelper;
import core.helper.DoUIModuleHelper;

import org.json.JSONObject;

import core.interfaces.DoIUIModuleView;
import core.interfaces.DoIScriptEngine;
import core.object.DoProperty;
import core.object.DoUIModule;
import core.object.DoInvokeResult;
import doext.define.do_ProgressBar1_IMethod;
import doext.define.do_ProgressBar1_MAbstract;
import doext.dottedprogress.DottedProgressBar;
import doext.dottedprogress.DottedProgressEntity;
import doext.dottedprogress.ScaleDotProgressBar;
import doext.dottedprogress.ScaleDotProgressEntity;

/**
 * 自定义扩展UIView组件实现类，此类必须继承相应VIEW类，并实现DoIUIModuleView,do_ProgressBar1_IMethod接口；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象；
 * 获取DoInvokeResult对象方式new DoInvokeResult(this.model.getUniqueKey());
 */
public class do_ProgressBar1_View extends FrameLayout implements DoIUIModuleView,do_ProgressBar1_IMethod{
	
	/**
	 * 每个UIview都会引用一个具体的model实例；
	 */
	private do_ProgressBar1_MAbstract model;
	private Context mContext;
	private DottedProgressBar dottedProgressBar;
	private ScaleDotProgressBar scaleDotProgressBar;
	private String mStyle;
	
	public do_ProgressBar1_View(Context context) {
		super(context);
		this.mContext = context;
	}
	
	/**
	 * 初始化加载view准备,_doUIModule是对应当前UIView的model实例
	 */
	@Override
	public void loadView(DoUIModule _doUIModule) throws Exception {
		this.model = (do_ProgressBar1_MAbstract) _doUIModule;

		DoProperty _propertyStyle = model.getProperty("style");
		mStyle = _propertyStyle.getValue();
		if(mStyle ==null || mStyle.equals("")){
			mStyle = "normal";
		}
		DoProperty _propertyPointNum = model.getProperty("pointNum");
		int _pointNum = DoTextHelper.strToInt(_propertyPointNum.getValue(), 0);
		//普通样式
		if (!TextUtils.isEmpty(mStyle) && mStyle.equals("normal")) {

			DoProperty _propertyDefaultImage = model.getProperty("defaultImage");
			String _defaultImage = _propertyDefaultImage.getValue();

			DoProperty _propertyChangeImage = model.getProperty("changeImage");
			String _changeImage = _propertyChangeImage.getValue();

			DottedProgressEntity entity = new DottedProgressEntity(_pointNum, getLocalBitmap(_defaultImage), getLocalBitmap(_changeImage));
			dottedProgressBar = new DottedProgressBar(mContext, entity);
			//设置居中显示
			FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
			fParams.gravity = Gravity.CENTER_HORIZONTAL;
			fParams.topMargin = 0;
			fParams.bottomMargin = 0;
			dottedProgressBar.setLayoutParams(fParams);
			this.addView(dottedProgressBar);
			
			dottedProgressBar.startProgress();
		//循环样式
		}else if(!TextUtils.isEmpty(mStyle) && mStyle.equals("zoom")){
			DoProperty _propertyChangeImage = model.getProperty("pointColors");
			String _colors = _propertyChangeImage.getValue();
			List<Integer> colors = getColors(_colors);
			ScaleDotProgressEntity entity = new ScaleDotProgressEntity(_pointNum, colors);
			scaleDotProgressBar = new ScaleDotProgressBar(mContext, entity);
			FrameLayout.LayoutParams fParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
			fParams.gravity = Gravity.CENTER_HORIZONTAL;
			fParams.topMargin = 0;
			fParams.bottomMargin = 0;
			this.addView(scaleDotProgressBar, fParams);
			scaleDotProgressBar.startProgress();
		}else{
			throw new Exception("do_ProgressBar1 style 异常");
		}
		
		
	}
	
	private List<Integer> getColors(String _colors) {
		List<Integer> list = new ArrayList<Integer>();
		String arrColors[] = _colors.split(",");
		for(int i=0;i<arrColors.length;i++){
			int color = DoUIModuleHelper.getColorFromString(arrColors[i], 0x000000ff);
			list.add(color);
		}
		return list;
	}

	/**
	 * 动态修改属性值时会被调用，方法返回值为true表示赋值有效，并执行onPropertiesChanged，否则不进行赋值；
	 * @_changedValues<key,value>属性集（key名称、value值）；
	 */
	@Override
	public boolean onPropertiesChanging(Map<String, String> _changedValues) {
		
		return true;
	}
	
	/**
	 * 属性赋值成功后被调用，可以根据组件定义相关属性值修改UIView可视化操作；
	 * @_changedValues<key,value>属性集（key名称、value值）；
	 */
	@Override
	public void onPropertiesChanged(Map<String, String> _changedValues) {
		DoUIModuleHelper.handleBasicViewProperChanged(this.model, _changedValues);
		
		if (_changedValues.containsKey("changeImage")) {
			String _changeImage = _changedValues.get("changeImage");
			if(dottedProgressBar!=null)
			dottedProgressBar.setChangeImage(getLocalBitmap(_changeImage));
		}
		if (_changedValues.containsKey("defaultImage")) {
			String _defaultImage = _changedValues.get("defaultImage");
			if(dottedProgressBar!=null)
			dottedProgressBar.setDefaultImage(getLocalBitmap(_defaultImage));
		}
		
		if (_changedValues.containsKey("pointNum")) {
			int pointNum = DoTextHelper.strToInt(_changedValues.get("pointNum"), 0);
			if(dottedProgressBar!=null)
			dottedProgressBar.setPointNum(pointNum);
		}
		if (_changedValues.containsKey("pointColors")) {
			String pointColors = _changedValues.get("pointColors");
			List<Integer> colors = getColors(pointColors);
			if(scaleDotProgressBar!=null)
			scaleDotProgressBar.setColors(colors);
		}
	}
	
	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, JSONObject _dictParas,
			DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult)throws Exception {
		//...do something
		return false;
	}
	
	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用，
	 * 可以根据_methodName调用相应的接口实现方法；
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名
	 * #如何执行异步方法回调？可以通过如下方法：
	 *	_scriptEngine.callback(_callbackFuncName, _invokeResult);
	 * 参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	   获取DoInvokeResult对象方式new DoInvokeResult(this.model.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, JSONObject _dictParas,
			DoIScriptEngine _scriptEngine, String _callbackFuncName) {
		//...do something
		return false;
	}
	
	/**
	* 释放资源处理，前端JS脚本调用closePage或执行removeui时会被调用；
	*/
	@Override
	public void onDispose() {
		//...do something
		if(dottedProgressBar!=null){
			dottedProgressBar.destroy();
			dottedProgressBar = null;
		}
		if(scaleDotProgressBar!=null){
			scaleDotProgressBar.destroy();
			scaleDotProgressBar = null;
		}
	}
	
	/**
	* 重绘组件，构造组件时由系统框架自动调用；
	  或者由前端JS脚本调用组件onRedraw方法时被调用（注：通常是需要动态改变组件（X、Y、Width、Height）属性时手动调用）
	*/
	@Override
	public void onRedraw() {
		this.setLayoutParams(DoUIModuleHelper.getLayoutParams(this.model));
	}
	
	/**
	 * 获取当前model实例
	 */
	@Override
	public DoUIModule getModel() {
		return model;
	}
	
	private Bitmap getLocalBitmap(String local) {
		Bitmap bitmap = null;
		try {
			if (null == DoIOHelper.getHttpUrlPath(local) && local != null && !"".equals(local)) {
				String path = DoIOHelper.getLocalFileFullPath(this.model.getCurrentPage().getCurrentApp(), local);
				bitmap = DoImageLoadHelper.getInstance().loadLocal(path, -1, -1);
			} else {
				throw new Exception("只支持本地图片");
			}
		} catch (Exception e) {
			DoServiceContainer.getLogEngine().writeError("do_ProgressBar1 无法获取图片", e);
		}
		return bitmap;
	}

}