package gui

import scala.collection.mutable.ArrayBuffer
import sfml.graphics.{RenderTarget, RenderStates, Rect}
import sfml.system.*
import gui.UIComponent


enum E_Direction:
  case Top, Bottom, Left ,Right

class VerticalBox extends UIComponent {

  var _position = Vector2(0f, 0f)
  var direction = E_Direction.Bottom
  var spacing = 5f

  def this(x: Float, y: Float) =
    this()
    this.position = (x, y)

  override def position: Vector2[Float] = this._position

  override def position_=(position: Vector2[Float]) =
    this._position = position
    this.updateChildPosition()

  def updateChildPosition() =
    var pos = this._position
    for child <- childs do {
      child.position = pos
      this.direction match {
        case E_Direction.Bottom => pos = pos + Vector2(0f, child.globalBounds.height + this.spacing)
        case E_Direction.Top => pos = pos - Vector2(0f, child.globalBounds.height + this.spacing)
        case E_Direction.Right => pos = pos + Vector2(child.globalBounds.width + this.spacing,0f)
        case E_Direction.Left => pos = pos - Vector2(child.globalBounds.width + this.spacing,0f)
      }
    }
    this.globalBounds = Rect(
      this._position.x,
      this.position.y,
      pos.x - this.position.x,
      pos.y - this.position.y
    )

  override def addChild(component: UIComponent) =
    this.childs += component
    this.updateChildPosition()

  override def removeChild(component: UIComponent) =
    this.childs -= component
    this.updateChildPosition()

  def removeAllChilds()=
    this.childs.clear()
    this.updateChildPosition()

  override def draw(target: RenderTarget, states: RenderStates) =
    for child <- childs do {
      child.draw(target, states)
    }

}
