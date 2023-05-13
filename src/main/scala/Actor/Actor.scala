package actor

import scala.math.{min, max}

import sfml.system.Vector2
import sfml.graphics.*
import sfml.window.Keyboard.Key

import gamestate.GameState
import clickable.{Clickable, States}
import manager.TextureManager
import event.{Event, KeyboardState}
import controller.Camera

//TODO : add an animation state that will load a different texture for each frame of the animation
//       and an animation time
//       example of animation : small dust arround an asteroid
//                              explosion animation
//                              ship engine
//                              ship shield
//                              ...

class OnDestroyed extends Event[Unit]()

/** Actor class
 * @constructor crate a new Actor
 */
class Actor(textureFile : String = "sfml-logo.png")
extends Transformable with Drawable with Clickable {
    var texture: Texture = TextureManager.get(textureFile)
    var live: Boolean = false

    var sprite: Sprite = _
    applyTexture()
    //collision box are arbitrarily defined as a circle with radius equal to half the smallest dimension of the sprite
    var collisionRadius: Float = min(this.sprite.globalBounds.height, this.sprite.globalBounds.width) / 2
    
    var idleColor: Color = Color.White()
    var hoverColor: Color = Color.Green()
    var pressedColor: Color = Color.Red()
    var targetColor: Color = Color.Yellow()

    this.onPressed.connect(Unit => this.sprite.color= this.pressedColor)
    this.onTargeted.connect(Unit => this.sprite.color= this.targetColor)
    this.onReleased.connect(Unit => this.sprite.color= this.idleColor)
    this.onHovered.connect(Unit => this.sprite.color= this.hoverColor)
    this.onUnhovered.connect(Unit => this.sprite.color= this.idleColor)

    /**
     * behavior of controlled drones and other actors :
     * if ctrl is hold :
     * Whenever left click is first pressed, all the actors are released, and the position is saved.
     * Whenever left click is released, all the controlled actors in the rectangle are selected.
     * Whenever right click is pressed, all the targets are released, and the position is saved.
     * Whenever right click is released, all the uncontrolled actors in the rectangle are selected as targets.
     * 
     * if ctrl is not hold :
     * Whenever left click is first pressed, all the actors are released.
     * Whenever left click is released, the controlled actor under the mouse cursor is selected. (/!\ collisions between actors!)
     * Whenever right click is pressed all targets are released.
     * Whenever right click is released, the uncontrolled actor under the mouse cursor is selected as a target.
     *     if there are controlled actors selected, they target the target or the position (sleepy or)
     * 
     * in a selection of targets, actors will first attack any ennemy then mine any resource.
    */
    //NOTE : this implementation should allow to hold or release ctrl after starting the selection.

    //TODO : change the behavior so that while ctrl is hold, the selection stays (to select severa units not in rectangles)
    //       it is necessary for the end game but currently not a priority.

    this.updateLeftPress = () =>
        if this.state == States.PRESSED || this.state == States.TARGET then
            this.onReleased(())
            this.state = States.IDLE
    
    this.updateLeftHold = () =>
        if KeyboardState.is_Press(Key.KeyLControl) then
            //test the intesection of the sprite and the selection rectangle
            var firstPos = KeyboardState.mouseHoldPos
            var secondPos = KeyboardState.mouseView
            var topLeft = Vector2[Float](min(firstPos.x, secondPos.x), min(firstPos.y, secondPos.y))
            var bottomRight = Vector2[Float](max(firstPos.x, secondPos.x), max(firstPos.y, secondPos.y))
            var size = bottomRight - topLeft
            // TODO : draw the selection rectangle (to be done in the right file, not here)
            var selectionRect = Rect(topLeft.x, topLeft.y, size.x, size.y)

            if this.clickBounds.intersects(selectionRect) then
                if this.state == States.IDLE then
                    this.state = States.HOVER
                    this.onHovered(())
            else
                if this.state == States.HOVER then
                    this.onUnhovered(())
                    this.state = States.IDLE
        
        else
            if this.clickBounds.contains(KeyboardState.mouseView) then
                if this.state == States.IDLE then
                    this.state = States.HOVER
                    this.onHovered(())
            else
                if this.state == States.HOVER then
                    this.onUnhovered(())
                    this.state = States.IDLE


    this.updateLeftClick = () =>
        if this.state == States.HOVER then
            this.state = States.IDLE
            this.onUnhovered(())
        
        if this.clickBounds.contains(KeyboardState.mouseView) && KeyboardState.is_Press(Key.KeyLAlt) then
            Camera.updateBind(this)

    this.updateRightPress = updateLeftPress
    
    this.updateRightHold = updateLeftHold

    this.updateRightClick = () =>
        if KeyboardState.is_Press(Key.KeyLControl) then
            var firstPos = KeyboardState.mouseHoldPos
            var secondPos = KeyboardState.mouseView
            var topLeft = Vector2[Float](min(firstPos.x, secondPos.x), min(firstPos.y, secondPos.y))
            var bottomRight = Vector2[Float](max(firstPos.x, secondPos.x), max(firstPos.y, secondPos.y))
            var size = bottomRight - topLeft
            var selectionRect = Rect(topLeft.x, topLeft.y, size.x, size.y)

            if this.clickBounds.intersects(selectionRect) then
                this.state = States.TARGET
                this.onTargeted(())
            else
                if this.state == States.HOVER then
                    this.onUnhovered(())
                    this.state = States.IDLE
        
        else
            if this.clickBounds.contains(KeyboardState.mouseView) then
                this.state = States.TARGET
                this.onTargeted(())
            else
                if this.state == States.HOVER then
                    this.onUnhovered(())
                    this.state = States.IDLE


    def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        target.draw(sprite, render_states)

    def moveActor(x: Float, y: Float) : Unit =
        this.position= Vector2(x,y)
        this.clickBounds = this.transform.transformRect(this.sprite.globalBounds)
    
    def moveActor(pos: Vector2[Float]) : Unit =
        this.moveActor(pos.x, pos.y)


    // Load the textures save in textures
    def applyTexture() =
        sprite = Sprite(texture)
        sprite.origin = Vector2(sprite.globalBounds.width / 2, sprite.globalBounds.height / 2)
        this.live = true
        this.clickBounds = this.sprite.globalBounds

    var onDestroyed = OnDestroyed()

    def destroy() =
        // code pour supprimer l'actor
        GameState.delete_list += this
        this.live = false

        this.moveConnection.disconnect()
        this.releaseConnection.disconnect()
        this.holdConnection.disconnect()
        this.clickConnection.disconnect()

        this.onDestroyed(())

}
