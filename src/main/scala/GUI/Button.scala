package gui

import sfml.graphics.*
import sfml.system.*

import gui.{UIComponent, Clickable, ShapeStyle, TextStyle}

class ButtonStyle(
    val shapeStyle: ShapeStyle = ShapeStyle(),
    val textStyle: TextStyle = TextStyle())
{
    def apply(shape: Shape,text: Text)=
        shapeStyle.apply(shape)
        textStyle.apply(text)
}


class Button extends UIComponent with Clickable:
    var shape: Shape = _
    var font: Font = Font()
    var text: Text = Text()

    var idleStyle: ButtonStyle = ButtonStyle(shapeStyle = ShapeStyle())
    var hoverStyle: ButtonStyle = ButtonStyle(shapeStyle = ShapeStyle(style = Style(outlineColor = Color.Red())))
    var pressedStyle: ButtonStyle = ButtonStyle(shapeStyle = ShapeStyle(style = Style(fillColor = Color.Green(),outlineColor = Color.Red())))

    def this(x: Float,y: Float, width: Float, height: Float,text: String) =
        this()
        this.shape = RectangleShape(width,height)
        this.position= Vector2(x,y)
        this.font.loadFromFile("src/main/resources/fonts/game_over.ttf")
        this.text.font_= (this.font)
        this.text.characterSize = 30
        this.text.string= text
        this.text.fillColor = Color.Yellow()
        this.applyStyle(idleStyle)
        this.globalBounds = this.text.globalBounds

    def applyStyle(buttonStyle: ButtonStyle)=
        buttonStyle.apply(this.shape, this.text)


    override def position: Vector2[Float]= this.shape.position

    override def position_=(position: Vector2[Float]) =
        this.shape.position = position
        this.text.position = position
        this.globalBounds = this.text.globalBounds

    override def draw(target: RenderTarget, states: RenderStates) =
        //val transformStates = RenderStates(states.transform.combine(this.transform))
        this.shape.draw(target,states)
        this.text.draw(target,states)


    override def onPressed()=
        this.applyStyle(this.pressedStyle)
        this.onPressedBind()

    override def onClicked()=
        this.onClickedBind()

    override def onHovered()=
        this.applyStyle(this.hoverStyle)
        this.onHoveredBind()

    override def onReleased()=
        this.applyStyle(this.hoverStyle)
        this.onReleasedBind()

    override def onUnhovered()=
        this.applyStyle(this.idleStyle)
        this.onUnhoveredBind()

end Button
