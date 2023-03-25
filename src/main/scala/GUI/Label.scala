package gui

import sfml.graphics.*

import gui.UIComponent
import manager.FontManager

class Label(
  var string : String = "",
  var font : Font = FontManager.get("game_over.ttf"),
  var characterSize : Int = 20
) extends UIComponent{

  var text = Text(string, font, characterSize)

  def setText(string: String)=
    this.string = string
    this.text.string = this.string

  override def close() =
    this.text.close()

  override def draw(target: RenderTarget, states: RenderStates) =
    val transformStates = RenderStates(states.transform.combine(this.transform))
    text.draw(target, transformStates)
}
