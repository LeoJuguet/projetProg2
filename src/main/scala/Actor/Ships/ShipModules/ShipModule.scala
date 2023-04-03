package shipmodule

import sfml.graphics.{
    Sprite,
    RenderTarget,
    RenderStates
}
import sfml.system.Vector2

import actor.Actor
import ship.{Ship, Price}
import gamestate.GameState

//TODO : in all the types of modules, create precise variants.

//This is the base of what will be the ship modules in the final game (weapons, engines, etc.). It is an actor that can be placed on a ship.
class ShipModule(var parent : Ship) extends Actor {
    sprite = Sprite()
    
    //TODO : GameState function createModule(module, parent, position) -> onDestroyed, added to actor lists, etc.
    this.parent.onDestroyed.connect(Unit => this.destroy())

    var price: Price = _
    
    def updateModule() = {}
}
