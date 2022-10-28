package com.tussle.angrycontrol.model.DB

import com.tussle.angrycontrol.sharedPreference.GlobalApplication

class Repo {
    private val dao = GlobalApplication.DBInstance.dao()
}