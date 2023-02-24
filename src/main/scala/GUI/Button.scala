package gui

import sfml.graphics.*
import sfml.system.*

import gui.{UIComponent, Clickable}
import gui.{Style,ShapeStyle, TextStyle, ClickStates}


class ButtonStyleState(
    val shapeStyle: ShapeStyle = ShapeStyle(),
    val textStyle: TextStyle = TextStyle())
{
    def apply(shape: Shape,text: Text)=
        shapeStyle.apply(shape)
        textStyle.apply(text)
}

class ButtonStyle(
    var idleStyle: ButtonStyleState = ButtonStyleState(shapeStyle = ShapeStyle()),
    var hoverStyle: ButtonStyleState = ButtonStyleState(shapeStyle = ShapeStyle(style = Style(outlineColor = Color.Red()))),
    var pressedStyle: ButtonStyleState = ButtonStyleState(shapeStyle = ShapeStyle(style = Style(fillColor = Color.Green(),outlineColor = Color.Red()))),
){
    def apply(shape: Shape,text: Text, state: ClickStates)=
        state match {
            case ClickStates.CLICK_IDLE => idleStyle.apply(shape,text)
            case ClickStates.CLICK_HOVER => hoverStyle.apply(shape,text)
            case ClickStates.CLICK_PRESSED => pressedStyle.apply(shape,text)
        }
}


class Button(
    var x : Float =0f,
    var y : Float = 0f,
    width: Float,
    height: Float,
    var string: String = "",
    var buttonStyle: ButtonStyle = ButtonStyle()
)
extends UIComponent(width,height) with Clickable{

    var shape: Shape = RectangleShape(this.width,this.height)
    var font: Font = Font()
    var text: Text = Text()
    this.text.string = this.string
    this.font.loadFromFile("src/main/resources/fonts/game_over.ttf")
    this.text.font_= (this.font)
    this.text.characterSize = 30
    this.applyStyle()
    this.position= Vector2(x,y)

    //def this(x: Float,y: Float, width: Float, height: Float,text: String) =
    //    this(x = x, y = y, width = width, height = height, string = text)

    def applyStyle()=
        this.buttonStyle.apply(this.shape, this.text, this.clickState)


    override def position: Vector2[Float]= this.shape.position

    override def position_=(position: Vector2[Float]) =
        this.shape.position = position
        this.text.position = position
        this.globalBounds = Rect(this.shape.position.x, this.shape.position.y,
        this.width, this.height)

    override def draw(target: RenderTarget, states: RenderStates) =
        //val transformStates = RenderStates(states.transform.combine(this.transform))
        this.shape.draw(target,states)
        this.text.draw(target,states)


    override def onPressed()=
        this.applyStyle()
        this.onPressedBind()

    override def onClicked()=
        this.onClickedBind()

    override def onHovered()=
        this.applyStyle()
        this.onHoveredBind()

    override def onReleased()=
        this.applyStyle()
        this.onReleasedBind()

    override def onUnhovered()=
        this.applyStyle()
        this.onUnhoveredBind()

}
