package com.chilik1020.steppercontrol.viewmodel

import com.chilik1020.steppercontrol.base.DataFlowObserver
import com.chilik1020.steppercontrol.model.*
import com.chilik1020.steppercontrol.model.Command
import com.chilik1020.steppercontrol.utils.CMD1_CURRENT_ZOOM_FOCUS_ANSWER
import com.chilik1020.steppercontrol.utils.CMD1_FIXED_POINTS_ANSWER
import com.chilik1020.steppercontrol.utils.getPacketByteByCmd1
import javafx.beans.property.*
import javafx.collections.FXCollections
import tornadofx.*

class StepperControlViewModel : ViewModel() {

    private val comPortDevice: ComPortDevice by inject()
    private val commandDetector: ReceivedCommandDetector by inject()

    var portOpened = SimpleBooleanProperty(false)

    val stepsZoom : IntegerProperty = SimpleIntegerProperty(1)
    val stepsFocus : IntegerProperty = SimpleIntegerProperty(1)

    val pointNumber : IntegerProperty = SimpleIntegerProperty(1)
    val pointZoom : IntegerProperty = SimpleIntegerProperty(0)
    val pointFocus : IntegerProperty = SimpleIntegerProperty(0)

    var fixedPoints : ListProperty<ZoomFocusFixedPoint> = SimpleListProperty<ZoomFocusFixedPoint>(FXCollections.observableArrayList())

    init {
        subscriptions()
    }

    private fun subscriptions() {
        comPortDevice.getPostStateObservable().subscribe(object : DataFlowObserver<Boolean> {
            override fun onNext(state: Boolean) {
                portStateChanged(state)
            }
        })
        commandDetector.getReceivedPacketObservable().subscribe(object : DataFlowObserver<DataPacket> {
            override fun onNext(packet: DataPacket) {
                if (packet.cmd1 == CMD1_CURRENT_ZOOM_FOCUS_ANSWER)
                    receivedCurrentZoomFocus(packet)
                else if (packet.cmd1 == CMD1_FIXED_POINTS_ANSWER)
                    receivedFixedPointsList(packet)
            }
        })
    }

    fun onClick(cmd: Command) {
        when(cmd) {
            Command.Init -> sendInitCommand(cmd)
            Command.ZoomPlus -> sendZoomCommand(cmd)
            Command.ZoomMinus -> sendZoomCommand(cmd)
            Command.FocusPlus -> sendFocusCommand(cmd)
            Command.FocusMinus -> sendFocusCommand(cmd)
            Command.ReadZoomFocus -> readCurrentZoomFocus(cmd)
            Command.SaveFixedPoint -> saveCurrentPoint(cmd)
            Command.ReadFixedPoints -> readAllPoints(cmd)
            Command.NextPoint -> sendChangePoint(cmd)
            Command.PreviousPoint -> sendChangePoint(cmd)
        }
    }

    private fun receivedCurrentZoomFocus(packet: DataPacket) {
        pointNumber.value = packet.cmd2.toInt()
        pointZoom.value = packet.data1.toInt()
        pointFocus.value = packet.data2.toInt()
    }

    private fun receivedFixedPointsList(packet: DataPacket) {
        val point  = ZoomFocusFixedPoint(
                packet.cmd2.toInt(),
                packet.data1.toInt(),
                packet.data2.toInt())

        fixedPoints.add(point)
    }

    private fun sendInitCommand(command: Command) {
        createAndSendCommandBytes(command,0, 0,0)
    }

    private fun sendZoomCommand(command: Command) {
        createAndSendCommandBytes(command,0, stepsZoom.value,0)
    }

    private fun sendFocusCommand(command: Command) {
        createAndSendCommandBytes(command,0, stepsFocus.value,0)
    }

    private fun readCurrentZoomFocus(command: Command) {
        createAndSendCommandBytes(command,0, 0,0)
    }

    private fun sendChangePoint(command: Command) {
        createAndSendCommandBytes(command,0, 0,0)
        readCurrentZoomFocus(Command.ReadZoomFocus)
    }

    private fun saveCurrentPoint(command: Command) {
        createAndSendCommandBytes(command, pointNumber.value, pointZoom.value, pointFocus.value)
    }

    private fun readAllPoints(command: Command) {
        fixedPoints.clear()
        createAndSendCommandBytes(command, 0, 0, 0)
    }

    private fun createAndSendCommandBytes(cmd1: Command, cmd2: Int, data1: Int, data2: Int) {
        val buffer = getPacketByteByCmd1(cmd1,cmd2.toUByte(), data1.toUByte(), data2.toUByte())
        comPortDevice.sendBytes(buffer.toByteArray())
    }

    private fun portStateChanged(state: Boolean) {
        portOpened.value = state
    }
}