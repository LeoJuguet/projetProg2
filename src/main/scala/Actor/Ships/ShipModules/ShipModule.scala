package shipmodule

import sfml.graphics.{
    Sprite,
    RenderTarget,
    RenderStates
}
import sfml.system.Vector2

import actor.Actor
import ship.Ship

class PriceStruct(
    var scrap: Int = 0,
    var copper: Int = 0,
    var iron: Int = 0,
    var uranium: Int = 0,
    var ethereum: Int = 0,
){}

//This is the base of what will be the ship modules in the final game (weapons, engines, etc.). It is an actor that can be placed on a ship.
class ShipModule extends Actor {
    sprite = Sprite()
    var parent: Ship = _

    var price: PriceStruct = _

    override def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        target.draw(sprite, render_states)
}
