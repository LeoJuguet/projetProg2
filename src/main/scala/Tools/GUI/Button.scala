package gui

import sfml.graphics.*
import sfml.system.*

import gui.{UIComponent, Clickable}
import gui.{Style,ShapeStyle, TextStyle, ClickStates}

import manager.*

/** A class for define a style for a special state of a button
 *
 * @constructor create a new style for a button state
 * @param shapeStyle the style of the Button
 * @param textStyle the style of the text on the Button
 */
class ButtonStyleState(
    var shapeStyle: ShapeStyle = ShapeStyle(),
    var textStyle: TextStyle = TextStyle())
{
    def apply(shape: Shape,text: Text)=
        shapeStyle.apply(shape)
        textStyle.apply(text)
}

/** A class for define the style of a Button
 *
 *  @constructor create a new style for a button
 *  @param idleStyle the style of the button when it's in idle
 *  @param hoverStyle the style of the button when it's hover
 *  @param pressedStyle the style of the button when it's pressed
 */
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

/** A button is an UIComponent of our gui
 *
 *  @constructor create a new buton with somes parameters
 *  @param pos the pos of the button, default value = Vector2(0f,0f)
 *  @tparam width the width of the button, default value = 100
 *  @param height the height of the button, default value = 50
 *  @param string the text show on top of the button, default value = ""
 *  @param buttonStyle the style of the button, the default value is the default value of ButtonStyle
 */
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
    var font: Font = FontManager.get("game_over.ttf")
    var text: Text = Text()
    this.text.string = this.string
    this.text.font_= (this.font)
    this.text.characterSize = 30
    this.applyStyle()
    this.position= Vector2(x,y)


    def applyStyle()=
        this.buttonStyle.apply(this.shape, this.text, this.clickState)


    override def position: Vector2[Float]= this.shape.position

    override def position_=(position: Vector2[Float]) =
        this.shape.position = position
        this.text.position = position
        this.globalBounds = Rect(this.shape.position.x, this.shape.position.y,
        this.width, this.height)

    override def clickBounds = this.shape.globalBounds

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
