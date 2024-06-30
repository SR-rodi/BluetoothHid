package ru.sr.hidbluetooth.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothHidDeviceAppQosSettings
import android.bluetooth.BluetoothHidDeviceAppSdpSettings
import android.bluetooth.BluetoothProfile
import ru.sr.hidbluetooth.domain.BluetoothDeviceState
import ru.sr.hidbluetooth.domain.HidError

class ProfileListener(
    private val onHidRegistration: (BluetoothHidDevice) -> Unit,
    private val onHidUnRegistration: () -> Unit,
    private val onErrorRegistrations: (HidError) -> Unit
) : BluetoothProfile.ServiceListener {

    private val sdpRecord by lazy {
        BluetoothHidDeviceAppSdpSettings(
            "BluetoothHidDevice",
            "Mobile bluetooth Controller",
            "bla",
            BluetoothHidDevice.SUBCLASS1_COMBO,
            DescriptorCollection.MOUSE_KEYBOARD_COMBO
        )
    }
    private val qosOut by lazy {
        BluetoothHidDeviceAppQosSettings(
            BluetoothHidDeviceAppQosSettings.SERVICE_BEST_EFFORT,
            800,
            9,
            0,
            11250,
            BluetoothHidDeviceAppQosSettings.MAX
        )
    }

    @SuppressLint("MissingPermission")
    override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
        val bluetoothHidDevice = proxy as? BluetoothHidDevice
        if (profile != BluetoothProfile.HID_DEVICE || bluetoothHidDevice == null) {
            onErrorRegistrations.invoke(HidError.UN_CORRECT_BLUETOOTH_PROFILE)
            return
        }
        bluetoothHidDevice.registerApp(
            sdpRecord,
            null,
            qosOut,
            { it.run() },
            BluetoothHidCallback(
                deviceConnectionListener = {},
                deviceStateListener = {}
            )
        )

        onHidRegistration.invoke(bluetoothHidDevice)
    }


    override fun onServiceDisconnected(profile: Int) {
        if (profile == BluetoothProfile.HID_DEVICE)
            onHidUnRegistration.invoke()
    }


}

class BluetoothHidCallback(
    private val deviceConnectionListener: (device: BluetoothDevice) -> Unit,
    private val deviceStateListener: (state: BluetoothDeviceState) -> Unit
) :
    BluetoothHidDevice.Callback() {

    override fun onConnectionStateChanged(device: BluetoothDevice?, state: Int) {
        super.onConnectionStateChanged(device, state)
        if (device == null) {
            deviceStateListener.invoke(BluetoothDeviceState.UNKNOWN)
            return
        }
        val currentState = when (state) {
            BluetoothProfile.STATE_CONNECTED -> BluetoothDeviceState.CONNECTED
            BluetoothProfile.STATE_CONNECTING -> BluetoothDeviceState.CONNECTING
            BluetoothProfile.STATE_DISCONNECTING -> BluetoothDeviceState.DISCONNECTING
            BluetoothProfile.STATE_DISCONNECTED -> BluetoothDeviceState.DISCONNECTED
            else -> BluetoothDeviceState.UNKNOWN
        }
        deviceStateListener.invoke(currentState)

        if (currentState == BluetoothDeviceState.CONNECTED) {
            deviceConnectionListener.invoke(device)
        }
    }
}