package com.chilik1020.steppercontrol.viewmodel

import com.chilik1020.steppercontrol.base.DataFlowObserver

import com.chilik1020.steppercontrol.model.ComPortDevice
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class ComPortViewModel : ViewModel() {

    private val comPortDevice: ComPortDevice by inject()

    var portOpened = SimpleBooleanProperty(false)
    var btnOpenPortText = SimpleStringProperty("Open")
    var portSelected = SimpleStringProperty(comPortDevice.getPortList()?.firstOrNull())

    var portListProperty = SimpleListProperty(comPortDevice.getPortList())

    init {
        comPortDevice.getPostStateObservable().subscribe(object : DataFlowObserver<Boolean> {
            override fun onNext(state: Boolean) {
                portStateChanged(state)
            }
        })
    }

    fun connect() {
        if (portOpened.value)
            comPortDevice.close()
         else
            comPortDevice.open(portSelected.value)
    }

    private fun portStateChanged(state: Boolean) {
        portOpened.value = state
        if(state)
            btnOpenPortText.value = "Close"
        else
            btnOpenPortText.value = "Open"
    }
}