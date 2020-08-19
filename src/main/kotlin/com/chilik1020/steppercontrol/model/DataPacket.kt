package com.chilik1020.steppercontrol.model

import com.chilik1020.steppercontrol.utils.ADDR_BYTE
import com.chilik1020.steppercontrol.utils.SYNC_BYTE

class DataPacket(
        val cmd1: UByte,
        val cmd2: UByte,
        val data1: UByte,
        val data2: UByte,
        val crc: UByte) {
    val syncByte = SYNC_BYTE
    val addrByte = ADDR_BYTE

    override fun toString(): String {
        return "DataPacket(syncByte=$syncByte, addrByte=$addrByte, cmd1=$cmd1, cmd2=$cmd2, data1=$data1, data2=$data2, crc=$crc)"
    }
}