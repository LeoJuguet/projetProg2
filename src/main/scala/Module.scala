package module

import actor.*
import gamestate.*

import sfml.system.*

class Module(gameState : GameState) extends Actor_Clickable(gameState)
{
    var relative_position: Vector2[Float] = Vector2(0.0f, 0.0f)

}