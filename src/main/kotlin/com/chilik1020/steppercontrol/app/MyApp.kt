package com.chilik1020.steppercontrol.app

import com.chilik1020.steppercontrol.model.ComPortDevice
import com.chilik1020.steppercontrol.view.MainView
import javafx.stage.Stage
import tornadofx.App

class MyApp: App(MainView::class, Styles::class) {

    private val comPortDevice: ComPortDevice by inject()
    override fun start(stage: Stage) {
        super.start(stage)
        stage.isResizable = false
    }

    override fun stop() {
        super.stop()
        comPortDevice.close()
    }
}