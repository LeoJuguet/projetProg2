package actor

import sfml.system.*
import sfml.graphics.*
import sfml.Resource

import gamestate.*
import clickable.*

/** Actor class
 * @constructor crate a new Actor
 * @param gameState the game state who draw this actor
 */
class Actor(var gameState :GameState) extends Transformable with Drawable with Resource with Clickable
{
    var textures: String = "src/main/resources/sfml-logo.png"

    def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(states.blendMode, this.transform)
        target.draw(sprite, render_states)

    override def close() =
      sprite.close()

    // Load the textures save in textures
    def loadTexture() =
        val texture = Texture()
        texture.loadFromFile(textures)
        sprite = Sprite(texture)
        sprite.origin = Vector2(sprite.globalBounds.width / 2, sprite.globalBounds.height / 2)

    def destroy() =
      // code pour supprimer l'actor
      gameState.actors_list -= this
      this.close
}