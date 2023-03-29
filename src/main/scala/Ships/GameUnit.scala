package ship

import actor.*
import gamestate.*
import sfml.system.*
import sfml.graphics.*

import scala.math.*

//This class is used to represent a game unit. It is the base class for all game units.
//It will only be inherited by the ships, but it is a general class that could be used for other game units if the game is further meant to be expanded.
abstract class GameUnit extends Actor
{
    var speed: Vector2[Float]
    var maxSpeed: Float
    var maxHealth: Int
    var _health: Int
    var regenerationRate: Int
    var attackDamage: Int

    def kill() : Unit =
        print("killing game unit\n")
        this.destroy()
        this.live = false

    def health = _health

    def health_=(newHealth: Int) : Unit =
        if newHealth > this.maxHealth then
            this._health = this.maxHealth
        else if newHealth <= 0 then
            this.kill()
        else
            this._health = newHealth
    
    def regenerate() : Unit =
        this.health = this.maxHealth.min(this.health + regenerationRate)

    def takeDamage(damageTaken: Int) : Unit =
        this.health = this.health - damageTaken
        if this.health <= 0 then
            //TODO : explosion animation
            //Maybe add a destroyed list buffer in the gamestate
            this.kill()

    def moveUnit(targetPosition : Vector2[Float]) : Boolean =
        val centered_target = Vector2(targetPosition.x - this.position.x,
                                      targetPosition.y - this.position.y)
        val distance = norm(centered_target)

        if distance < 1.0f then
            speed = Vector2(0.0f, 0.0f)
        else
            val normalized = Vector2(centered_target.x / distance,
                                     centered_target.y / distance)
            this.speed = Vector2(0.95f * speed.x + 0.05f * normalized.x,
                                 0.95f * speed.y + 0.05f * normalized.y)
            var angle = atan2(speed.y, speed.x)

            this.moveActor(this.position + this.speed)
            this.sprite.rotation = (angle * 180 / Pi).toFloat
        //returns true if the unit is close enough to the target
        distance < 1.0f
}
