package ship

import sfml.system.Vector2

import actor.Actor
import container.Container
import manager.TextureManager
import sfml.graphics.Sprite

//The base is the main building of the player. It is used as end goal for the player to win the game. It can be used to store ressources but not building units.
class Base(teamID : Int, initialPosition: Vector2[Float]) extends Actor with Container {
    var maxHealth = 1000
    var health = 1000

    var regenerationRate = 10

    var team = teamID
    this.maxLoad = 1000

    this.texture = TextureManager.get("base.png")
    this.applyTexture()
    
    this.moveActor(initialPosition)
    
    def takeDamage(damageTaken: Int) : Unit =
        this.health = this.health - damageTaken
        if this.health <= 0 then
            this.kill()
    
    def kill() : Unit =
        this.destroy()
        this.live = false

}
