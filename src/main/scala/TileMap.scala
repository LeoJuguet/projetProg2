package tilemap

import sfml.graphics.*
import sfml.system.*
import manager.TextureManager

//This structure is used to store the information about a portion of the map that is loaded in memory.
//It allows to unload the textures when they are not needed anymore, and thus not to overload the memory and display them when they are needed.
//TODO : when the texture manager will have the free function, use it to unload the textures instead of setting them to null.
class TileMap(var name : String, var i : Int, var j : Int) extends Transformable with Drawable {
    var sprite : Sprite = null
    var texture : Texture = TextureManager.get(name)

    def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        target.draw(this.sprite, render_states)

    def loadTexture() =
        if (sprite == null) then
            this.sprite = Sprite(texture)
            this.sprite.position = Vector2(j*512, i*512)
    
    def unloadTexture() =
        this.sprite = null
        this.texture = null
}
