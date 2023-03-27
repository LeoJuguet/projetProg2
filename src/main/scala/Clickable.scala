package clickable

import sfml.system.*
import sfml.graphics.*
import sfml.window.Mouse.Button
import event.*


enum States:
    case IDLE, HOVER, PRESSED

// The clickable trait is used to add clickability to any object
// TODO : this file has to be updated to take into account the new input and event system
trait Clickable()
{
    var state = States.IDLE
    var clickBounds: Rect[Float] = Rect[Float]()

    var onPressed: () => Unit = () => ()
    var onHovered: () => Unit = () => ()
    var onReleased: () => Unit = () => ()
    var onUnhovered: () => Unit = () => ()
    
    def updateLeftClick(mousePos : Vector2[Float]) =
        if(this.clickBounds.contains(mousePos.x, mousePos.y)){
            this.state = States.PRESSED
            this.onPressed()
        }else{
            if (this.state == States.PRESSED)
                this.onReleased()
                this.state = States.IDLE
        }
    
    def updateRightClick(mousePos : Vector2[Float]) =
        if(this.clickBounds.contains(mousePos.x, mousePos.y)){
            if (this.state == States.PRESSED)
                this.onReleased()
                this.state = States.HOVER
        }else{
            if (this.state == States.PRESSED)
                this.onReleased()
                this.state = States.IDLE
        }

    var moveConnection = OnMouseMoved.connect((x, y) => {
        if this.clickBounds.contains(x, y) then
            if this.state == States.IDLE then
                this.state = States.HOVER
                this.onHovered()
        else
            if this.state == States.HOVER then
                this.state = States.IDLE
                this.onUnhovered()
    })
    
    var clickConnection = OnMouseButtonPressed.connect((button, x, y) => {
        if(button == Left)
            this.updateLeftClick(Vector2(x,y))
        else if(button == Right)
            this.updateRightClick(Vector2(x,y))
    })
    
    def isPressed: Boolean =
        return this.state == States.PRESSED
        
}
