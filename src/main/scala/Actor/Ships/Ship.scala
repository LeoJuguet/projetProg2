package ship

import scala.math.*

import sfml.system.*
import sfml.graphics.*

import shipmodule.ShipModule
import container.Container
import actor.Actor
import clickable.Clickable
import gamestate.GameState
import asteroid.Asteroid
import manager.TextureManager
import ShipModules.*


//TODO : Actions should behave as a list, so that a player can assign several of them in one input to limit redundancy in the player's actions.
enum Action:
    case IDLE
    case MOVE(target : Vector2[Float])
    case ATTACK(target : Actor)
    case MINE(target : Asteroid)
    case TRANSFER(target : Container)

class Ship(
    teamID : Int,
)
extends Actor with Container {
    var speed = Vector2(0.0f, 0.0f)

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

    //TODO : redo the collision and remove the distinct cases. This might only be usefull when quadtree is implemented, and it's not even sure.
    def asteroidCollision() : Option[Actor] =
        var res : Option[Actor] = None
        for asteroid <- GameState.asteroids_list do
            if distance(this.position, asteroid.position) < asteroid.collisionRadius + this.collisionRadius then
                res = Some(asteroid)
        res
    
    def droneCollision() : Option[Actor] =
        var res : Option[Actor] = None
        for drone <- GameState.drones_list do
            if drone != this && distance(this.position, drone.position) < drone.collisionRadius + this.collisionRadius then
                res = Some(drone)
        res
    
    def baseCollision() : Option[Actor] =
        var res : Option[Actor] = None
        res

    def capitalShipCollision() : Option[Actor] =
        None
    
    def checkCollision() : Option[Actor] =
        //check collision with asteroids
        //then with drones
        //then with bases
        //then with capital ships

        //for now, collisions are in O(n^2) but it could be optimized to O(n) with a quadtree
        //quadtree shall be used only for the asteroids and the drones

        asteroidCollision() match
        case Some(asteroid) => Some(asteroid)
        case None => droneCollision() match
        case Some(drone) => Some(drone)
        case None => baseCollision() match
        case Some(base) => Some(base)
        case None => capitalShipCollision() match
        case Some(capitalShip) => Some(capitalShip)
        case None => None
    
    def moveUnit(targetPosition : Vector2[Float]) : Boolean =
        //this function is used to move the unit towards a target position
        //it starts by setting the position in the self reference frame
        val centered_target = Vector2(targetPosition.x - this.position.x,
                                      targetPosition.y - this.position.y)
        val distance = norm(centered_target)

        if distance < 10.0f then
            this.speed = Vector2(0.0f, 0.0f)
        else
            val normalized = Vector2(centered_target.x / distance,
                                     centered_target.y / distance)
            val inertia = 0.98f
            this.speed = Vector2(inertia * this.speed.x + (1 - inertia) * normalized.x,
                                 inertia * this.speed.y + (1 - inertia) * normalized.y)
            val angle = atan2(speed.y, speed.x)


            val oldPosition = this.position
            val oldRotation = this.sprite.rotation

            this.moveActor(this.position + this.speed)
            this.sprite.rotation = (angle * 180 / Pi).toFloat

            //check for collision
            this.checkCollision() match
            case Some(actor) =>
                //we start by moving the unit on the edge of the collision
                //finds the position of the actor in the self reference frame
                val actorPosition = Vector2(actor.position.x - this.position.x,
                                            actor.position.y - this.position.y)
                //then we move the actor to the edge of the collision
                val distance = norm(actorPosition)
                val newActorPosition = Vector2(actorPosition.x * (this.collisionRadius + actor.collisionRadius + 10) / distance,
                                               actorPosition.y * (this.collisionRadius + actor.collisionRadius + 10) / distance)
                this.moveActor(this.position + actorPosition - newActorPosition)
                //then rotate the reference frame so that the actor normalized position is (0, 1)
                val angle = atan2(newActorPosition.y, newActorPosition.x)
                //then rotate the speed to match this new reference frame
                val rotatedSpeed = Vector2(speed.x * cos(angle) - speed.y * sin(angle),
                                           speed.x * sin(angle) + speed.y * cos(angle))
                //then flip the speed around the y axis
                val flippedSpeed = Vector2(-rotatedSpeed.x, rotatedSpeed.y)
                //then rotate the speed back to the original reference frame
                val newSpeed = Vector2(flippedSpeed.x * cos(-angle) - flippedSpeed.y * sin(-angle),
                                       flippedSpeed.x * sin(-angle) + flippedSpeed.y * cos(-angle))
                //then set the new speed
                this.speed = Vector2(newSpeed.x.toFloat, newSpeed.y.toFloat)
                //and add a little bump as a portion of the actorPosition vector to avoid getting stuck and to ... well, bump
                this.speed = this.speed + Vector2(actorPosition.x * 0.1f, actorPosition.y * 0.1f)
                //then move the unit back to its original position
                this.moveActor(oldPosition)
                this.sprite.rotation = oldRotation
                //move the collided actor in the opposite direction. If it is a ship, update its speed too.
                actor.moveActor(actor.position - this.speed)
                actor match
                case ship: Ship =>
                    //TODO : this may cause explosions of speed values. Maybe add a linear interpolation to avoid this.
                    ship.speed = ship.speed * 0.5f - this.speed * 0.5f
                case _ => ()
            case None => ()

        //returns true if the unit is close enough to the target
        distance < 1.0f

    def updateUnit() = {}
}
