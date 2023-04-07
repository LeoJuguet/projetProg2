package gui

import scala.collection.mutable.ArrayBuffer
import sfml.graphics.*
import sfml.system.*

import gui.{UIComponent, Button, VerticalBox, E_Direction}


class SelectBox(
  var x: Float = 0f,
  var y: Float = 0f,
  width: Float = 200,
  height: Float= 50,
  var selected: Int = 0,
  var direction: E_Direction = E_Direction.Bottom,
  var items: ArrayBuffer[String] = ArrayBuffer[String](),
  var onSelectedBind: Int => Unit = (i: Int) => {print(i)}
) extends UIComponent(width, height){

  private var principalButton = Button(x = x, y = y, width = width, height = height, string = items(selected))
  private var verticalBox = this.initVerticalBox()
  this.verticalBox.direction = this.direction
  private var isChanged = false
  var isExpands = false

  private def initVerticalBox(): VerticalBox=
    var x = this.x
    var y = this.y
    this.direction match {
      case E_Direction.Bottom => y += this.height
      case E_Direction.Top => y -= this.height
      case E_Direction.Right => x += this.width
      case E_Direction.Left => x -= this.width
    }
    VerticalBox(x,y)

  principalButton.onClickedBind = () => {
    this.expand()
  }

  private def expand(): Unit =
    if this.isExpands then{
      this.verticalBox.removeAllChilds()
      this.isExpands = false
    }else{
      var i = 0
      for item <- this.items do{
        var bt = Button(0,0,this.width,this.height,item)
        val bti = i
        bt.onClickedBind = () => {
          this.selected = bti
          this.isChanged = true
        }
        verticalBox.addChild(bt)
        i = i+1
      }
      this.isExpands = true
    }

  private def onSelected(): Unit=
    this.principalButton.text.string = this.items(this.selected)
    this.expand()
    this.onSelectedBind(this.selected)


  override def updateClick(mousePos: Vector2[Float], leftMouse: Boolean) =
    this.isChanged = false
    this.principalButton.updateClick(mousePos,leftMouse)
    for child <- this.verticalBox.childs do {
        child.updateClick(mousePos,leftMouse)
    }
    if this.isChanged then onSelected()
    return this.isChanged

  override def draw(target: RenderTarget, states: RenderStates)=
    this.principalButton.draw(target,states)
    if this.isExpands then this.verticalBox.draw(target,states)
}
