package gui

import sfml.graphics.*
import sfml.system.*

import gui.UIComponent

enum Button_states:
    case BTN_IDLE, BTN_HOVER, BTN_PRESSED


class Button extends UIComponent :
    var buttonState = Button_states.BTN_IDLE
    var shape: Sprite = new Sprite(Texture())
    var font: Font = Font()
    var text: Text = Text()
/** Called whenever the button is click by the user
 *
 */
    var onClickedBind: () => Unit = () => {}
/** Called whenever the button is pressed by the user
 *
 */
    var onPressedBind: () => Unit = () => {}
/** Called whenever the button is released by the user
 */
    var onReleasedBind: () => Unit = () => {}
/** Called whenever the button is hovered by the user */
    var onHoveredBind: () => Unit  = () => {}
/** Called whenever the button was hovered by the user the frame before and it's
 *  not in this frame
 */
    var onUnhoveredBind : () => Unit = () => {}


    var idleColor: Color = Color.White()
    var hoverColor: Color = Color.Green()
    var pressedColor: Color = Color.Red()

    def this(x: Float,y: Float, width: Float, height: Float,text: String) =
        this()
        val texture = Texture()
        texture.loadFromFile("src/main/resources/sfml-logo.png")
        this.shape = Sprite(texture)
        this.shape.position= Vector2(x,y)
        this.font.loadFromFile("src/main/resources/fonts/game_over.ttf")
        this.text.font_= (this.font)
        this.text.characterSize = 30
        this.text.string= text
        this.text.color = Color.Red()
        this.text.position=Vector2(x,y)

    def isPressed: Boolean =
        return this.buttonState == Button_states.BTN_PRESSED

    override def render(target: RenderTarget) =
        target.draw(this.shape)
        target.draw(this.text)


    def update(mousePos : Vector2[Float], leftMouse: Boolean) =
        if(this.text.globalBounds.contains(mousePos.x, mousePos.y)){
            if(leftMouse){
                if(this.buttonState != Button_states.BTN_PRESSED) then
                    this.onClicked()
                this.buttonState = Button_states.BTN_PRESSED
            }else{
                if(this.buttonState == Button_states.BTN_PRESSED)
                    this.onReleased()
                this.buttonState = Button_states.BTN_HOVER
            }
        }else{
            if(this.buttonState != Button_states.BTN_IDLE)
                this.onUnhovered()
            this.buttonState = Button_states.BTN_IDLE
        }

        buttonState match
            case Button_states.BTN_IDLE => {
                this.text.color= this.idleColor
            }
            case Button_states.BTN_HOVER => {
                this.onHovered()
            }
            case Button_states.BTN_PRESSED => {
                this.onPressed()
            }

    def onPressed()=
        this.text.color= this.pressedColor
        this.onPressedBind()

    def onClicked()=
        this.onClickedBind()

    def onHovered()=
        this.text.color= this.hoverColor
        this.onHoveredBind()

    def onReleased()=
        this.text.color= this.hoverColor
        this.onReleasedBind()

    def onUnhovered()=
        this.text.color= this.idleColor
        this.onUnhoveredBind()

end Button
