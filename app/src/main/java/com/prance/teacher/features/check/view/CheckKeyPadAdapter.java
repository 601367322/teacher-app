package com.prance.teacher.features.check.view;

import android.view.ViewGroup;

import com.prance.lib.teacher.base.ui.BaseLoadMoreAdapter;
import com.prance.lib.teacher.base.ui.BaseRecyclerAdapter;
import com.prance.lib.teacher.base.ui.BaseRecyclerHolder;
import com.prance.teacher.features.check.model.CheckKeyPadGroupTitle;

public class CheckKeyPadAdapter extends BaseLoadMoreAdapter<Object, BaseRecyclerHolder> {

    private static final int TITLE = 1;

    @Override
    public BaseRecyclerHolder onCreate(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new CheckKeyPadTitleHolder(parent);
        } else {
            return new CheckKeyPadHolder(parent);
        }
    }

    @Override
    public void onBind(BaseRecyclerHolder viewHolder, int position, Object data) {
        viewHolder.onBind(data);
    }

    @Override
    protected int getNormalViewType(int position) {
        if (getData(position) instanceof CheckKeyPadGroupTitle) {
            return TITLE;
        }
        return super.getNormalViewType(position);
    }
}
