package actor


import sfml.system.*
import sfml.graphics.*
import sfml.Resource

import gamestate.*
import clickable.*
import manager.*

/** Actor class
 * @constructor crate a new Actor
 * @param gameState the game state who draw this actor
 */
class Actor(var gameState : GameState) extends Drawable with Clickable {
    var textures: String = "src/main/resources/sfml-logo.png"
    var texture: Texture = _
    var live: Boolean = false


    def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        target.draw(sprite, render_states)

    //def position : Vector2[Float] = this.sprite.position
    //def position_= : Float => Float => Unit =
    //    x => y => this.position=(x,y)

    //def position_= : Vector2[Float] => Unit =
    //    position => this.sprite.position=position

    def move : Float => Float => Unit =
        x => y => this.sprite.move(x,y)

    //def move : Vector2[Float] => Unit =
    //    offset => this.sprite.move(offset)

    //def transform =
    //    this.sprite.transform


    // Load the textures save in textures
    def loadTexture() =
        texture = TextureManager.get(textures)
        sprite = Sprite(texture)
        sprite.origin = Vector2(sprite.globalBounds.width / 2, sprite.globalBounds.height / 2)

        this.live = true

    def destroy() =
      // code pour supprimer l'actor
      gameState.actors_list -= this

}
