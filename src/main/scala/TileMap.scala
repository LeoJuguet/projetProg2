package tilemap

import sfml.graphics.*
import sfml.system.*
import manager.TextureManager

class TileMap(var name : String, var i : Int, var j : Int) extends Transformable with Drawable {
    var sprite : Sprite = null
    var texture : Texture = TextureManager.get(name)

    def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        print(this.sprite)
        target.draw(this.sprite, render_states)

    def loadTexture() =
        if (sprite == null) then
            this.sprite = Sprite(texture)
            this.sprite.position = Vector2(j*512, i*512)
    
    def unloadTexture() =
        this.sprite = null
        this.texture = null
}