package com.tussle.angrycontrol.ui.listener

import com.tussle.angrycontrol.model.DateAndDiary

interface DiaryCallBackListener {
    fun DiaryShowIntent(info : DateAndDiary)
}