package vb.queue.com.adverforverb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.activity_setting.*
import vb.queue.com.adverforverb.adapts.SettingAdapter
import vb.queue.com.adverforverb.fragments.ShowFragment
import vb.queue.com.adverforverb.fragments.ShowTimeFragment
import vb.queue.com.adverforverb.views.SimpleDividerDecoration


class SettingActivity : AppCompatActivity() {

    var fragmentCurr: androidx.fragment.app.Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        val linnermanger = LinearLayoutManager(this)
        recyclerView.layoutManager = linnermanger
        recyclerView.addItemDecoration(SimpleDividerDecoration(this))
        val tData = this.resources.getStringArray(R.array.setting_arrays)
        val list = tData.toList()
        val settingAdapt = SettingAdapter(list)
        recyclerView.adapter = settingAdapt

        val fragmentShow = ShowFragment.newInstance()
        val fragmentTimeShow = ShowTimeFragment.newInstance()
        showFragment(fragmentTimeShow)

        settingAdapt.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                when (position) {
                    0 -> {
                        showFragment(fragmentTimeShow)
                    }
                    1 -> {
                        showFragment(fragmentShow)
                    }
                }
            }

        })
    }

    fun showFragment(fragmentTo: androidx.fragment.app.Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        if (fragmentCurr === fragmentTo) {
            return
        }

        if (fragmentTo.isAdded()) {
            transaction.show(fragmentTo)
        } else {
            transaction.add(R.id.lfContainer, fragmentTo).show(fragmentTo)
        }

        if (null != fragmentCurr) {
            transaction.hide(fragmentCurr!!)   //hide()函数放在show()函数后面。保证焦点存在
        }

        fragmentCurr = fragmentTo
        transaction.commit()
    }
}
