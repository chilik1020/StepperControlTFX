package com.chilik1020.steppercontrol.view

import tornadofx.*

class MainView : View("Stepper control") {

    private val comPortView : ComPortView by inject()
    private val stepperControlView : StepperControlView by inject()

    override val root = vbox {
        add(comPortView)
        add(stepperControlView)
    }
}