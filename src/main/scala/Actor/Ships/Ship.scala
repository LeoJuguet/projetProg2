package ship

import scala.math.*

import sfml.system.*
import sfml.graphics.*

import shipmodule.ShipModule
import container.{Container, Wreck}
import actor.Actor
import clickable.Clickable
import gamestate.GameState
import asteroid.Asteroid
import manager.TextureManager
import ShipModules.*
import ship.Bird


//TODO : Actions should behave as a list, so that a player can assign several of them in one input to limit redundancy in the player's actions.
enum Action:
    case IDLE
    case MOVE(target : Vector2[Float])
    case ATTACK(target : Actor)
    case MINE(target : Asteroid)
    case TRANSFER(target : Container)

class Ship(
    teamID : Int,
    var name : String = "Ship"
)
extends Actor with Container with Bird {
    var maxHealth = 50
    var health = 50
    var regenerationRate = 0

    this.maxLoad = 20

    var action = Action.IDLE

    var team = teamID

    def kill() : Unit =
        print("killing game unit\n")
        this.destroy()
        this.live = false
    
    def heal(healAmount: Int) : Unit =
        if this.health + healAmount > this.maxHealth then
            this.health = this.maxHealth
        else
            this.health = this.health + healAmount
            
    def takeDamage(damageTaken: Int) : Unit =
        this.health = this.health - damageTaken
        if this.health <= 0 then
            //TODO : explosion animation
            //Maybe add a destroyed list buffer in the gamestate
            this.kill()
        
    def closeEnough(targetPosition : Vector2[Float]) : Boolean =
        distance(this.position, targetPosition) < this.collisionRadius
    
    def moveUnit(targetPosition : Option[Vector2[Float]]) : Boolean =
        val birdList = this.team match {
            case 0 => GameState.playerCollisionList
            case 1 => GameState.enemyCollisionList
        }
        val speedFactor = this.team match {
            case 0 => 1f
            case 1 => 0.5f
        }
        if targetPosition.isDefined then
            this.updateBird(targetPosition, birdList, speedFactor)

        //match the sprite orientation with the speed vector
        val angle = atan2(this.speed.y, this.speed.x) * 180 / Pi + 90
        this.sprite.rotation = angle.toFloat

        //returns true if the unit is close enough to the target
        if targetPosition.isDefined then
            this.closeEnough(targetPosition.get)
        else
            false

    def updateUnit() = {}

    def createWreckage() : Unit = {
        var wreck = new Wreck(this.position, this.rotation)
    }
}
