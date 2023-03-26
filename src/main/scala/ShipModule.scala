package shipmodule

import actor.*
import gamestate.*
import controller.*

import sfml.system.*
import character.Ship
import sfml.graphics.Sprite
import sfml.graphics.RenderTarget
import sfml.graphics.RenderStates

class PriceStruct(
    var scrap: Int = 0,
    var cooper: Int = 0,
    var iron: Int = 0,
    var uranium: Int = 0,
    var ethereum: Int = 0,
){}

class ShipModule extends Actor
{
    sprite = Sprite()
    var parent: Ship = _

    var price: PriceStruct = _

    override def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        target.draw(sprite, render_states)
}
