package character

import actor.*
import gamestate.*
import sfml.system.*
import sfml.graphics.*

abstract class GameUnit(gameState: GameState) extends Actor(gameState)
{
    var speed: Vector2[Float]
    var maxSpeed: Float
    var maxHealth: Int
    var health: Int
    var regenerationRate: Int
    var attackDamage: Int
    var targetPosition: Vector2[Float]

    def killUnit() : Unit =
        this.destroy()

    def health_(newHealth: Int) : Unit =
        if newHealth > this.maxHealth then
            this.health = this.maxHealth
        else if newHealth <= 0 then
            this.killUnit()
        else
            this.health = newHealth
    
    def regenerate() : Unit =
        this.health = this.maxHealth.min(this.health + regenerationRate)

    def takeDamage(damageTaken: Int) : Unit =
        this.health = this.health - damageTaken

    def moveTo(target: Vector2[Float]) : Unit =
        this.targetPosition = target
    
    def setPosition(target: Vector2[Float]) : Unit =
        this.sprite.position = target
    
    def distance2D(p1: Vector2[Float], p2: Vector2[Float]) : Float =
        return (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y)

    def moveUnit() : Unit =
        val centered_target = Vector2(this.targetPosition.x - this.sprite.position.x,
                                      this.targetPosition.y - this.sprite.position.y)
        val distance = norm(centered_target)

        if distance < 1.0f then
            speed = Vector2(0.0f, 0.0f)
        else
            val normalized = Vector2(centered_target.x / distance,
                                     centered_target.y / distance)
            this.speed = Vector2(0.95f * speed.x + 0.05f * normalized.x,
                                 0.95f * speed.y + 0.05f * normalized.y)
            this.sprite.position=(this.sprite.position.x + this.speed.x, this.sprite.position.y + this.speed.y)
            this.rotation = Math.atan2(this.speed.y, this.speed.x).toFloat * 180 / Math.PI.toFloat
    
    def attack(target: GameUnit)=
        target.takeDamage(this.attackDamage)
}
