package clickable

import sfml.system.*
import sfml.graphics.*
import sfml.window.Mouse
import event.*
import event.KeyboardState.mouseView


//TODO : add a color and its cases for the TARGET state
enum States:
    case IDLE, HOVER, PRESSED, TARGET

// The clickable trait is used to add clickability to any object
// TODO : this file has to be updated to take into account the new input and event system
trait Clickable()
{
    var state = States.IDLE
    var clickBounds: Rect[Float] = Rect[Float]()

    //actions to do when a clickable is [pressed, ...]
    var onPressed: () => Unit = () => ()
    var onHovered: () => Unit = () => ()
    var onReleased: () => Unit = () => ()
    var onUnhovered: () => Unit = () => ()

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
                this.onHovered()
        else
            if this.state == States.HOVER then
                this.state = States.IDLE
                this.onUnhovered()
    })

    var releaseConnection = OnMouseButtonReleased.connect((button, x, y) => {
        if(button == Mouse.Button.Left)
            this.updateLeftClick()
        else if(button == Mouse.Button.Right)
            this.updateRightClick()
    })
    var clickConnection = OnMouseButtonPressed.connect((button, x, y) => {
        if(button == Mouse.Button.Left)
            if KeyboardState.holdLeft then
                this.updateLeftHold()
            else
                this.updateLeftPress()
        else if(button == Mouse.Button.Right)
            if KeyboardState.holdRight then
                this.updateRightHold()
            else
                this.updateRightPress()
    })
    
    def isPressed: Boolean =
        return this.state == States.PRESSED
        
}
