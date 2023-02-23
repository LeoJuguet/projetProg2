package character

import actor.*
import gamestate.*
import sfml.system.*

val rand = new scala.util.Random

class Resource(gameState: GameState, resourceId: Int, initialPosition: Vector2[Float]) extends Actor(gameState):
    // Map will be considered 32768*32768 units² for now
    this.position = initialPosition;
    var remainingQuantity = 500;
    resourceId match {
        case 0 => remainingQuantity = 300;
        case _ => () // No specific resource type implemented yet. TODO: add other resource cases when implemented
    }

    gameState.actors_list += this

    def mined(damage : Int) : Unit =
        this.remainingQuantity -= damage
        if this.remainingQuantity <= 0 then {
            this.kill()
        }
    
    def kill() : Unit =
        print("killing ressource\n")
        this.destroy()
        this.live = false