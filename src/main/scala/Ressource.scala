package character

import actor.*
import gamestate.*
import sfml.system.*
import manager.TextureManager

val rand = new scala.util.Random

class Resource(initialPosition: Vector2[Float]) extends Actor
{
    // Map will be considered 32768*32768 units² for now
    this.position = initialPosition;
    var remainingQuantity = 100;

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

class Scrap(initialPosition: Vector2[Float]) extends Resource(initialPosition)
{
    this.remainingQuantity = 300
    this.texture = TextureManager.get("scrap.png")
}

class Cooper(initialPosition: Vector2[Float]) extends Resource(initialPosition)
{
    this.remainingQuantity = 100
    this.texture = TextureManager.get("cooper.png")
}

class Iron(initialPosition: Vector2[Float]) extends Resource(initialPosition)
{
    this.remainingQuantity = 50
    this.texture = TextureManager.get("iron.png")
}

class Uranium(initialPosition: Vector2[Float]) extends Resource(initialPosition)
{
    this.remainingQuantity = 10
    this.texture = TextureManager.get("uranium.png")
}

class Ethereum(initialPosition: Vector2[Float]) extends Resource(initialPosition)
{
    this.remainingQuantity = 1000
    this.texture = TextureManager.get("ethereum.png")
}
