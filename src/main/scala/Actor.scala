package actor


import sfml.system.*
import sfml.graphics.*
import sfml.Resource

import gamestate.*
import clickable.*
import manager.*
import event.Event

class OnDestroyed extends Event[Unit]()

/** Actor class
 * @constructor crate a new Actor
 */
class Actor extends Transformable with Drawable with Clickable  {
    var texture: Texture = TextureManager.get("sfml-logo.png")
    var live: Boolean = false

    var sprite: Sprite = Sprite(texture)
    
    var idleColor: Color = Color.White()
    var hoverColor: Color = Color.Green()
    var pressedColor: Color = Color.Red()

    this.onPressed = () => this.sprite.color= this.pressedColor
    this.onHovered = () => this.sprite.color= this.hoverColor
    this.onReleased = () => this.sprite.color= this.hoverColor
    this.onUnhovered = () => this.sprite.color= this.idleColor


    def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        target.draw(sprite, render_states)

    def moveActor(x: Float, y: Float) : Unit =
        this.position= Vector2(x,y)
        this.clickBounds = this.sprite.globalBounds
    
    def moveActor(pos: Vector2[Float]) : Unit =
        this.position= pos
        this.clickBounds = this.sprite.globalBounds


    // Load the textures save in textures
    def applyTexture() =
        sprite = Sprite(texture)
        sprite.origin = Vector2(sprite.globalBounds.width / 2, sprite.globalBounds.height / 2)
        this.live = true
        this.clickBounds = this.sprite.globalBounds

        print(clickBounds)

    var onDestroyed = OnDestroyed()

    def destroy() =
        // code pour supprimer l'actor
        GameState.delete_list += this

        this.moveConnection.disconnect()
        this.clickConnection.disconnect()

        this.onDestroyed(())

}
