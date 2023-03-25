package base

import actor.*
import gamestate.*

class Base(gameState : GameState) extends Actor(gameState) {
    var maxHealth = 1000
    var health = 1000

    var team = 0

    var regenerationRate = 10

    var scrap = 0
    var cooper = 0
    var iron = 0
    var uranium = 0

    var ethereum = 0
    
    def takeDamage(damageTaken: Int) : Unit =
        this.health = this.health - damageTaken
        if this.health <= 0 then
            this.kill()
    
    def kill() : Unit =
        this.destroy()
        this.live = false

}