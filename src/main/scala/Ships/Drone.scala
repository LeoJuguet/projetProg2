package ship

import ship.Ship

import event.KeyboardState
import clickable.States

import sfml.system.Vector2
import sfml.graphics.Rect
import scala.math.{min, max}

/**
 * extention of the basic ship class.
 * It has no additional features, but its click behavior is different.
*/
class Drone(
    teamID: Int,
    initialPosition: Vector2[Float]
)
extends Ship(teamID, initialPosition) {
    //for mor details ont the behavior of the drone, see the Actor class.
    if this.teamID == 0 then
        this.updateLeftClick = () =>
            if KeyboardState.ctrl then
                var firstPos = KeyboardState.mouseHoldPos
                var secondPos = KeyboardState.mouseView
                var topLeft = Vector2[Float](min(firstPos.x, secondPos.x), min(firstPos.y, secondPos.y))
                var bottomRight = Vector2[Float](max(firstPos.x, secondPos.x), max(firstPos.y, secondPos.y))
                var size = bottomRight - topLeft
                // TODO : draw the selection rectangle (to be done in the right file, not here)
                var selectionRect = Rect(topLeft.x, topLeft.y, size.x, size.y)

                if this.clickBounds.intersects(selectionRect) then
                    this.state = States.PRESSED
                    this.onPressed()
                else
                    if this.state == States.HOVER then
                        this.onUnhovered()
                        this.state = States.IDLE
            
            else
                if this.clickBounds.contains(KeyboardState.mouseView) then
                    this.state = States.PRESSED
                    this.onPressed()
                else
                    if this.state == States.HOVER then
                        this.onUnhovered()
                        this.state = States.IDLE
        
        this.updateRightPress = () => ()

        //this is necessary to ensure that selectioning targets do not release the actors selected by the player.
        var general_right_hold = this.updateRightHold

        this.updateRightHold = () =>
            if this.state != States.PRESSED then
                general_right_hold()
        
        this.updateRightClick = () =>
            if this.state == States.HOVER then
                this.state = States.IDLE
                this.onUnhovered()

}