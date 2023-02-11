package gui

import scala.collection.mutable.ArrayBuffer
import sfml.graphics.RenderTarget
import gui.UIComponent

class VerticalBox extends UIComponent:

    override def render(renderState: RenderTarget)=
      for child <- this.childs
      do
        child.render(renderState)