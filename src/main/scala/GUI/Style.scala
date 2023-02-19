package gui

import sfml.graphics.{Color, Texture, Shape, Text}

class Style (
  var fillColor: Color = Color.White(),
  var outlineColor: Color = Color.Black(),
  var outlineThickness: Float = 1f,
){}

class ShapeStyle (
  var style: Style = Style(),
  var texture: Texture = Texture()
){
  def apply(shape: Shape)=
    shape.fillColor = this.style.fillColor
    shape.outlineColor = this.style.outlineColor
    shape.outlineThickness = this.style.outlineThickness
    shape.texture = this.texture

}


class TextStyle (
  var style: Style = Style(fillColor = Color.Black(), outlineThickness = 0f),
  var letterSpacing: Float = 1f,
){
  def apply(text: Text)=
    text.color = this.style.fillColor
    text.outlineColor = this.style.outlineColor
    text.outlineThickness = this.style.outlineThickness
}
