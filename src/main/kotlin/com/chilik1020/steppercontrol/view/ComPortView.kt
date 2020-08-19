package com.chilik1020.steppercontrol.view

import com.chilik1020.steppercontrol.app.Styles
import com.chilik1020.steppercontrol.viewmodel.ComPortViewModel
import tornadofx.*

class ComPortView : View("My View") {

    private val viewModel: ComPortViewModel by inject()

    override val root = gridpane {

        addClass(Styles.gridPane)

        row {
            combobox<String> {
                addClass(Styles.comboBoxComPort)
                disableProperty().bind(viewModel.portOpened)
                itemsProperty().bind(viewModel.portListProperty)

                setOnAction {
                    viewModel.portSelected = selectedItem.toProperty()
                }
            }

            button {
                addClass(Styles.controlButton)
                textProperty().bind(viewModel.btnOpenPortText)
                action {
                    viewModel.connect()
                }
            }
        }
    }

}
