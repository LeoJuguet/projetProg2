package actor

import sfml.graphics.*
import sfml.Resource
import sfml.window.*

import gamestate.*

/** Actor
 *
 * @constructor crate a new Actor
 * @param gameState the game state who draw this actor
 */
class Actor(var gameState :GameState) extends Transformable with Drawable with Resource :
    var textures: String = "src/main/resources/sfml-logo.png"
    var sprite: Transformable with Drawable with Resource = new Sprite(Texture())

    def draw(target: RenderTarget, states: RenderStates)=
        val render_states = RenderStates(states.blendMode,this.transform);
        target.draw(sprite,render_states)

    override def close()=
      sprite.close()

/** Load the textures save in textures
 */
    def loadTexture() =
        val texture = Texture()
        texture.loadFromFile(textures)
        sprite = Sprite(texture)

    def destroy() =
      // code pour supprimer l'actor
      gameState.actors_list -= this
      this.close

end Actor
