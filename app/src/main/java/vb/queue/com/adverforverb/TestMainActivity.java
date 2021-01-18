package vb.queue.com.adverforverb;


import java.util.HashMap;
import java.util.Iterator;

import com.szzk.usb_printportlibs.USBFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import vb.queue.com.text.Toast_Util;

public class TestMainActivity extends Activity {

	private final int CONNECTRESULT=0x001;
	private LinearLayout linearLayoutUSBDevices;
	private Context mContext;
	private Button print_bt,search_print_bt;
    private TextView usbconnt_tv;
	
	private UsbManager mUsbManager ;
	private HashMap<String, UsbDevice> deviceList;
	private Iterator<UsbDevice> deviceIterator ;
	public static USBFactory usbfactory;
	private Handler mHandler;
	private boolean t=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_main);
		mContext=this;		
		usbfactory=USBFactory.getUsbFactory(mContext);
		usbconnt_tv=(TextView)findViewById(R.id.usbconnt_tv);
		search_print_bt=(Button)findViewById(R.id.search_print_bt);
		print_bt=(Button)findViewById(R.id.print_bt);
		linearLayoutUSBDevices = (LinearLayout) findViewById(R.id.linearLayoutUSBDevices);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {

			probe();
		} else {
			finish();
		}
		//普通打印
		print_bt.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(!usbfactory.is_connecusb())
				{
					Toast_Util.ToastString(mContext, "unconnected");	//usb未连接 usb unconnected
					return;
				}				
				if(!usbfactory.Check_Paper())
				{
					Toast_Util.ToastString(mContext, "printer_out_of_paper");//打印机缺纸  printer out of paper
					return;
				}
				Intent intent=new Intent(TestMainActivity.this, NormalPrintActivity.class);
				startActivity(intent);
			}
		});
		//搜索打印机
		search_print_bt.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!usbfactory.is_connecusb())
				{					
					probe();				
				}else {
					Toast_Util.ToastString(getApplicationContext(),"printer_connected");//打印机已连接 printer connected
				}


			}
		});

	}

	//搜索USB设备
	//search USBdevice
	private void probe() {
		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		deviceList = mUsbManager.getDeviceList();
		deviceIterator = deviceList.values().iterator();		
		linearLayoutUSBDevices.removeAllViews();
	
		if (deviceList.size() > 0) {
			// 初始化选择对话框布局，并添加按钮和事件
			//Initialize the selection of dialog boxes, add buttons and events
			while (deviceIterator.hasNext()) { // 这里是if不是while，说明我只想支持一种device
				final UsbDevice device = deviceIterator.next();
				device.getInterfaceCount();
				Button btDevice = new Button(linearLayoutUSBDevices.getContext());
				btDevice.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 80));
				btDevice.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				btDevice.setTextSize(30);
				btDevice.setText(String.format(" VID:%04X PID:%04X", device.getVendorId(), device.getProductId()));
				btDevice.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						PendingIntent mPermissionIntent = PendingIntent.getBroadcast(TestMainActivity.this, 0, new Intent(getApplicationInfo().packageName), 0);
						if (!mUsbManager.hasPermission(device)) {
							mUsbManager.requestPermission(device,mPermissionIntent);
						} else {
							   t=usbfactory.connectUsb(mUsbManager, device);
							  if(t) {
								  //usb连接成功 
//								 Toast_Util.ToastString(mContext, R.string.successful_connection_printer);
								  usbconnt_tv.setText("connected");
								  usbconnt_tv.setTextColor(Color.GREEN);
							  }else {
								  //usb连接失败
//								  Toast_Util.ToastString(mContext, R.string.printer_connection_failure);
								  usbconnt_tv.setText("unconnected");
								  usbconnt_tv.setTextColor(Color.RED);
							  }
						}
					}
				});
				linearLayoutUSBDevices.addView(btDevice);
			}
		}
		mHandler=new MHandler();
		new mthread().start();
		
	}
	// 每隔2秒监听打印连接状态
	//Listen to the print connection every 2 seconds
	int i=0;
	class mthread extends Thread{
		@Override
		public void run() {
			super.run();
			while(true)
			{
				i++;
				mHandler.sendEmptyMessage(CONNECTRESULT);
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	 class MHandler extends Handler {
			@SuppressLint("HandlerLeak") 
			@SuppressWarnings("static-access")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {

				case CONNECTRESULT: {
					if(usbfactory!=null)
					{
						if(usbfactory.is_connecusb())
						{
							 usbconnt_tv.setText("connected");  //已连接
							 usbconnt_tv.setTextColor(Color.GREEN);
						}else {
							 usbconnt_tv.setText("unconnected");//未连接
							 usbconnt_tv.setTextColor(Color.RED);
							 if(t) {
								 linearLayoutUSBDevices.removeAllViews();
								 t=false;
							 }
						}
					}
					break;
				}

				}
			}
		}
	
}
