package com.example.bt_dev.services


import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.UUID

class ConnectThreadService(device: BluetoothDevice) : Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    private var btSocket: BluetoothSocket? = null

    init {
        //открываем канал соединения с микроконтролером
        try {
            btSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (e: IOException) {

        } catch (e: SecurityException) {

        }
    }

    override fun run() {
        try {
            btSocket?.connect()
        } catch (e: IOException) {
            //если вдруг прервалась связь с микроконтролером
        } catch (e: SecurityException) {

        }
    }

    fun closeConnection() {
        try {
            btSocket?.close()
        } catch (e: IOException) {
            //если вдруг прервалась связь с микроконтролером
        }
    }
}