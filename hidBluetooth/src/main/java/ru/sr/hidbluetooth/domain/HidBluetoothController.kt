package ru.sr.hidbluetooth.domain

interface HidBluetoothController {

    fun startScan()
    fun stopScan()
    fun deleteHid()

    fun isBluetoothEnabled(): Boolean

    fun connectToDevice(macAddress: String)

    fun sendEvent(event: ButtonEvent)
}

