package vb.queue.com.adverforverb

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.*
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.mjdev.libaums.fs.FileSystem
import com.smart.library.widget.SmartVideoPlayer
import com.youth.banner.Banner
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import vb.queue.com.adverforverb.adapts.ImageNetAdapter
import vb.queue.com.adverforverb.greendao.ctrls.NoticeInfoCtrls
import vb.queue.com.adverforverb.greendao.ctrls.RoomCtrls
import vb.queue.com.adverforverb.greendao.ctrls.SettingInfoCtrls
import vb.queue.com.adverforverb.services.SmartServices
import vb.queue.com.adverforverb.tcp.ConfigUtils
import vb.queue.com.adverforverb.utils.*
import java.io.File

open class MainActivity : BaseActivity() {

    private var isRun: Boolean = false
    var url = "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"

    var playIndex: Int = 0
    private var arrays: Array<String>? = null
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001
    private var listFile: ArrayList<File>? = null
    private var imglistFile: ArrayList<File>? = null
    private var isPlayImage: Boolean = true
    private var orderType: Int = 0//渠道类型

    private val imgPlay: Int = 12 * 1000
    private var currentFs: FileSystem? = null
    private var curPage: Int = 0 //当前页数
    private var pageCount: Int = 0//总页数
    private var delayTime: Long = 6000//延时


    private var tpageCount: Int = 0
    private var tcurPage: Int = 0
    private var tcount: Int = 23//页数
    private var tIndex: Int = 0;//哪一条资讯

    lateinit var mVideoplayer: SmartVideoPlayer

    var leftAdapet: ImageNetAdapter? = null
    var rightAdapet: ImageNetAdapter? = null


    @SuppressLint("HandlerLeak")
    internal var handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                101 -> {
                    curPage++
                    if (curPage < pageCount) {
                        loadDataInfoByPage(curPage)
                    } else {
                        curPage = 0
                        loadDataInfoByPage(0)
                    }
                }
                102 -> {
                    sendTxtMsg()
                }
                103 -> {
                    playMedia()
                }
                104 -> {
                    loadRightImageInfo()
                }
                105 -> {
                    tvTime.text = Tool.getCurTime()
                    tvDate.text = Tool.getCurDayAndWeek()
                    sendTime()
                }

            }

        }
    }

    /**
     * 保持屏幕常亮
     *
     * @param keep
     */
    protected open fun keepScreenOn(keep: Boolean) {
        val window: Window = getWindow()
        if (keep) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keepScreenOn(true)
        /**
         * 屏幕常亮需要 申请屏幕 WAKE_LOCK 唤醒锁 权限
         *  <uses-permission android:name="android.permission.WAKE_LOCK" />
         * **/
//        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//                android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main)
        mVideoplayer = findViewById(R.id.videoplayer)
        EventBus.getDefault().register(this)
//        var context = this@MainActivity
        //初始化音频管理器
//        val mAudioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        //获取系统最大音量
//        var maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
//        // 获取设备当前音量
//        var currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 设置seekbar的最大值
//        sbVolume.setMax(maxVolume);
        // 显示音量
        //  sbVolume.setProgress(currentVolume);

        tvIPAddr.text = PreferenceUtils.getString(this@MainActivity, "IPADDR", "")
        val chanel = SystemUtil.getChanel(this@MainActivity, "UMENG_CHANNEL")
        if (chanel.equals("doctor")) {
            orderType = 1
        } else if (chanel.equals("qucan")) {
            orderType = 2
        } else {
            orderType = 3
        }
        val isFirst = PreferenceUtils.getBoolean(this@MainActivity, "isFirst", true)
        if (isFirst) {
            PreferenceUtils.putBoolean(this@MainActivity, "isFirst", false)
        }

        startService(Intent(this@MainActivity, SmartServices::class.java))
        //设置后，跑马灯字体才会跑动
        tvNotice.isSelected = true
        isRun = true

        loadFileInfo()




        loadDataInfo(0)
        val title = SettingInfoCtrls.getTitleInfo(orderType)
        tvTitle.setText(if (TextUtils.isEmpty(title)) getString(R.string.des_title) else title)
        sendTxtMsg()
        tvOrderNameTitle.setText(SettingInfoCtrls.getOrderTitleInfo(orderType))
        if (Build.VERSION.SDK_INT >= 23) {
            //授权处理
            if (!PreferenceUtils.getBoolean(this@MainActivity, "permissions", false))
                allowPermission()
        }
    }

    private fun showSettingInfo() {
        //是否显示菜单
        val isShow = PreferenceUtils.getBoolean(this@MainActivity, "showMenu", false)
        showMenuLayout.visibility = if (isShow) View.VISIBLE else View.GONE
        val isShowTvNotice = PreferenceUtils.getBoolean(this@MainActivity, "isShowTvNotice", false)
        tvNoticeLayout.visibility = if (isShowTvNotice) View.VISIBLE else View.GONE
        val isShowScreen = PreferenceUtils.getBoolean(this@MainActivity, "isShowScreen", false)
        tvTitleLayout.visibility = if (isShowScreen) View.VISIBLE else View.GONE
        sendTime()
    }

    fun sendTime() {
        handler.sendEmptyMessageDelayed(105, 1000)
    }

    private fun loadFileInfo() {
        Thread(Runnable {
            listFile = FileUtils.findListFile1()
            handler.sendEmptyMessageDelayed(103, 500)
            handler.sendEmptyMessageDelayed(104, 500)
        }).start()
    }

    private fun playMedia() {
        runOnUiThread {
            val playVideo = PreferenceUtils.getBoolean(this@MainActivity, "playVideo", true)
            if (playVideo) {
                arrays = FileUtils.spitVideoListFile(listFile)
                videoplayer.visibility = View.VISIBLE
                leftSliderLayout.visibility = View.GONE
                if (arrays!!.size > 0) {
//                    videoplayer.visibility = View.VISIBLE
//                    leftSliderLayout.visibility = View.GONE
                    playVideo(arrays!![0])
                }

//                else {
//                    PreferenceUtils.putBoolean(this@MainActivity, "playVideo", false)
//                    leftSliderLayout.visibility = View.VISIBLE
//                    videoplayer.visibility = View.GONE
//                    loadImageInfo()
//                }
            } else {
                videoplayer.visibility = View.GONE
                leftSliderLayout.visibility = View.VISIBLE
                loadImageInfo()
            }
        }
    }


    private fun allowPermission() {
        val arrays = arrayOfNulls<String>(1)
        arrays[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(
            this, arrays,
            MY_PERMISSIONS_REQUEST_READ_CONTACTS
        )
    }

    private fun loadImageInfo() {
        //删除所有的
        try {
            leftSliderLayout?.let {
                val delayTime = PreferenceUtils.getInt(this@MainActivity, "delayTime", imgPlay)
                imglistFile = FileUtils.spitImgListFile(listFile)
                leftAdapet = ImageNetAdapter(imglistFile)
                val banner = it as Banner<File, ImageNetAdapter>
                banner.setAdapter(leftAdapet)
                it.setLoopTime(delayTime.toLong())
                it.isAutoLoop(true)
                it.start()
            }

        } catch (e: Exception) {
        }
        loadRightImageInfo()
    }


    /**
     * 右边图片
     */
    private fun loadRightImageInfo() {
        //删除所有的
        try {
            rightSliderLayout.let {
                it.setOrientation(Banner.VERTICAL)
                val delayTime = PreferenceUtils.getInt(this@MainActivity, "rightDelayTime", imgPlay)
                val rightImglistFile = FileUtils.spitRightImgListFile(listFile)
                LogUtils.sysout("===right===imglistFile=======${listFile!!.size}")
                rightAdapet = ImageNetAdapter(rightImglistFile)
                val banner = it as Banner<File, ImageNetAdapter>
                banner.setAdapter(rightAdapet)
                banner.setOrientation(Banner.VERTICAL)
                it.setLoopTime(delayTime.toLong())
                it.isAutoLoop(true)
                it.start()
            }

        } catch (e: Exception) {
        }

    }


    fun playVideo(playUrl: String) {
        if (TextUtils.isEmpty(url)) return
        mVideoplayer.setUp(playUrl, true, "")
        mVideoplayer.startPlayLogic()
        mVideoplayer.setOnVideoStateCallback {
            val lunbo = PreferenceUtils.getBoolean(this@MainActivity, "lunbo", true)
            if (lunbo) {
                if (null != arrays) {//
                    playIndex++
                    if (playIndex >= arrays!!.size) playIndex = 0
                    if (arrays!!.size > 0) {
                        playVideo(arrays!![playIndex])
                        PreferenceUtils.putString(
                            this@MainActivity,
                            "curVideoUrl",
                            arrays!![playIndex]
                        )
                    }
                }
            } else {
                videoplayer.startPlayLogic()
            }
        }
    }


    /**
     * 计算总页数
     */
    fun mackPageCount() {
        val size = RoomCtrls.loadRoomCount()
        if (size % RoomCtrls.PAGE_INDEX_COUNT == 0) {
            pageCount = size / RoomCtrls.PAGE_INDEX_COUNT
        } else {
            pageCount = size / RoomCtrls.PAGE_INDEX_COUNT + 1
        }
    }


    /**
     * 分页加载
     */
    fun loadDataInfoByPage(offet: Int) {
        val list = RoomCtrls.loadAllRoomInfo(offet)
        LogUtils.sysout("======list size========" + list.size)
        sendMsg()
    }

    fun sendMsg() {
        handler.removeMessages(101)
        handler.sendEmptyMessageDelayed(101, delayTime)
    }


    fun sendTxtMsg() {
        val list = NoticeInfoCtrls.loadCurNoticeInfo()
        if (list.size == 0) {
            tvNotice.setText("")
            tvNoticeTitle.setText("")
            return
        }

        if (tIndex % 2 == 0) {
            tvNoticeTitle.background = this.resources.getDrawable(R.drawable.tv_notice_title_bg)
        } else {
            tvNoticeTitle.background = this.resources.getDrawable(R.drawable.tv_notice_title_bg1)
        }

        val notice = list.get(tIndex)
        val content = notice.content
        val arry = this.resources.getStringArray(R.array.notice_items)
        when (notice.author) {
            "0" -> tvNoticeTitle.setText(arry[0])
            "1" -> tvNoticeTitle.setText(arry[1])
            "2" -> tvNoticeTitle.setText(arry[2])
            "3" -> tvNoticeTitle.setText(arry[3])
            "4" -> tvNoticeTitle.setText(arry[4])
            "5" -> tvNoticeTitle.setText(arry[5])
            "6" -> tvNoticeTitle.setText(arry[6])
            "7" -> tvNoticeTitle.setText(arry[7])
        }
        val page = content.length
        LogUtils.sysout("======page======" + page)
        if (page % tcount == 0) {
            tpageCount = page / tcount
        } else {
            tpageCount = page / tcount + 1
        }
        var showText = ""
        val subPage = (tcurPage + 1)
        LogUtils.sysout("======tpageCount======" + tpageCount)
        LogUtils.sysout("======subPage======" + subPage)
        if (subPage < tpageCount) {
            //1234567890
            showText = content.substring(tcurPage * tcount, subPage * tcount)
            tcurPage++
        } else {
            showText = content.substring(tcurPage * tcount, page)
            tcurPage = 0
            tIndex++//下一条资讯
            if (tIndex >= list.size) tIndex = 0
        }
        LogUtils.sysout("=showText====:" + showText)
        tvNotice.setText(showText)
        Texts.tra(tvNotice)
        handler.sendEmptyMessageDelayed(102, 4000)
    }

    /**
     * 加载数据
     */
    fun loadDataInfo(offet: Int) {
        mackPageCount()
        val list = RoomCtrls.loadAllRoomInfo(offet)
        LogUtils.sysout("======list size========" + list.size)
        if (list.size > 0) {
            if (pageCount > 1)
                sendMsg()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(message: String) {
        when (message) {
            CmdUtils.CMD1,
            CmdUtils.CMD2,
            CmdUtils.CMD3,
            CmdUtils.CMD4,
            CmdUtils.CMD5,
            CmdUtils.CMD7,
            CmdUtils.CMD22,
            CmdUtils.CMD21,
            CmdUtils.CMD26,
            CmdUtils.CMD37,
            CmdUtils.CMD8 -> {
                loadDataInfo(0)
            }
            CmdUtils.CMD6 -> {
                play()
                loadDataInfo(0)
            }

            CmdUtils.CMD9 -> {
                tvTitle.setText(SettingInfoCtrls.getTitleInfo(orderType))
            }
            CmdUtils.CMD10 -> {
                //清零
                tcurPage = 0
                tIndex = 0
                handler.removeMessages(102)
                sendTxtMsg()
            }
            CmdUtils.CMD11 -> {

            }
            CmdUtils.CMD12 -> {
                tvOrderNameTitle.setText(SettingInfoCtrls.getOrderTitleInfo(orderType))
            }
            CmdUtils.CMD14 -> {//增加节目
                if (!PreferenceUtils.getBoolean(this@MainActivity, "playVideo", false))
                    return
                arrays = FileUtils.spitVideoListFile(listFile)
            }
            CmdUtils.CMD15 -> {//下一个视频
                if (!PreferenceUtils.getBoolean(this@MainActivity, "playVideo", true))
                    return
                runOnUiThread {
                    playIndex++
                    if (playIndex >= arrays!!.size) playIndex = 0
                    playVideo(arrays!![playIndex])
                    PreferenceUtils.putString(this@MainActivity, "curVideoUrl", arrays!![playIndex])
                }
            }
            CmdUtils.CMD16 -> {//第一个视频
                if (!PreferenceUtils.getBoolean(this@MainActivity, "playVideo", true))
                    return
                runOnUiThread {
                    playIndex = 0
                    playVideo(arrays!![playIndex])
                    PreferenceUtils.putString(this@MainActivity, "curVideoUrl", arrays!![playIndex])
                }
            }
            CmdUtils.CMD17 -> {//固定
                runOnUiThread {
                    //  videoplayer.startButton.performClick()
                    val b = PreferenceUtils.getBoolean(this@MainActivity, "lunbo", true)
                    PreferenceUtils.putBoolean(this@MainActivity, "lunbo", !b)
                }
            }
            CmdUtils.CMD23 -> {//是否播放视频
                val b = PreferenceUtils.getBoolean(this@MainActivity, "playVideo", true)
                PreferenceUtils.putBoolean(this@MainActivity, "playVideo", !b)
            }
            CmdUtils.CMD25 -> {
                runOnUiThread {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) != PackageManager.PERMISSION_GRANTED
                        ) run {
                            //没有权限，申请一下
                            allowPermission()
                        }
                    }
                    val playVideo = PreferenceUtils.getBoolean(this@MainActivity, "playVideo", true)
                    if (playVideo) {
                        arrays = FileUtils.spitVideoListFile(listFile)
                        videoplayer.visibility = View.VISIBLE
                        leftSliderLayout.visibility = View.GONE
                        arrays?.let {
                            if (it.size > 0) {
                                playVideo(it[0])
                            }
                        }
                    } else {
                        mVideoplayer.onVideoPause()
                        videoplayer.visibility = View.GONE
                        leftSliderLayout.visibility = View.VISIBLE
                        loadImageInfo()
                    }
                }
            }
            CmdUtils.CMD27 -> {
                loadImageInfo()
            }
            CmdUtils.CMD38 -> {
                loadRightImageInfo()
            }
            CmdUtils.CMD29 -> {
                runOnUiThread {
                    try {
                        if (PreferenceUtils.getBoolean(
                                this@MainActivity,
                                "playVideo",
                                true
                            )
                        ) return@runOnUiThread
//                        leftSliderLayout!!.movePrevPosition()
                    } catch (e: Exception) {
                    }
                }
            }
            CmdUtils.CMD30 -> {
                runOnUiThread {
                    try {
                        if (PreferenceUtils.getBoolean(
                                this@MainActivity,
                                "playVideo",
                                true
                            )
                        ) return@runOnUiThread
//                        leftSliderLayout!!.moveNextPosition()
                    } catch (e: Exception) {
                    }
                }
            }
            CmdUtils.CMD31 -> {//停止播放
                runOnUiThread {
                    if (PreferenceUtils.getBoolean(
                            this@MainActivity,
                            "playVideo",
                            true
                        )
                    ) return@runOnUiThread
                    if (isPlayImage) {
                        leftSliderLayout!!.isAutoLoop(false)
                        isPlayImage = false
                    } else {
                        leftSliderLayout!!.isAutoLoop(true)
                        isPlayImage = true
                    }
                }
            }

            CmdUtils.CMD32 -> {//是否显示菜单
                runOnUiThread {
                    val isShow = PreferenceUtils.getBoolean(this@MainActivity, "showMenu", false)
                    showMenuLayout.visibility = if (isShow) View.VISIBLE else View.GONE
                }
            }
            CmdUtils.CMD35 -> {//是否全屏
                runOnUiThread {
                    val isShow = PreferenceUtils.getBoolean(this@MainActivity, "isShowScreen", true)
                    tvTitleLayout.visibility = if (isShow) View.VISIBLE else View.GONE
                }
            }
            CmdUtils.CMD36 -> {//是否跑马灯
                runOnUiThread {
                    val isShow =
                        PreferenceUtils.getBoolean(this@MainActivity, "isShowTvNotice", true)
                    tvNoticeLayout.visibility = if (isShow) View.VISIBLE else View.GONE
                }
            }
            CmdUtils.CMD39 -> {
                runOnUiThread {
                    listFile?.add(File(ConfigUtils.TEMP_PATH))
                    val playVideo = PreferenceUtils.getBoolean(this@MainActivity, "playVideo", true)
                    if (playVideo) {
                        arrays = FileUtils.spitVideoListFile(listFile)
                        videoplayer.visibility = View.VISIBLE
                        leftSliderLayout.visibility = View.GONE
                        if (arrays!!.size > 0) {
                            playVideo(arrays!![0])
                        }
                        return@runOnUiThread
                    }
                    loadImageInfo()
                }
            }
            CmdUtils.CMD40 -> {
                handler.postDelayed(Runnable {
                    loadFileInfo()
                }, 500)
            }
        }

    }


    fun play() {
        try {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val rt = RingtoneManager.getRingtone(getApplicationContext(), uri)
            rt.play()
        } catch (e: Exception) {
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PreferenceUtils.putBoolean(this, "permissions", true)
                    LogUtils.sysout("========onRequestPermissionsResult========")
                    loadFileInfo()
                }
                return
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mVideoplayer.release()
        EventBus.getDefault().removeStickyEvent(this)
        EventBus.getDefault().unregister(this)
        isRun = false
        stopService(Intent(this@MainActivity, SmartServices::class.java))
        handler.removeMessages(101)
        handler.removeMessages(102)
        handler.removeMessages(105)
    }

    override fun onResume() {
        super.onResume()
        showSettingInfo()
    }

    fun toast(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> {
                toast("这是菜单按钮")
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


}
