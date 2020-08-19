package com.chilik1020.steppercontrol.model

import com.chilik1020.steppercontrol.base.DataFlowObserver
import com.chilik1020.steppercontrol.utils.ADDR_BYTE
import com.chilik1020.steppercontrol.utils.SYNC_BYTE
import com.chilik1020.steppercontrol.utils.calcCRC
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import tornadofx.*
import java.util.*

class ReceivedCommandDetector : DataFlowObserver<UByte>, Component(), ScopedInstance {

    private val comPortDevice: ComPortDevice by inject()
    init {
        comPortDevice.receivedBufferObservable().subscribe(this)
    }
    private val bytes: Queue<UByte> = LinkedList<UByte>()

    private var receivedPacketObservable: Subject<DataPacket> = PublishSubject.create()

    fun getReceivedPacketObservable() = receivedPacketObservable

    override fun onNext(byte: UByte) {
        bytes.add(byte)
        detectCommand()
    }

    private fun detectCommand() {
        if (bytes.size < 7)
            return

        val bytesList = bytes.toList()
        if (bytesList[0].toUByte() != SYNC_BYTE.toUByte()) {
            bytes.remove()
            return
        }

        if (bytesList[1].toUByte() != ADDR_BYTE.toUByte()) {
            bytes.remove()
            return
        }

        val crc = calcCRC(bytesList.subList(1,6).toUByteArray())
        if (bytesList[6] != crc.toUByte()) {
            bytes.remove()
            return
        }

        val packet = DataPacket(
                bytesList[2].toUByte(),
                bytesList[3].toUByte(),
                bytesList[4].toUByte(),
                bytesList[5].toUByte(),
                crc.toUByte())
        println("RECEIVED: $packet")
        receivedPacketObservable.onNext(packet)

        bytes.clear()
    }
}