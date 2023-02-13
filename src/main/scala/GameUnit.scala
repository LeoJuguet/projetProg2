package character

import actor.*
import gamestate.*
import sfml.system.*
import sfml.graphics.*

abstract class GameUnit(gameState: GameState) extends Actor(gameState):
    var speed = 0.0;
    var maxSpeed: Float;
    var maxHealth: Int;
    var currentHealth: Int;
    var regenerationRate: Int;
    var attackDamage: Int;
    var targetPosition: Vector2[Float];

    def killUnit()=
        this.destroy()

    def health_(newHealth: Int)=
        if newHealth > this.maxHealth then
            this.currentHealth = this.maxHealth
        else if newHealth <= 0 then
            this.killUnit()
        else
            this.currentHealth = newHealth
    
    def regenerate()=
        this.health_(this.currentHealth + regenerationRate)
    
    def takeDamage(damageTaken: Int)=
        this.health_(this.currentHealth - damageTaken)
    
    def moveTo(target: Vector2[Float])=
        this.targetPosition = target
    
    def position_(target: Vector2[Float])=
        this.position = target
    
    def distance2D(p1: Vector2[Float], p2: Vector2[Float]): Float=
        return (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y)

    def moveUnit()=
        var distanceToTarget = distance2D(this.targetPosition,this.position);
        var xDelta = this.targetPosition.x - this.position.x;
        var yDelta = this.targetPosition.y - this.position.y;
        if distanceToTarget < maxSpeed then
            this.move(xDelta,yDelta)
        else
            var xMove: Float = maxSpeed * xDelta / distanceToTarget;
            var yMove: Float = maxSpeed * yDelta / distanceToTarget;
            this.move(xMove, yMove)
    
    def attack(target: GameUnit)=
        target.takeDamage(this.attackDamage)