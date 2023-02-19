package gui

import scala.collection.mutable.ArrayBuffer
import sfml.graphics.*
import sfml.system.*
import sfml.Resource



enum EVisibility:
    case Visible, Collapsed, Hidden, HitTestInvisible, SelfHitTestInvisible
end EVisibility

class UIComponent extends Drawable with Resource :
    var height = 100
    var width = 100
    var globalBounds = Rect(0.0f,0.0f,0.0f,0.0f)
    var childs = ArrayBuffer[UIComponent]()
    var transform = Transform()

    var isFocused = false

    //var onClickedBind: () => Unit = () => {}
    //var onPressedBind: () => Unit = () => {}
    //var onHoveredBind: () => Unit  = () => {}
    //var onIdleBind : () => Unit = () => {}
    def close()=
        {}

    def position : Vector2[Float]= Vector2(0.0f,0.0f)

    def position_=(x: Float, y: Float): Unit=
        this.position= Vector2(x,y)

    def position_=(position: Vector2[Float])=
        {}

    //TODO
    //var ToolTipText: String
    //var ToolTipWidget : UIComponent
    //var Visibility :
    def draw(target: RenderTarget, states:RenderStates)=
        childs.foreach(_.draw(target,states))


    //def onPressed =
    //    this.onPressedBind()
    //def onCliked =
    //    this.isFocused = true
    //    this.onClickedBind()
    //def onHovered =
    //    this.onHoveredBind()
