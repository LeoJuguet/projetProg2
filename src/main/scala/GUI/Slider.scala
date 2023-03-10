package gui

import sfml.system.*
import sfml.graphics.*


import gui.{UIComponent, E_Direction, Style, ShapeStyle, Button, ButtonStyle}


class SliderStyle(
  val cursorStyle: ButtonStyle = ButtonStyle(idleStyle = ButtonStyleState(shapeStyle = ShapeStyle(style = Style(fillColor = Color.Red())))),
  val barStyle: ShapeStyle = ShapeStyle()
){
  def apply(cursor: Button, bar: Shape)=
    cursor.applyStyle()
    barStyle.apply(bar)
}

class Slider(
  var x: Float = 0f,
  var y: Float = 0f,
  width: Float,
  height: Float,

  var min: Float = 0f,
  var max: Float = 1f,
  var sizeBar : Float= 0.2f,
  var direction : E_Direction = E_Direction.Right,
  var padding: Float = 0.01f,
  var value: Float = 0.5f,
  var style : SliderStyle = SliderStyle()
) extends UIComponent(width, height){
  var currentValue = value
  var prevValue = value

  private var sliderCursor = Button(width = 2f, height = this.height, buttonStyle = style.cursorStyle)
  private var sliderBar = RectangleShape(this.width,this.height * this.sizeBar)

  this.sliderBar.origin = (0f,-this.height / 2 + this.height * this.sizeBar / 2)
  this.applyStyle(this.style)
  this.position = (x,y)

  this.sliderCursor.onPressedBind = () => {
    print("oui")
  }


  var onValueChangeBind: (newValue: Float) => Unit = _ => {}
  def onValueChange =
    onValueChangeBind(this.currentValue)


  def applyStyle(sliderStyle: SliderStyle)=
        sliderStyle.apply(this.sliderCursor, this.sliderBar)
/*
  override def position: Vector2[Float]= this.sliderBar.position

  override def position_=(position: Vector2[Float]) =
        this.sliderBar.position = position
        this.sliderCursor.position = (position.x + this.width * this.value - 1f, position.y)
        this.globalBounds = Rect(this.sliderBar.position.x, this.sliderBar.position.y,
        this.width, this.height)
 */

  override def draw(target: RenderTarget, states: RenderStates) =
        val transformStates = RenderStates(states.transform.combine(this.transform))
        this.sliderBar.draw(target,transformStates)
        this.sliderCursor.draw(target,transformStates)


  override def updateClick(mousePos: Vector2[Float], leftMouse: Boolean)=
    this.sliderCursor.updateClick(mousePos,leftMouse)
}
