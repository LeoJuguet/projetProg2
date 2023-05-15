package asteroid

import actor.Actor
import sfml.system.Vector2
import manager.TextureManager
import scala.math.min

val rand = new scala.util.Random

class Asteroid(
    initialPosition: Vector2[Float],
    textureFile : String = "asteroid.png",
) extends Actor {
    // Map will be considered 32768*32768 units² for now
    this.texture = TextureManager.get(textureFile)
    this.applyTexture()

    this.moveActor(initialPosition)
    var remainingQuantity = 100

    def mined(damage : Int) : Int =
        var lost = min(damage, this.remainingQuantity)
        this.remainingQuantity -= lost
        if this.remainingQuantity <= 0 then
            this.kill()
        lost
    
    def kill() : Unit =
        this.destroy()
        this.live = false
}

class Scrap(initialPosition: Vector2[Float]) extends Asteroid(initialPosition) {
    this.remainingQuantity = 300
}

class Cooper(initialPosition: Vector2[Float]) extends Asteroid(initialPosition, "Textures/Ore/Copper/Average_density/copper_average_density_00.png") {
    this.remainingQuantity = 100
}

class Iron(initialPosition: Vector2[Float]) extends Asteroid(initialPosition, "Textures/Ore/Iron/Average_density/iron_average_density_00.png") {
    this.remainingQuantity = 50
}

class Uranium(initialPosition: Vector2[Float]) extends Asteroid(initialPosition, "Textures/Ore/Uranium/Average_density/uranium_average_density_00.png") {
    this.remainingQuantity = 10
}

class Ethereum(initialPosition: Vector2[Float]) extends Asteroid(initialPosition, "Textures/Ore/Ethereum/Average_density/ethereum_average_density_00.png") {
    this.remainingQuantity = 1000
}
