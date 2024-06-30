package ru.sr.hidbluetooth.data

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import ru.sr.hidbluetooth.domain.ButtonEvent
import ru.sr.hidbluetooth.domain.HidBluetoothController

class AndroidHidController(
    private val applicationContext: Context
) : HidBluetoothController {

    private val bluetoothAdapter =
        applicationContext.getSystemService(BluetoothManager::class.java)?.adapter


    init {
        registrationHidDevice()
    }

    override fun startScan() {

    }

    override fun stopScan() {
        TODO("Not yet implemented")
    }

    override fun deleteHid() {
        TODO("Not yet implemented")
    }

    override fun isBluetoothEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun connectToDevice(macAddress: String) {
        TODO("Not yet implemented")
    }

    override fun sendEvent(event: ButtonEvent) {
        TODO("Not yet implemented")
    }

    private fun registrationHidDevice() {
        bluetoothAdapter?.getProfileProxy(
            applicationContext,
            ProfileListener(
                onHidRegistration = {},
                onHidUnRegistration = {},
                onErrorRegistrations = {}
            ),
            BluetoothProfile.HID_DEVICE
        )
    }
}