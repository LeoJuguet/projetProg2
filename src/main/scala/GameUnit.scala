package character

import actor.*
import gamestate.*
import sfml.system.*
import sfml.graphics.*

import scala.math.*

abstract class GameUnit(gameState: GameState) extends Actor(gameState)
{
    var speed: Vector2[Float]
    var maxSpeed: Float
    var maxHealth: Int
    var health: Int
    var regenerationRate: Int
    var attackDamage: Int
    var targetPosition: Vector2[Float]

    def kill() : Unit =
        print("killing game unit\n")
        this.destroy()
        this.live = false

    def health_(newHealth: Int) : Unit =
        if newHealth > this.maxHealth then
            this.health = this.maxHealth
        else if newHealth <= 0 then
            this.kill()
        else
            this.health = newHealth
    
    def regenerate() : Unit =
        this.health = this.maxHealth.min(this.health + regenerationRate)

    def takeDamage(damageTaken: Int) : Unit =
        this.health = this.health - damageTaken
        if this.health <= 0 then
            //TODO : explosion animation
            //Maybe add a destroyed list buffer in the gamestate
            this.kill()

    def moveTo(target: Vector2[Float]) : Unit =
        this.targetPosition = target

    def moveUnit() : Unit =
        val centered_target = Vector2(this.targetPosition.x - this.position.x,
                                      this.targetPosition.y - this.position.y)
        val distance = norm(centered_target)

        if distance < 1.0f then
            speed = Vector2(0.0f, 0.0f)
        else
            val normalized = Vector2(centered_target.x / distance,
                                     centered_target.y / distance)
            this.speed = Vector2(0.95f * speed.x + 0.05f * normalized.x,
                                 0.95f * speed.y + 0.05f * normalized.y)
            var angle = atan2(speed.y, speed.x)

            this.position=(this.position.x + this.speed.x, this.position.y + this.speed.y)
            this.sprite.rotation = (angle * 180 / Pi).toFloat
}
