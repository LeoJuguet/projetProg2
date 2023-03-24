package character

import actor.*
import controller.*
import gamestate.*
import sfml.system.*

val rand = new scala.util.Random

class Resource(gameState : GameState, resourceId: Int, initialPosition: Vector2[Float]) extends Actor(gameState)
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
}

class Scrap(gameState : GameState, initialPosition: Vector2[Float]) extends Resource(gameState, 0, initialPosition)
{
    this.textures = "src/main/resources/scrap.png"
    this.loadTexture()
}

class Cooper(gameState : GameState, initialPosition: Vector2[Float]) extends Resource(gameState, 1, initialPosition)
{
    this.textures = "src/main/resources/cooper.png"
    this.loadTexture()
}

class Iron(gameState : GameState, initialPosition: Vector2[Float]) extends Resource(gameState, 2, initialPosition)
{
    this.textures = "src/main/resources/iron.png"
    this.loadTexture()
}

class Uranium(gameState : GameState, initialPosition: Vector2[Float]) extends Resource(gameState, 3, initialPosition)
{
    this.textures = "src/main/resources/uranium.png"
    this.loadTexture()
}

class Ethereum(gameState : GameState, initialPosition: Vector2[Float]) extends Resource(gameState, 4, initialPosition)
{
    this.textures = "src/main/resources/ethereum.png"
    this.loadTexture()
}