package vb.queue.com.adverforverb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import kotlinx.android.synthetic.main.activity_test.*
import vb.queue.com.adverforverb.utils.Texts

class TestActivity : AppCompatActivity() {

    private var tpageCount: Int = 0
    private var tcurPage: Int = 0
    private var tcount: Int = 30
    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            sendMsg()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        button.setOnClickListener {
            startActivity(Intent(this@TestActivity,MainActivity::class.java))
        }

        buttonSetting.setOnClickListener {
            startActivity(Intent(this@TestActivity,SettingActivity::class.java))
        }
//        sendMsg()
    }

    fun sendMsg() {
        val content = "1212212121"
        val page = content.length
        if (page % tcount == 0) {
            tpageCount = page % tcount
        } else {
            tpageCount = page / tcount + 1
        }
        var showText = ""
        val subPage = (tcurPage + 1)
        if (subPage < tpageCount) {
            showText = content.substring(tcurPage * tcount, subPage * tcount - 1)
            tcurPage++
        } else {
            showText = content.substring(tcurPage * tcount, page)
            tcurPage = 0
        }
        description.setText(showText)
        System.out.println("===showText======$showText")
        Texts.tra(description)
        handler.sendEmptyMessageDelayed(102, 5000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeMessages(102)
    }
}
