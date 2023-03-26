package gui

import event.*
import sfml.window.Mouse
import math.*



class ScrollbarButton() extends UIComponent{

  object OnButtonGrabbed extends Event[(Int /*x*/,Int /*y*/)]

  object OnButtonMoved extends Event[(Int /*x*/,Int /*y*/)]

  object OnButtonReleased extends Event[Unit]

  private var isGrabbed = false


  OnMouseButtonPressed.connect(onMouseButtonPress)
  def onMouseButtonPress(button: Mouse.Button,x:Int,y:Int)={
    if(button == Mouse.Button.Left){
      this.isGrabbed = true
      OnButtonGrabbed(x,y)

      //TODO make style
    }
  }

  OnMouseButtonReleased.connect(onMouseButtonPress)
  def onMouseButtonRelease(button: Mouse.Button,x:Int,y:Int)={
    if(button == Mouse.Button.Left){
      this.isGrabbed = false
      OnButtonReleased(())

      //TODO make style
    }
  }

  OnMouseMoved.connect(onMouseMove)
  def onMouseMove(x: Int, y:Int)={
    if(isGrabbed){
      OnButtonMoved(x,y)
    }
  }

}


enum ScrollbarOrientation:
    case Horizontal, Vertical

class Scrollbar(
  var orientation: ScrollbarOrientation = ScrollbarOrientation.Vertical,
  var minimumValue: Float = 0f,
  var maximumValue: Float = 1f,
  var step: Float = 1f
) extends UIComponent{

  object OnScrollbarValueUpdate extends Event[Float /*newValue*/]

  var scrollbarButton = ScrollbarButton()

  private var _value = 0f
  var grabbedValue = 0f
  var grabbedPosition = 1

  //init
  private def init(): Unit={
  orientation match
    case ScrollbarOrientation.Horizontal => {
      scrollbarButton.OnButtonGrabbed.connect((x,y) =>
        {
          grabbedPosition = x
          grabbedValue = value
        })


      scrollbarButton.OnButtonMoved.connect((x,y) =>
      {
        var deltaX = x - grabbedPosition;
        if (deltaX == 0) then
          return;

        var scrollbarWidth = step * width
        var remainingWidth = width - scrollbarButton.width - scrollbarWidth
        var valueRange = maximumValue - minimumValue

        value = grabbedValue + deltaX * valueRange / remainingWidth
      })

    }
    case ScrollbarOrientation.Vertical =>{
      scrollbarButton.OnButtonGrabbed.connect((x,y) =>
        {
          grabbedPosition = y
          grabbedValue = value
        })


      scrollbarButton.OnButtonMoved.connect((x,y) =>
      {
        var deltaY = y - grabbedPosition;
        if (deltaY == 0) then
          return

        var scrollbarHeight = step * height
        var remainingHeight = height - scrollbarButton.height - scrollbarHeight
        var valueRange = maximumValue - minimumValue

        value = grabbedValue + deltaY * valueRange / remainingHeight
      })
    }

  }



  def value = _value

  def value_=(newValue: Float)={
    _value = min(maximumValue,max(minimumValue,newValue))
    OnScrollbarValueUpdate(_value)
  }









}
