package resource

import actor.Actor
import sfml.system.Vector2
import manager.TextureManager
import scala.math.min

val rand = new scala.util.Random

class Resource(initialPosition: Vector2[Float]) extends Actor {
    // Map will be considered 32768*32768 units² for now
    this.texture = TextureManager.get("ore.png")
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
        print("killing resource\n")
        this.destroy()
        this.live = false
}

class Scrap(initialPosition: Vector2[Float]) extends Resource(initialPosition) {
    this.remainingQuantity = 300
    this.texture = TextureManager.get("ore.png")//TODO : find textures ! "scrap.png")
}

class Cooper(initialPosition: Vector2[Float]) extends Resource(initialPosition) {
    this.remainingQuantity = 100
    this.texture = TextureManager.get("copper.png")
}

class Iron(initialPosition: Vector2[Float]) extends Resource(initialPosition) {
    this.remainingQuantity = 50
    this.texture = TextureManager.get("iron.png")
}

class Uranium(initialPosition: Vector2[Float]) extends Resource(initialPosition) {
    this.remainingQuantity = 10
    this.texture = TextureManager.get("uranium.png")
}

class Ethereum(initialPosition: Vector2[Float]) extends Resource(initialPosition) {
    this.remainingQuantity = 1000
    this.texture = TextureManager.get("ethereum.png")
}
