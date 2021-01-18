package vb.queue.com.adverforverb;

import java.io.IOException;
import java.io.InputStream;

import com.szzk.usb_printportlibs.USBFactory;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import vb.queue.com.adverforverb.R;
import vb.queue.com.adverforverb.TestMainActivity;
import vb.queue.com.text.Toast_Util;

public class NormalPrintActivity extends Activity {

	private Button bt_text,bt_barcode,bt_qr,bt_image,bt_check_location,bt_cutting,bt_nocutting;
	private USBFactory usbfactory;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_normal);
		usbfactory= TestMainActivity.usbfactory;
		bt_text=(Button)findViewById(R.id.bt_text);
		bt_barcode=(Button)findViewById(R.id.bt_barcode);
		bt_qr=(Button)findViewById(R.id.bt_qr);
		bt_image=(Button)findViewById(R.id.bt_image);
		bt_check_location=(Button)findViewById(R.id.bt_check_location);
		bt_cutting=(Button)findViewById(R.id.bt_cutting);
		bt_nocutting=(Button)findViewById(R.id.bt_nocutting);
		//打印文本
		bt_text.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				usbfactory.PrintText("\r\nDFEFKJJKE","3","1",0);
				usbfactory.PrintText("","1","1",10);
				usbfactory.PrintText("1SFSDFA45687dfsdf","1","1",20);
				usbfactory.PrintText("2SFSDFA45687dfsdf","2","1",30);
				usbfactory.PrintText("3SFSDFA45687dfsdf","3","1",40);
				usbfactory.PrintText("4SFSDFA45687dfsdf","1","1",50);
				usbfactory.PrintText("5SFSDFA45687dfsdf","1","2",50);
				usbfactory.PrintText("6SFSDFA45687dfsdf","1","3",50);
				usbfactory.PrintText("_______________","1","1",10);
				usbfactory.PrintText("","1","1",10);
				usbfactory.PrintText("","1","1",10);
				usbfactory.PrintText("","1","1",10);
				usbfactory.PaperCut();
			}
		});
//		打印条码
		bt_barcode.setOnClickListener(new OnClickListener() {			
			@SuppressWarnings("static-access")
			@Override
			public void onClick(View arg0) {
				usbfactory.PrintBarcode("5A3456A5",3,100,5,2);
				usbfactory.PrintBarcode("6A3456A6",5,200,6,2);
				usbfactory.PrintBarcode("12345678",3,80,7,2);
				usbfactory.PrintBarcode("8A3456A8",2,80,8,2);
				usbfactory.PrintBarcode("9A3456A9",3,80,9,2);
				usbfactory.PrintText("","1","1",10);
				usbfactory.PrintText("","1","1",50);
				usbfactory.PrintText("","1","1",30);
				usbfactory.PaperCut();
			}
		});
		//打印二维码
		bt_qr.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				usbfactory.PrintQR("fdsfsdfsad", 2);
				usbfactory.PrintQR("fdsfsdfsad", 3);
				usbfactory.PrintQR("fdsfsdfsad", 4);
				usbfactory.PrintQR("fdsfsdfsad", 5);
				usbfactory.PrintQR("fdsfsdfsad", 6);
				usbfactory.PrintText("","1","1",10);
				usbfactory.PrintText("","1","1",10);
				usbfactory.PaperCut();
			}
		});
//		打印图片 print pictures
		bt_image.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bitmap bm = getImageFromAssetsFile("1.jpg");
				if (null != bm) {
					usbfactory.PrintImage(bm,384,0,"1");//打印黑白色图片 print black and white pictures
					usbfactory.PrintPicture(bm,384);//用于彩色转灰度图片打印 For color to grayscale image printing
					usbfactory.PrintText("","1","1",10);
					usbfactory.PrintText("","1","1",30);
					usbfactory.PaperCut();
				}else{
					Toast_Util.ToastString(getApplicationContext(), "no_printed_pictures");//没有打印图片
				}
			}
		});
		//
		bt_check_location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//标签纸效验定位,校准缝隙位置 只用于803机器
				//Label paper positioning, calibration gap position only for 803 machines
				byte[] buf=new byte[]{0x1f,0x63};
				usbfactory.PrintByte(buf);
			}
		});
		
		bt_cutting.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//走到下一张并切纸，找到缝隙位并切刀只用于803机器（打印完后调用该指令会定位到缝隙切纸）
					//Go to the next one and cut the paper, find the gap and use the knife only for the 803 machine 
					//(calling this command will locate the crevice cut)
					byte[] buf=new byte[]{0x1D,0x56,0x42,0x00};
					usbfactory.PrintByte(buf);
				}
			});
		
		bt_nocutting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//走到下一张不切纸，只定位走纸不切纸只用于803机器（打印完后调用该指令会定位到缝隙不切纸）
				//Go to the next sheet without cutting, only position the paper without cutting Paper is only used on the 803 machine 
				//(after calling this command, it will locate the gap and not cut paper)
				byte[] buf=new byte[]{0x1A,0x0C,0x00};
				usbfactory.PrintByte(buf);
			}
		});
			
	}
	/**
	 * 打印测试页面
	 * print test page
	 * @param v
	 */
	public void printtestpage(View v)
	{
		usbfactory.PrintTest();
	}
	
	
	/**
	 * 检查缺纸
	 * Check for paper
	 */
	public void checkpage(View v)
	{
		if(usbfactory.Check_Paper())
		{
//			Toast_Util.ToastString(getApplicationContext(),R.string.printer_have_paper );//打印机有纸
		}else {
//			Toast_Util.ToastString(getApplicationContext(), R.string.printer_does_not_have_paper);//打印机缺纸
		}
	}
	/**
	 * 从Assets中读取图片
	 * Read pictures from Assets
	 */
	private Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;

	}
	
	public void back(View v)
	{
		finish();
	}
	
   
	

}
