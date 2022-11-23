package com.tussle.angrycontrol.ui.listener

import com.tussle.angrycontrol.model.DateAndDiary

interface DiaryCallBackListener {
    fun diaryShowIntent(info : DateAndDiary)
}