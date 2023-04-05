package gui

import sfml.graphics.*
import sfml.system.*

import gui.UIComponent
import manager.FontManager

class Label(
  var string : String = "",
  var font : Font = FontManager.get("game_over.ttf"),
  var characterSize : Int = 20
) extends UIComponent{

  var text = Text(string, font, characterSize)
  text.color = Color.Red()
  globalBounds = text.globalBounds


  def setText(string: String)=
    this.string = string
    this.text.string = this.string
    globalBounds = text.globalBounds

  override def close() =
    this.text.close()

  override def position: Vector2[Float]= this.text.position

  override def position_=(position: Vector2[Float]) =
    this.text.position = position
    this.globalBounds = text.globalBounds

  override def draw(target: RenderTarget, states: RenderStates) =
    val transformStates = RenderStates(states.transform.combine(this.transform))
    text.draw(target, transformStates)
}
