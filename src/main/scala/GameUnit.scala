package character

import actor.*
import gamestate.*
import sfml.system.*
import sfml.graphics.*

def norm(vector: Vector2[Float]) : Float =
    Math.sqrt(vector.x * vector.x + vector.y * vector.y).toFloat

abstract class GameUnit(gameState: GameState) extends Actor(gameState):
    var speed: Vector2[Float];
    var maxSpeed: Float;
    var maxHealth: Int;
    var health: Int;
    var regenerationRate: Int;
    var attackDamage: Int;
    var targetPosition: Vector2[Float];

    def killUnit()=
        this.destroy()

    def setHealth(newHealth: Int)=
        if newHealth > this.maxHealth then
            this.health = this.maxHealth
        else if newHealth <= 0 then
            this.killUnit()
        else
            this.health = newHealth
    
    def regenerate()=
        this.setHealth(this.health + regenerationRate)
    
    def takeDamage(damageTaken: Int)=
        this.setHealth(this.health - damageTaken)
    
    def moveTo(target: Vector2[Float])=
        this.targetPosition = target
    
    def setPosition(target: Vector2[Float])=
        this.position = target
    
    def distance2D(p1: Vector2[Float], p2: Vector2[Float]): Float=
        return (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y)

    def moveUnit() =
        if this.position == this.targetPosition then
            speed = Vector2(0.0f, 0.0f)
        else
            val centered_target = Vector2(targetPosition.x - this.position.x,
                                          this.targetPosition.y - this.position.y)
            val distance = norm(centered_target)
            val normalized = Vector2(centered_target.x / distance,
                                     centered_target.y / distance)
            this.speed = Vector2((0.95 * speed.x + 0.05 * normalized.x).toFloat,
                                 (0.95 * speed.y + 0.05 * normalized.y).toFloat)
            this.position = Vector2(this.position.x + this.speed.x, this.position.y + this.speed.y)
            //this.transform = Transform.translate(this.position)
            this.sprite.position = this.position
    
    def attack(target: GameUnit)=
        target.takeDamage(this.attackDamage)