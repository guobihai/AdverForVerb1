package vb.queue.com.adverforverb

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.ClipboardManager
import android.text.TextUtils
import android.widget.Toast
import com.smart.library.utils.SystemUtils
import kotlinx.android.synthetic.main.activity_jh.*
import vb.queue.com.adverforverb.utils.PreferenceUtils
import vb.queue.com.adverforverb.utils.ToolDes

//import vb.queue.com.print.USBPrinter

class JhActivity : BaseActivity() {
    //    var usbPrinter: USBPrinter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jh)
        if (PreferenceUtils.getBoolean(this@JhActivity, "isJh", false)) {
            startActivity(Intent(this@JhActivity, WelActivity::class.java))
            finish()
        }
//        println("tag:" + SystemUtils.getDeviceId(this@JhActivity))
//        usbPrinter = USBPrinter.getInstance();
//        usbPrinter!!.initPrinter(this);
//        USBPrinter.initPrinter(this@JhActivity)
        text2.setText("(请把如下设备码告知管理员开通激活码:" + SystemUtils.getDeviceId(this@JhActivity) + ")")
        btnJh.setOnClickListener {
            val tools = ToolDes()
            val value = tools.getValue(SystemUtils.getDeviceId(this@JhActivity))
            if (!TextUtils.isEmpty(value) && etJhm.text.toString().trim().equals(value)) {
                PreferenceUtils.putBoolean(this@JhActivity, "isJh", true)
                Toast.makeText(this, "设备激活成功。", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@JhActivity, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "请输入正确的激活码。", Toast.LENGTH_LONG).show()
            }

//            Thread(Runnable {
//                usbPrinter!!.bold(true)
//                usbPrinter!!.setTextSize(3)
//                usbPrinter!!.setAlign(1)
//                usbPrinter!!.printTextNewLine("测试USB打印")
//                usbPrinter!!.printLine(1)
//                usbPrinter!!.bold(false)
//                usbPrinter!!.printTextNewLine("出品单号：" + "123456")
//                usbPrinter!!.printTextNewLine("出品员：" + "测试")
//                usbPrinter!!.printBarCode("6936983800013");
//                usbPrinter!!.printLine(5)
//                usbPrinter!!.cutPager()
//            }).start()
//            USBPrinter.getInstance().print("hello 打印机")

        }
        copy.setOnClickListener { copy() }
    }

    /**
     * 复制内容
     *
     */
    private fun copy() {
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 将文本内容放到系统剪贴板里。
        cm.setText(SystemUtils.getDeviceId(this@JhActivity))
        Toast.makeText(this, "复制设备码成功。", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
//        USBPrinter.destroyPrinter()
//        usbPrinter!!.close()
    }
}
