package com.chilik1020.steppercontrol.model

sealed class Command {
    object Init : Command()
    object ZoomPlus : Command()
    object ZoomMinus : Command()
    object FocusPlus : Command()
    object FocusMinus : Command()
    object NextPoint: Command()
    object PreviousPoint : Command()
    object ReadZoomFocus : Command()
    object SaveFixedPoint : Command()
    object ReadFixedPoints : Command()
}