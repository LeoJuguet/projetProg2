package module

import actor.*
import gamestate.*
import controller.*

import sfml.system.*

class Module(gameState : GameState, controller : Controller) extends Actor(gameState, controller)
{
    var relative_position: Vector2[Float] = Vector2(0.0f, 0.0f)

}