package tilemap

import sfml.graphics.*
import sfml.system.*

class TileMap(var name : String, var i : Int, var j : Int) extends Transformable with Drawable {
    var sprite : Sprite = null
    var texture : Texture = null

    def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        target.draw(sprite, render_states)

    def loadTexture() =
        if (sprite == null) then
            texture = Texture()
            texture.loadFromFile(name)
            sprite = Sprite(texture)
            sprite.position = Vector2(j*512, i*512)
    
    def unloadTexture() =
        sprite = null
        texture = null
}