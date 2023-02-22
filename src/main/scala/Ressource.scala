package character

import actor.*
import gamestate.*
import sfml.system.*

val rand = new scala.util.Random

class Resources(gameState: GameState, resourceId: Int, initialPosition: Vector2[Float]) extends Actor(gameState):
    // Map will be considered 32768*32768 units² for now
    this.position = initialPosition;
    var remainingQuantity = 500;
    resourceId match {
        case 0 => remainingQuantity = 300;
        case _ => () // No specific resource type implemented yet. TODO: add other resource cases when implemented
    }

    def remainingQuantity_(newQuantity: Int) =
        remainingQuantity = newQuantity

    def mined() =
        var newQuantity = remainingQuantity - 10;
        remainingQuantity_(newQuantity);
        if newQuantity == 0 then {
            this.destroy()
        }
    