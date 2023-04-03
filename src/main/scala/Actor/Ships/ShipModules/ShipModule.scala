package shipmodule

import sfml.graphics.{
    Sprite,
    RenderTarget,
    RenderStates
}
import sfml.system.Vector2

import actor.Actor
import ship.{Ship, Price}

//This is the base of what will be the ship modules in the final game (weapons, engines, etc.). It is an actor that can be placed on a ship.
class ShipModule(var parent : Ship) extends Actor {
    sprite = Sprite()
    
    this.parent.onDestroyed.connect(Unit => this.destroy())

    var price: Price = _

    override def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        target.draw(sprite, render_states)
}
