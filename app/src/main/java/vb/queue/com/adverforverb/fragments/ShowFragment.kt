package vb.queue.com.adverforverb.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_show.*

import vb.queue.com.adverforverb.R
import vb.queue.com.adverforverb.utils.PreferenceUtils


class ShowFragment : Fragment() {

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
        val view = inflater!!.inflate(R.layout.fragment_show, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bShowMenu = PreferenceUtils.getBoolean(activity, "showMenu", false)
        switchPrint.isChecked = bShowMenu

        val bShowScreen = PreferenceUtils.getBoolean(activity, "isShowScreen", false)

        switchFullScreen.isChecked = bShowScreen

        val bShowNotice = PreferenceUtils.getBoolean(activity, "isShowTvNotice", false)
        switchNotice.isChecked = bShowNotice

        switchPrint.setOnCheckedChangeListener { button, b ->
            PreferenceUtils.putBoolean(activity, "showMenu", b)
//            EventBus.getDefault().post(CmdUtils.CMD32)
        }
        switchFullScreen.setOnCheckedChangeListener { button, b ->
            PreferenceUtils.putBoolean(activity, "isShowScreen", b)
//            EventBus.getDefault().post(CmdUtils.CMD35)
        }
        switchNotice.setOnCheckedChangeListener { button, b ->
            PreferenceUtils.putBoolean(activity, "isShowTvNotice", b)
//            EventBus.getDefault().post(CmdUtils.CMD36)
        }
    }

    companion object {

        fun newInstance(): ShowFragment {
            val fragment = ShowFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor
