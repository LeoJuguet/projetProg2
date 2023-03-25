package actor


import sfml.system.*
import sfml.graphics.*
import sfml.Resource

import gamestate.*
import clickable.*
import controller.*
import manager.*

/** Actor class
 * @constructor crate a new Actor
 */
class Actor( controller : Controller) extends Transformable with Drawable with Clickable
{
    var texture: Texture = TextureManager.get("sfml-logo.png")
    var live: Boolean = false


    def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        target.draw(sprite, render_states)

    def move : Float => Float => Unit =
        x => y => this.sprite.move(x,y)


    // Load the textures save in textures
    def applyTexture() =
        sprite = Sprite(texture)
        sprite.origin = Vector2(sprite.globalBounds.width / 2, sprite.globalBounds.height / 2)
        this.live = true


    def destroy() =
      // code pour supprimer l'actor
      GameState.delete_list += this

}
