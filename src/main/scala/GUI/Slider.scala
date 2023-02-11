package gui

import sfml.system.*

import gui.UIComponent

class Slider(val min: Float = 0, val max: Float =1, val startValue: Float = 0.5) extends UIComponent:
    var currentValue = startValue
    var prevValue = startValue

    var onValueChangeFunction : (newValue: Float) => Unit = _ => {}
    def onValueChange =
      onValueChangeFunction(this.currentValue)