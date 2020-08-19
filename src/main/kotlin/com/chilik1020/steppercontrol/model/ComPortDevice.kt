package com.chilik1020.steppercontrol.model

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import jssc.*
import tornadofx.*

class ComPortDevice : Component(), ScopedInstance {

    companion object {
        private var serialPort: SerialPort? = null

    }

    private var portStateObservable: Subject<Boolean> = PublishSubject.create()
    private lateinit var portName: String

    private var receivedBufferObservable: Subject<UByte> = PublishSubject.create()

    init {
        setPortState(false)
    }

    fun open(port: String) {
        serialPort = SerialPort(port)
        portName = port
        try {
            serialPort?.apply {
                openPort()
                setParams(
                        SerialPort.BAUDRATE_115200,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE
                )
            }

            //Выключаем аппаратное управление потоком
           // serialPort?.setFlowControlMode(SerialPort.FLOWCONTROL_NONE)
            //Устанавливаем ивент лисенер и маску
            val mask = SerialPort.MASK_RXCHAR
            serialPort?.addEventListener(PortReader(), mask)
            setPortState(true)
        } catch (e: SerialPortException) {
            println("ERROR: " + e.message)
        }
    }

    fun sendBytes(buffer: ByteArray) {
        print("SEND: ")
        buffer.forEach { print("%02x ".format(it)) }
        println()
        serialPort?.writeBytes(buffer)
    }
    fun getPostStateObservable() = portStateObservable

    fun receivedBufferObservable() = receivedBufferObservable

    fun getPortList(): ObservableList<String>? {
        return FXCollections.observableList(SerialPortList.getPortNames().asList())
    }

    fun close() {
        serialPort?.closePort()
        setPortState(false)
    }

    private fun setPortState(state: Boolean) {
        portStateObservable.onNext(state)
    }

    private inner class PortReader : SerialPortEventListener {
        override fun serialEvent(event: SerialPortEvent) {
            if (event.isRXCHAR) {
                if (event.eventValue > 0) {
                    try {
                        val buffer: ByteArray? = serialPort?.readBytes()
                        buffer?.forEach {
                            receivedBufferObservable.onNext(it.toUByte())
                        }
                    } catch (ex: Exception) {
                        println(ex.message)
                    }
                }
            }
        }
    }
}