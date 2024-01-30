package com.example.bt_dev.services


import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.UUID

class ConnectThread(device: BluetoothDevice) : Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    private var btSocket: BluetoothSocket? = null

    init {
        //открываем канал соединения с микроконтролером
        try {
            btSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (e: IOException) {
            Log.d("MyLog", "IOException $e")
        } catch (e: SecurityException) {
            Log.d("MyLog", "SecurityException $e")
        }
    }

    override fun run() {
        try {
            Log.d("MyLog", "Connecting...")
            btSocket?.connect()
            Log.d("MyLog", "Connected")
        } catch (e: IOException) {
            Log.d("MyLog", "IOException $e")
        } catch (e: SecurityException) {
            Log.d("MyLog", "SecurityException $e")
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