package character

import actor.*
import gamestate.*
import sfml.system.*
import manager.TextureManager

val rand = new scala.util.Random

class Resource(resourceId: Int, initialPosition: Vector2[Float]) extends Actor
{
    // Map will be considered 32768*32768 units² for now
    this.position = initialPosition;
    var remainingQuantity = 100;
    resourceId match {
        case 0 => remainingQuantity = 300
        case 1 => remainingQuantity = 100
        case 2 => remainingQuantity = 50
        case 3 => remainingQuantity = 10
        case 4 => remainingQuantity = 1000
    }

    GameState.actors_list += this

    def mined(damage : Int) : Unit =
        this.remainingQuantity -= damage
        if this.remainingQuantity <= 0 then {
            this.kill()
        }
    
    def kill() : Unit =
        print("killing ressource\n")
        this.destroy()
        this.live = false
}

class Scrap(initialPosition: Vector2[Float]) extends Resource(0, initialPosition)
{
    this.texture = TextureManager.get("scrap.png")
}

class Cooper(initialPosition: Vector2[Float]) extends Resource(1, initialPosition)
{
    this.texture = TextureManager.get("cooper.png")
}

class Iron(initialPosition: Vector2[Float]) extends Resource(2, initialPosition)
{
    this.texture = TextureManager.get("iron.png")
}

class Uranium(initialPosition: Vector2[Float]) extends Resource(3, initialPosition)
{
    this.texture = TextureManager.get("uranium.png")
}

class Ethereum(initialPosition: Vector2[Float]) extends Resource(4, initialPosition)
{
    this.texture = TextureManager.get("ethereum.png")
}
