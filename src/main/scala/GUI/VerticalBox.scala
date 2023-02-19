package gui

import scala.collection.mutable.ArrayBuffer
import sfml.graphics.{RenderTarget, RenderStates}
import gui.UIComponent

class VerticalBox extends UIComponent:

    override def draw(target: RenderTarget, states: RenderStates)=
     // var transformStates = RenderStates(states.transform.combine(this.transform))
     // var transform = this.transform
      for child <- childs do {
        //child.transform= transform
        child.draw(target,states)
       // transform.translate(0.0f,child.globalBounds.height)
      }
