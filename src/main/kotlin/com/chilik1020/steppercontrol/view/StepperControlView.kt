package com.chilik1020.steppercontrol.view

import com.chilik1020.steppercontrol.app.Styles
import com.chilik1020.steppercontrol.model.Command
import com.chilik1020.steppercontrol.model.ZoomFocusFixedPoint
import com.chilik1020.steppercontrol.viewmodel.StepperControlViewModel
import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import tornadofx.*

class StepperControlView : View("Zoom/Focus control") {

    private val viewModel : StepperControlViewModel by inject()

    override val root = vbox {
        disableProperty().bind(!viewModel.portOpened)

        separator {  }

        gridpane {
            addClass(Styles.gridPane)

            row {
                label {
                    tooltip("250 максимум")
                    addClass(Styles.labelH1)
                    text = "Steps (1..250)"
                }
                textfield(viewModel.stepsZoom) {
                    tooltip("1 = 8 шагов двигателя")
                    addClass(Styles.textFieldStep)
                    promptText = "steps"
                }
                button {
                    addClass(Styles.controlButton)
                    text = "Zoom +"
                    action {
                        viewModel.onClick(Command.ZoomPlus)
                    }
                }
                button {

                    addClass(Styles.controlButton)
                    text = "Zoom -"
                    action {
                        viewModel.onClick(Command.ZoomMinus)
                    }
                }
            }

            row {
                label {
                    tooltip("250 максимум")
                    addClass(Styles.labelH1)
                    text = "Steps (1..250)"
                }
                textfield(viewModel.stepsFocus) {
                    tooltip("1 = 8 шагов двигателя")
                    addClass(Styles.textFieldStep)
                    promptText = "steps"
                }
                button {
                    addClass(Styles.controlButton)
                    text = "Focus +"
                    action {
                        viewModel.onClick(Command.FocusPlus)
                    }
                }
                button {
                    addClass(Styles.controlButton)
                    text = "Focus -"
                    action {
                        viewModel.onClick(Command.FocusMinus)
                    }
                }
            }

            row {
                label {
                    tooltip("Zoom по фиксированным точкам")
                    addClass(Styles.labelH1)
                    text = "Points"
                }

                button {
                    addClass(Styles.controlButton)
                    text = "Init"
                    action {
                        viewModel.onClick(Command.Init)
                    }
                }

                button {
                    addClass(Styles.controlButton)
                    text = "Next"
                    action {
                        viewModel.onClick(Command.NextPoint)
                    }
                }

                button {
                    addClass(Styles.controlButton)
                    text = "Previous"
                    action {
                        viewModel.onClick(Command.PreviousPoint)
                    }
                }
            }

            row {
                label {
                    addClass(Styles.labelH1)
                    text = "Zoom"
                }

                label {
                    addClass(Styles.labelH1)
                    text = "Focus ->"
                }
                label {
                    addClass(Styles.labelH1)
                    text = "Focus <-"
                }
            }

            row {
                textfield(viewModel.pointZoom) {
                    tooltip("Zoom")
                    addClass(Styles.textFieldStep)
                    promptText = "Zoom"
                }

                textfield(viewModel.pointFocusForward) {
                    tooltip("Focus forward")
                    addClass(Styles.textFieldStep)
                    promptText = "Focus forward"
                }

                textfield(viewModel.pointFocusBack) {
                    tooltip("Focus back")
                    addClass(Styles.textFieldStep)
                    promptText = "Focus back"
                }

                hbox {
                    button {
                        addClass(Styles.controlSmallButton)
                        tooltip("Вычитать текущее значение zoom и focus")
                        text = "Load"
                        action {
                            viewModel.onClick(Command.ReadZoomFocus)
                        }
                    }

                    button {
                        addClass(Styles.controlSmallButton)
                        tooltip("Сохранить точку")
                        text = "Save"
                        action {
                            viewModel.onClick(Command.SaveFixedPoint)
                        }
                    }
                }
            }

            row {
                button {
                    addClass(Styles.controlButton)
                    tooltip("Test")
                    text = "Test"
                    action {
                        viewModel.onClickTest()
                    }
                }
            }
        }

        val updateItems = MenuItem("Update")
        updateItems.onAction = EventHandler {
            viewModel.onClick(Command.ReadFixedPoints)
        }

        tableview(viewModel.fixedPoints) {
            tooltip("Правой кнопкой жми")
            readonlyColumn("№",ZoomFocusFixedPoint::id)
            readonlyColumn("Zoom", ZoomFocusFixedPoint::zoom)
            readonlyColumn("Focus Forward", ZoomFocusFixedPoint::focusForward)
            readonlyColumn("Focus Back", ZoomFocusFixedPoint::focusBack)
        }.contextMenu = ContextMenu(updateItems)
    }
}

