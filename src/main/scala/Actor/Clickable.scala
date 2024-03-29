package clickable

import sfml.graphics.Rect
import sfml.window.Mouse

import event.{
    Event,
    KeyboardState,
    OnMouseMoved,
    OnMouseButtonPressed,
    OnMouseButtonReleased,
    OnMouseButtonHold
}

class OnPressed extends Event[Unit]
class OnTargeted extends Event[Unit]
class OnReleased extends Event[Unit]
class OnHovered extends Event[Unit]
class OnUnhovered extends Event[Unit]

enum States:
    case IDLE, HOVER, PRESSED, TARGET

// The clickable trait is used to add clickability to any object
trait Clickable {
    var state = States.IDLE
    var clickBounds: Rect[Float] = Rect[Float]()

    //actions to do when a clickable is [pressed, ...]
    var onPressed = OnPressed()
    var onTargeted = OnTargeted()
    var onReleased = OnReleased()
    var onHovered = OnHovered()
    var onUnhovered = OnUnhovered()

    //actions to do when the mouse is pressed, hold or released.
    var updateLeftPress: () => Unit = () => ()
    var updateLeftHold: () => Unit = () => ()
    var updateLeftClick: () => Unit = () => ()

    var updateRightPress: () => Unit = () => ()
    var updateRightHold: () => Unit = () => ()
    var updateRightClick: () => Unit = () => ()

    var moveConnection = OnMouseMoved.connect((x, y) => {
        if this.clickBounds.contains(KeyboardState.mouseView) then
            if this.state == States.IDLE then
                this.state = States.HOVER
                this.onHovered(())
        else
            if this.state == States.HOVER then
                this.state = States.IDLE
                this.onUnhovered(())
    })

    var releaseConnection = OnMouseButtonReleased.connect((button, x, y) => {
        if(button == Mouse.Button.Left)
            this.updateLeftClick()
        else if(button == Mouse.Button.Right)
            this.updateRightClick()
    })
    var holdConnection = OnMouseButtonHold.connect((button, x, y) => {
        if(button == Mouse.Button.Left)
            this.updateLeftHold()
        else if(button == Mouse.Button.Right)
            this.updateRightHold()
    })
    var clickConnection = OnMouseButtonPressed.connect((button, x, y) => {
        if(button == Mouse.Button.Left)
            this.updateLeftPress()
        else if(button == Mouse.Button.Right)
            this.updateRightPress()
    })
    
    def isPressed: Boolean =
        return this.state == States.PRESSED
        
}
