package clickable

import sfml.system.*
import sfml.graphics.*


enum States:
    case IDLE, HOVER, PRESSED

// The clickable trait is used to add clickability to any object
// TODO : this file has to be updated to take into account the new input and event system
trait Clickable() extends Transformable
{
    var sprite: Sprite = new Sprite(Texture())
    var state = States.IDLE

    // Called whenever the button is click by the user
    var onClickedBind: () => Unit = () => {}

    // Called whenever the button is pressed by the user
    var onPressedBind: () => Unit = () => {}

    // Called whenever the button is released by the user
    var onReleasedBind: () => Unit = () => {}

    // Called whenever the button is hovered by the user
    var onHoveredBind: () => Unit  = () => {}

    // Called whenever the button was hovered by the user the frame before and it's not in this frame
    var onUnhoveredBind : () => Unit = () => {}

    var idleColor: Color = Color.White()
    var hoverColor: Color = Color.Green()
    var pressedColor: Color = Color.Red()

    def isPressed: Boolean =
        return this.state == States.PRESSED

    def updateClick(mousePos : Vector2[Float], leftMouse: Boolean, rightMouse : Boolean) =
        if(this.transform.transformRect(this.sprite.globalBounds).contains(mousePos.x, mousePos.y)){
            if(leftMouse){
                if(this.state != States.PRESSED) then
                    this.onClicked()
                    this.state = States.PRESSED
                else if(this.state == States.PRESSED) then
                    this.onReleased()
                    this.state = States.HOVER
            }else {
                if (rightMouse && this.state == States.PRESSED)
                    this.onReleased()
                    this.state = States.HOVER
                if this.state == States.IDLE then
                    this.state = States.HOVER
            }
        }else{
            if(this.state == States.HOVER)
                this.onUnhovered()
                this.state = States.IDLE

            if (leftMouse && this.state == States.PRESSED)
                this.onReleased()
                this.state = States.IDLE
        }

        state match
            case States.IDLE => {
                this.sprite.color= this.idleColor
            }
            case States.HOVER => {
                this.onHovered()
            }
            case States.PRESSED => {
                this.onPressed()
            }

    def onPressed()=
        this.sprite.color= this.pressedColor
        this.onPressedBind()

    def onClicked()=
        this.onClickedBind()

    def onHovered()=
        this.sprite.color= this.hoverColor
        this.onHoveredBind()

    def onReleased()=
        this.sprite.color= this.hoverColor
        this.onReleasedBind()

    def onUnhovered()=
        this.sprite.color= this.idleColor
        this.onUnhoveredBind()
}
