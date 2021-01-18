package vb.queue.com.adverforverb.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_time_show.*
import org.greenrobot.eventbus.EventBus

import vb.queue.com.adverforverb.R
import vb.queue.com.adverforverb.utils.CmdUtils
import vb.queue.com.adverforverb.utils.PreferenceUtils


class ShowTimeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_time_show, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bcheck = PreferenceUtils.getBoolean(activity, "playVideo", true)
        raVideoSetting.isChecked = bcheck
        raImgSetting.isChecked = !bcheck

        val nDelayTime = PreferenceUtils.getInt(activity, "delayTime", 6000)
        if (nDelayTime > 0) {
            et_delay.setText("${nDelayTime / 1000}")
        }


        val rightDelayTime = PreferenceUtils.getInt(activity, "rightDelayTime", 12000)
        if (rightDelayTime > 0) {
            et_rightdelay.setText("${rightDelayTime / 1000}")
        }

        switchGroup.setOnCheckedChangeListener { radioGroup, i ->
            val checkid = radioGroup.checkedRadioButtonId
            when (checkid) {
                R.id.raVideoSetting -> {
                    PreferenceUtils.putBoolean(activity, "playVideo", true)
                }
                R.id.raImgSetting -> {
                    PreferenceUtils.putBoolean(activity, "playVideo", false)
                }
            }
            EventBus.getDefault().post(CmdUtils.CMD25)
        }

        btnSetdelayTime.setOnClickListener {
            val delaytTime = et_delay.text.toString().trim()
            if (TextUtils.isEmpty(delaytTime)) {
                return@setOnClickListener
            }
            val time = delaytTime.toInt()
            if (time < 2) {
                toast(getString(R.string.second_time))
                return@setOnClickListener
            }
            if (time > 99) {
                toast(getString(R.string.second_err_time))
                return@setOnClickListener
            }
            PreferenceUtils.putInt(activity, "delayTime", time * 1000)
            EventBus.getDefault().post(CmdUtils.CMD27)
        }

        btnRightSetdelayTime.setOnClickListener {

            val delaytTime = et_rightdelay.text.toString().trim()
            if (TextUtils.isEmpty(delaytTime)) {
                return@setOnClickListener
            }
            val time = delaytTime.toInt()
            if (time < 2) {
                toast(getString(R.string.second_time))
                return@setOnClickListener
            }
            if (time > 99) {
                toast(getString(R.string.second_err_time))
                return@setOnClickListener
            }
            PreferenceUtils.putInt(activity, "rightDelayTime", time * 1000)
            EventBus.getDefault().post(CmdUtils.CMD38)

        }
    }

    fun toast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {

        fun newInstance(): ShowTimeFragment {
            val fragment = ShowTimeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor
