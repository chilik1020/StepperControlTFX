package com.chilik1020.steppercontrol.viewmodel

import com.chilik1020.steppercontrol.base.DataFlowObserver
import com.chilik1020.steppercontrol.model.*
import com.chilik1020.steppercontrol.model.Command
import com.chilik1020.steppercontrol.utils.CMD1_CURRENT_ZOOM_FOCUS_ANSWER
import com.chilik1020.steppercontrol.utils.CMD1_FIXED_POINTS_ANSWER
import com.chilik1020.steppercontrol.utils.getPacketByteByCmd1
import javafx.beans.property.*
import javafx.collections.FXCollections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tornadofx.*
import kotlin.random.Random

class StepperControlViewModel : ViewModel() {

    private val comPortDevice: ComPortDevice by inject()
    private val commandDetector: ReceivedCommandDetector by inject()

    var portOpened = SimpleBooleanProperty(false)

    var testRunning = SimpleBooleanProperty(false)

    val stepsZoom : IntegerProperty = SimpleIntegerProperty(1)
    val stepsFocus : IntegerProperty = SimpleIntegerProperty(1)

    val pointZoom : IntegerProperty = SimpleIntegerProperty(0)
    val pointFocusForward : IntegerProperty = SimpleIntegerProperty(0)
    val pointFocusBack : IntegerProperty = SimpleIntegerProperty(0)

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

    fun onClickTest() {
        testRunning.value = !testRunning.value
        runAsync {
            var steps = 0
            while (testRunning.value) {
                steps = Random.nextInt(1,39)
                for(i in 0..steps) {
                    sendChangePoint(Command.NextPoint)
                    Thread.sleep(300)
                }
                steps = Random.nextInt(1,39)
                for(i in 0..steps) {
                    sendChangePoint(Command.PreviousPoint)
                    Thread.sleep(300)
                }
            }
        }
    }

    private fun receivedCurrentZoomFocus(packet: DataPacket) {
        pointZoom.value = packet.data1.toInt()
        pointFocusForward.value = packet.data2.toInt()
        pointFocusBack.value = packet.data2.toInt()
    }

    private fun receivedFixedPointsList(packet: DataPacket) {
        val point  = ZoomFocusFixedPoint(
                packet.cmd2.toInt()/5 + 1,
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
      //  readCurrentZoomFocus(Command.ReadZoomFocus)
    }

    private fun saveCurrentPoint(command: Command) {
        createAndSendCommandBytes(command, pointZoom.value, pointFocusForward.value, pointFocusBack.value)
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