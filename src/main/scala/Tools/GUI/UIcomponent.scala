package gui

import scala.collection.mutable.ArrayBuffer
import sfml.graphics.*
import sfml.system.*
import sfml.Resource

import gui.*

enum EVisibility:
  case Visible, Collapsed, Hidden, HitTestInvisible, SelfHitTestInvisible
end EVisibility

class UIComponent(
    var width: Float = 100f,
    var height: Float = 100f
) extends Drawable with Resource with Clickable {

  var childs = ArrayBuffer[UIComponent]()
  var transform = Transform()

  var isFocused = false

  var globalBounds = Rect(0f,0f,0f,0f)

  override def clickBounds = this.globalBounds

  def close() = {}

  def position: Vector2[Float] = Vector2(0.0f, 0.0f)

  def position_=(x: Float, y: Float): Unit =
    this.position = Vector2(x, y)

  def position_=(position: Vector2[Float]) = {}

  // TODO
  // var ToolTipText: String
  // var ToolTipWidget : UIComponent
  // var Visibility :
  def draw(target: RenderTarget, states: RenderStates) =
    //val transformStates = RenderStates(states.transform.combine(this.transform))
    childs.foreach(_.draw(target, states))

  def addChild(component: UIComponent): Unit =
    this.childs += component

  def removeChild(component: UIComponent): Unit =
    this.childs -= component

  //def width = globalBounds.width
  //def height = globalBounds.height

}
