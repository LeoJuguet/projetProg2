package ship

import scala.math.*

import sfml.system.*
import sfml.graphics.*

import ship.{GameUnit, Base}
import shipmodule.ShipModule
import container.Container
import actor.Actor
import clickable.Clickable
import gamestate.GameState
import resource.Resource
import manager.TextureManager


enum Action:
    case IDLE
    case MOVE(target : Vector2[Float])
    case ATTACK(target : Actor)
    case MINE(target : Resource)
    case TRANSFER(target : Container)

class Ship(
    teamID : Int,
    initialPosition : Vector2[Float]
)
extends GameUnit with Container {
    texture = TextureManager.get("ovni.png")
    this.applyTexture()
    var maxSpeed = 100.0
    var speed = Vector2(0.0f, 0.0f)

    var maxHealth = 50
    var _health = 50
    var regenerationRate = 0

    var attackDamage = 5
    var attackSpeed = 500
    var attackCoolDown = 10

    var miningDamage = 10
    var miningSpeed = 200
    var miningCoolDown = 0

    this.maxLoad = 20

    this.moveActor(initialPosition)

    var action = Action.IDLE

    this.team = teamID


    var shipDimension = Vector2(5,5)
    var modules = Array.ofDim[Option[ShipModule]](shipDimension.x,shipDimension.y)

    var random_move_array : Array[Vector2[Float]] = Array(Vector2(0.0f, 0.0f))
    
    def attack() : Unit =
        this.action match
        case Action.ATTACK(target : Ship) => target.takeDamage(this.attackDamage)
        case Action.ATTACK(target : Base) => target.takeDamage(this.attackDamage)
        case _ => print("Error : attack action not valid\n")
        
    def mine() : Unit =
        this.action match
        case Action.MINE(target : Resource) =>
            var obtained = target.mined(this.miningDamage)
            this.in(target, obtained)
        case _ => print("Error : mine action not valid\n")
    
    def transfer() : Unit =
        //TODO : implement transfer rate (here it is 10)
        this.action match { case Action.TRANSFER(target : Container) =>
            if this.ethereum > 0 then
                this.transfer(target, "etherum", 10)
            else if this.uranium > 0 then
                this.transfer(target, "uranium", 10)
            else if this.iron > 0 then
                this.transfer(target, "iron", 10)
            else if this.copper > 0 then
                this.transfer(target, "copper", 10)
            if this.scrap > 0 then
                this.transfer(target, "scrap", 10)
        case _ => print("Error : transfer action not valid\n")
        }
    
    def updateUnit() =
        this.regenerate()

        this.attackCoolDown = max(0, this.attackCoolDown - 1)
        this.miningCoolDown = max(0, this.miningCoolDown - 1)

        this.action match {
            case Action.IDLE => ()
            case Action.MOVE(target) => {
                if this.moveUnit(target) then
                    this.action = Action.IDLE
            }
            case Action.ATTACK(target) => {
                //if the ship is close enough to the target, it will attack it
                if norm(this.position - target.asInstanceOf[Actor].position) < 50 then
                    if this.attackCoolDown == 0 then
                        this.attack()
                        this.attackCoolDown = this.attackSpeed
                        //there is no need to check if the target is still alive, because the player controller will change the target if it dies
                //if the ship is not close enough, it will move towards the target
                else
                    this.moveUnit(target.asInstanceOf[Actor].position)
            }
            case Action.MINE(target) => {
                //if the ship is close enough to the resource, it will mine it
                if norm(this.position - target.asInstanceOf[Actor].position) < 50 then
                    //Check if cooldown is over
                    if this.miningCoolDown == 0 then
                        this.mine()
                        this.miningCoolDown = this.miningSpeed

                        //Check if full
                        if this.totalLoad == this.maxLoad then
                            //start transfering
                            this.action = Action.TRANSFER(GameState.actors_list.find(actor => actor match {
                                case base : Base => base.team == this.team
                                case _ => false
                            }) match {
                                case Some(base : Base) => base
                                case _ => print("Error : no base found for the team\n"); null
                            })
                //if the ship is not close enough, it will move towards the resource
                else
                    this.moveUnit(target.asInstanceOf[Actor].position)
            }
            case Action.TRANSFER(target) => {
                //if the ship is close enough to the mase, it will transfer the resources
                if norm(this.position - target.asInstanceOf[Actor].position) < 50 then
                    this.transfer()

                    //Check if empty
                    if this.totalLoad == 0 then
                        this.action = Action.IDLE
                
                //if the ship is not close enough, it will move towards the base
                else
                    this.moveUnit(target.asInstanceOf[Actor].position) //TODO : not exactly the center of the base. Same for other targets, not exactly ther center.
            }
        }
}

