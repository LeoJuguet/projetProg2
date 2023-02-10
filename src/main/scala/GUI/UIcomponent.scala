package gui

import scala.collection.mutable.ArrayBuffer
import sfml.graphics.*
import sfml.Resource



enum EVisibility:
    case Visible, Collapsed, Hidden, HitTestInvisible, SelfHitTestInvisible
end EVisibility

class UIComponent extends Transformable with Drawable with Resource :
    var height = 100
    var width = 100
    var childs = ArrayBuffer[UIComponent]()

    var isFocused = false

    //var onClickedBind: () => Unit = () => {}
    //var onPressedBind: () => Unit = () => {}
    //var onHoveredBind: () => Unit  = () => {}
    //var onIdleBind : () => Unit = () => {}


    //TODO
    //var ToolTipText: String
    //var ToolTipWidget : UIComponent
    //var Visibility :
    final def draw(target: RenderTarget, states:RenderStates)=
        println("cool")

    def render(target: RenderTarget)=
        println("nothing")

    //def onPressed =
    //    this.onPressedBind()
    //def onCliked =
    //    this.isFocused = true
    //    this.onClickedBind()
    //def onHovered =
    //    this.onHoveredBind()
