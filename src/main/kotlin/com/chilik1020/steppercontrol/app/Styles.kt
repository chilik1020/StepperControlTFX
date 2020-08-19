package com.chilik1020.steppercontrol.app

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val gridPane by cssclass()
        val labelH1 by cssclass()
        val textFieldStep by cssclass()
        val controlButton by cssclass()
        val controlSmallButton by cssclass()
        val comboBoxComPort by cssclass()
    }

    init {
        gridPane {
            hgap = 5.px
            vgap = 5.px
            padding = box(10.px)
        }

        label and labelH1 {
            fontSize = 13.px
            fontWeight = FontWeight.BOLD
            minWidth = 100.px
            alignment = Pos.CENTER
        }

        textFieldStep {
            alignment = Pos.CENTER_RIGHT
            minWidth = 100.px
            maxWidth = 100.px
            maxHeight = 20.px
        }

        controlButton {
            minWidth = 100.px
            maxWidth = 100.px
            maxHeight = 20.px
            alignment = Pos.CENTER
        }

        controlSmallButton {
            minWidth = 50.px
            maxWidth = 50.px
            maxHeight = 20.px
            alignment = Pos.CENTER
        }

        comboBoxComPort {
            minWidth = 100.px
            maxWidth = 100.px
            maxHeight = 20.px
            alignment = Pos.CENTER
        }

    }
}