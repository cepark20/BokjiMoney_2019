package com.example.myapplication.ui.notifications;

import android.view.View;

public interface OnNotiItemClickListener {
    public void onItemLongClick(NotiAdapter.ViewHolder holder, View view, int position);
}
