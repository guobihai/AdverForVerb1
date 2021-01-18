package vb.queue.com.adverforverb.adapts;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import vb.queue.com.adverforverb.R;

public class SettingAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SettingAdapter(@Nullable List<String> data) {
        super(R.layout.setting_item_layout, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, String s) {
        helper.setText(R.id.setting_title, s);
    }
}
