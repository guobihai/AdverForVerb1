package vb.queue.com.adverforverb

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_wel.*
import vb.queue.com.adverforverb.tcp.Constants
import vb.queue.com.adverforverb.utils.SystemUtil
import vb.queue.com.adverforverb.tcp.UDPBuild
import vb.queue.com.adverforverb.utils.LogUtils
import vb.queue.com.adverforverb.utils.PreferenceUtils
import vb.queue.com.adverforverb.utils.Texts
import java.net.DatagramPacket
import java.net.ServerSocket
import java.text.SimpleDateFormat
import java.util.*


class WelActivity : BaseActivity() {

    private var udpBuild: UDPBuild? = null

    private var index: Int = 10

    @SuppressLint("HandlerLeak")
    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1111) {
                --index
                if (index > 0) {
                    sendTimes()
                } else {
                    startToMain()
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wel)

        val s = ServerSocket(0)
        Constants.SOCKET_UDP_PORT = s.localPort

        udpBuild = UDPBuild.getUdpBuild()
        udpBuild?.setUdpReceiveCallback(object : UDPBuild.OnUDPReceiveCallbackBlock {
            override fun OnParserComplete(data: String) {
                runOnUiThread {
                    data.let {
                        tv_nativeIpAddr.text = it
                        System.out.println("=========ip====" + it)
                        if (index <= 5) {
                            PreferenceUtils.putString(this@WelActivity, "IPADDR", it)
                        }
                    }

                }

            }
        })


        var machineNo = PreferenceUtils.getString(this@WelActivity, "machineNo", "")
        if (TextUtils.isEmpty(machineNo)) {
            machineNo = getMachineNo()
            PreferenceUtils.putString(this@WelActivity, "machineNo", machineNo)
        }
        tv_machine_value.text = machineNo
    }

    override fun onResume() {
        super.onResume()
        sendTimes()
    }

    fun getMachineNo(): String {
        return ((Math.random() * 9000).toInt() + 1000).toString();
    }

    fun sendTimes() {
        handler.sendEmptyMessageDelayed(1111, 1000)
        tv_second.text = String.format("%dS", index)
        udpBuild?.sendMessage("hello")
//        tv_nativeIpAddr.text = SystemUtil.getLocalHostIp(this)
    }

    fun startToMain() {
        udpBuild?.stopUDPSocket()
        handler.removeMessages(1111)
        startActivity(Intent(this@WelActivity, MainActivity::class.java))
        finish()
    }
}
