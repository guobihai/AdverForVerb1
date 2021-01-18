package vb.queue.com.adverforverb

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vb.queue.com.adverforverb.utils.ActivityHook

open class BaseActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityHook.hookOrientation(this);//hook，绕过检查
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 26) {
            ActivityHook.convertActivityFromTranslucent(this);
        }
    }
}