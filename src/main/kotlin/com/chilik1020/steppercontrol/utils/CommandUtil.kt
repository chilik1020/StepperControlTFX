package com.chilik1020.steppercontrol.utils

import com.chilik1020.steppercontrol.model.Command

const val SYNC_BYTE : UByte = 0xFFu
const val ADDR_BYTE : UByte = 0x01u

const val CMD1_INIT : UByte = 0x01u
const val CMD1_ZOOM_FORWARD : UByte = 0x02u
const val CMD1_ZOOM_BACK : UByte = 0x04u
const val CMD1_FOCUS_FORWARD : UByte = 0x08u
const val CMD1_FOCUS_BACK : UByte = 0x10u
const val CMD1_NEXT_POINT : UByte = 0x20u
const val CMD1_PREVIOUS_POINT : UByte  = 0x40u
const val CMD1_READ_ZOOM_FOCUS : UByte = 0x80u
const val CMD1_READ_FIXED_POINTS : UByte = 0x81u
const val CMD1_WRITE_FIXED_POINTS : UByte = 0x82u

const val CMD1_CURRENT_ZOOM_FOCUS_ANSWER : UByte = 0x90u
const val CMD1_FIXED_POINTS_ANSWER : UByte = 0x91u


fun getPacketByteByCmd1(cmd1: Command, cmd2: UByte , data1: UByte, data2: UByte) : UByteArray {
    val cmd1Byte = getCommandByte(cmd1)

    val crc = calcCRC(ubyteArrayOf(ADDR_BYTE, cmd1Byte,cmd2, data1, data2))

    return ubyteArrayOf(
        SYNC_BYTE,
        ADDR_BYTE,
        cmd1Byte,
        cmd2,
        data1,
        data2,
        crc)
}

fun getCommandByte(command: Command) : UByte {
    return when(command) {
        Command.Init -> CMD1_INIT
        Command.ZoomPlus ->  CMD1_ZOOM_FORWARD
        Command.ZoomMinus -> CMD1_ZOOM_BACK
        Command.FocusPlus -> CMD1_FOCUS_FORWARD
        Command.FocusMinus -> CMD1_FOCUS_BACK
        Command.ReadZoomFocus -> CMD1_READ_ZOOM_FOCUS
        Command.SaveFixedPoint -> CMD1_WRITE_FIXED_POINTS
        Command.ReadFixedPoints -> CMD1_READ_FIXED_POINTS
        Command.NextPoint -> CMD1_NEXT_POINT
        Command.PreviousPoint -> CMD1_PREVIOUS_POINT
    }
}

fun calcCRC(buffer: UByteArray): UByte {
    var crc: Int = 0
    for (element in buffer) crc = crc + element.toByte()
    return crc.toUByte()
}

fun byteArrayOfInts(vararg ints: Int) = UByteArray(ints.size) { pos -> ints[pos].toUByte() }



