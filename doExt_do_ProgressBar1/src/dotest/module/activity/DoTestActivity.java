package dotest.module.activity;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.doext.module.activity.R;

import core.DoServiceContainer;
import core.object.DoModule;
import core.object.DoUIModule;
import doext.implement.do_ProgressBar1_Model;
import doext.implement.do_ProgressBar1_View;
import dotest.module.frame.debug.DoPage;
import dotest.module.frame.debug.DoPageViewFactory;
import dotest.module.frame.debug.DoService;

/**
 * 测试扩展组件Activity需继承此类，并重写相应测试方法；
 */
public class DoTestActivity extends Activity {
	
	protected DoModule model;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deviceone_test);
		DoService.Init(this);
		DoPageViewFactory doPageViewFactory = (DoPageViewFactory)DoServiceContainer.getPageViewFactory();
		doPageViewFactory.setCurrentActivity(this);
		try {
			initModuleModel();
			initUIView();
		} catch (Exception e) {
			e.printStackTrace();
		}
		onEvent();
	}

	/**
	 * 初始化UIView，扩展组件是UIModule类型需要重写此方法；
	 */
	protected void initUIView() throws Exception{
		do_ProgressBar1_View view = new do_ProgressBar1_View(this);
        DoPage _doPage = new DoPage();
        ((DoUIModule)this.model).setCurrentUIModuleView(view);
        ((DoUIModule)this.model).setCurrentPage(_doPage);
        view.loadView((DoUIModule)this.model);
        LinearLayout uiview = (LinearLayout)findViewById(R.id.ll);
        uiview.addView(view);
	}

	/**
	 * 初始化Model对象
	 */
	protected void initModuleModel() throws Exception {
		this.model = new do_ProgressBar1_Model();
//		DoService.setPropertyValue(this.model, "style", "normal");
//		DoService.setPropertyValue(this.model, "pointNum", "6");
//		//DoService.setPropertyValue(this.model, "defaultImage", "/mnt/sdcard/inactive_dot.png");
//		DoService.setPropertyValue(this.model, "defaultImage", "/mnt/sdcard/test.jpg");
//		DoService.setPropertyValue(this.model, "changeImage", "/mnt/sdcard/Bb.jpg");
//		//DoService.setPropertyValue(this.model, "changeImage", "/mnt/sdcard/active_dot.png");
		
		DoService.setPropertyValue(this.model, "style", "zoom");
		DoService.setPropertyValue(this.model, "pointNum", "5");
		DoService.setPropertyValue(this.model, "pointColors", "ff0000ff,ff00ffff,ffff00ff,aabbccff");
		
	}

	/**
	 * 测试属性
	 * 
	 * @param view
	 */
	public void doTestProperties(View view) {

	}

	/**
	 * 测试（同步/异步）方法
	 * 
	 * @param view
	 */
	public void doTestMethod(View view) {
		doTestSyncMethod();
		doTestAsyncMethod();
	}
	
	public void setPro(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("请输入要测试属性的序号");
		final EditText _editText = new EditText(this);
		_editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		builder.setView(_editText);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String resultNum = _editText.getText().toString().trim();
				if(resultNum==null||"".equals(resultNum))
					return;
				
				int result = Integer.parseInt(resultNum);
				String sdcardRootPath = Environment.getExternalStorageDirectory().getPath();
				switch (result) {
				case 1:
					DoService.setPropertyValue(DoTestActivity.this.model, "pointNum", "2");
					break;
				case 2:
					DoService.setPropertyValue(DoTestActivity.this.model, "defaultImage", sdcardRootPath+"/active_dot.png");
					break;
				case 3:
					DoService.setPropertyValue(DoTestActivity.this.model, "changeImage", sdcardRootPath+"/inactive_dot.png");
					break;
					
				case 4:
					DoService.setPropertyValue(DoTestActivity.this.model, "pointColors", "ff00ffff,ffff00ff");
					break;
				default:
					Toast.makeText(getApplicationContext(), "输入序号有误", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	/**
	 * 测试同步方法
	 */
	protected void doTestSyncMethod() {
		
	}

	/**
	 * 测试异步方法
	 */
	protected void doTestAsyncMethod() {
		
	}

	/**
	 * 测试Module订阅事件消息
	 */
	protected void onEvent() {

	}

	/**
	 * 测试模拟触发一个Module消息事件
	 * 
	 * @param view
	 */
	public void doTestFireEvent(View view) {

	}
}
