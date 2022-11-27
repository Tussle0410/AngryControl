package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.SettingBackupLayoutBinding
import com.tussle.angrycontrol.sharedPreference.GlobalApplication
import com.tussle.angrycontrol.viewmodel.SettingBackUpViewModel
import de.raphaelebner.roomdatabasebackup.core.RoomBackup

class SettingBackUpActivity : AppCompatActivity() {
    private val viewModel : SettingBackUpViewModel by lazy {
        ViewModelProvider(this).get(SettingBackUpViewModel::class.java)
    }
    private lateinit var binding : SettingBackupLayoutBinding
    private val backUp = RoomBackup(this)
    private val restore = RoomBackup(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.setting_backup_layout)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
    }
    private fun init(){
        backUpInit()
        restoreInit()
        setButton()
    }
    private fun setButton(){
        binding.settingBackupBackButton.setOnClickListener {
            finish()
        }
        binding.settingBackupBackup.setOnClickListener {
            backUp.backup()
        }
        binding.settingBackupRestore.setOnClickListener {
            restore.restore()
        }
    }
    private fun backUpInit(){
        backUp
            .database(GlobalApplication.DBInstance)
            .enableLogDebug(true)
            .backupIsEncrypted(true)
            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
            .maxFileCount(5)
            .apply {
                onCompleteListener { success, _, _ ->
                    if(success)
                        viewModel.setBackUpDate()
                }
            }
    }
    private fun restoreInit(){
        restore
            .database(GlobalApplication.DBInstance)
            .enableLogDebug(true)
            .backupIsEncrypted(true)
            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
            .maxFileCount(5)
    }
}