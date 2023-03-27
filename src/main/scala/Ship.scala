package character

import actor.*
import clickable.*
import gamestate.*
import shipmodule.*
import base.*

import sfml.system.*
import sfml.graphics.*

import scala.math.*
import manager.TextureManager

enum Action:
    case IDLE
    case ATTACK
    case MINE
    case TRANSFER

class Ship( teamID : Int, shipID : Int, initialPosition : Vector2[Float]) extends GameUnit
{
    texture = TextureManager.get("ovni.png")
    this.applyTexture()
    var maxSpeed = 100.0;
    var speed = Vector2(0.0f, 0.0f);

    var maxHealth = 50;
    var _health = 50;
    var regenerationRate = 0;

    var attackDamage = 5;
    var attackSpeed = 500;
    var attackCoolDown = 10;

    var miningDamage = 10;
    var miningSpeed = 200;
    var miningCoolDown = 0;

    var scrap = 0
    var cooper = 0
    var iron = 0
    var uranium = 0
    var ethereum = 0

    var totalLoad = 0
    var maxTotalLoad = 100

    this.moveActor(initialPosition);
    var targetPosition = initialPosition;

    var currentAction = Action.IDLE;
    
    //TODO : change that for a mining target, a transfer target and an attack target. It will simplify many other cases.
    var targetResource: Option[Resource] = None
    var targetShip: Option[Ship] = None
    var targetBase: Option[Base] = None

    var ID = shipID;
    var team : Int = teamID;


    var shipDimension = Vector2(5,5)
    var modules = Array.ofDim[Option[ShipModule]](shipDimension.x,shipDimension.y)

    var random_move_array : Array[Vector2[Float]] = Array(Vector2(0.0f, 0.0f))
    var move_index = 0

   
    GameState.actors_list += this
    if teamID == 0 then
        GameState.player = this
    
    def attack() : Unit =
        //attaque en priorité les vaisseaux ennemis puis les bases.
        if this.targetShip != None then
            this.targetShip.get.takeDamage(this.attackDamage)
        else if this.targetBase != None then
            this.targetBase.get.takeDamage(this.attackDamage)
    
    def mine() : Unit =
        //TODO: Implement mining function here and in ressource
        this.targetResource.get.mined(this.miningDamage)

        this.targetResource match {
            case Some(scrap : Scrap) => this.scrap += min(this.miningDamage, this.maxTotalLoad - this.totalLoad)
            case Some(cooper : Cooper) => this.cooper += min(this.miningDamage, this.maxTotalLoad - this.totalLoad)
            case Some(iron : Iron) => this.iron += min(this.miningDamage, this.maxTotalLoad - this.totalLoad)
            case Some(uranium : Uranium) => this.uranium += min(this.miningDamage, this.maxTotalLoad - this.totalLoad)
            case Some(ethereum : Ethereum) => this.ethereum += min(this.miningDamage, this.maxTotalLoad - this.totalLoad)
            case Some(_) => print("ERROR !!! Forgot to implement a ressource case !"); ()
            case None => ()
        }

        this.totalLoad = this.scrap + this.cooper + this.iron + this.uranium + this.ethereum
    
    def transfer() : Unit =
        //TODO : implement transfer rate (here it is 10)
        if this.ethereum > 0 then
            this.ethereum -= min(10, this.ethereum)
            this.targetBase.get.ethereum += 10
        else if this.uranium > 0 then
            this.uranium -= 10
            this.targetBase.get.uranium += 10
        else if this.iron > 0 then
            this.iron -= 10
            this.targetBase.get.iron += 10
        else if this.cooper > 0 then
            this.cooper -= 10
            this.targetBase.get.cooper += 10
        else if this.scrap > 0 then
            this.scrap -= 10
            this.targetBase.get.scrap += 10
    
    def updateUnit() =
        this.moveUnit()
        this.regenerate()

        this.attackCoolDown = max(0, this.attackCoolDown - 1)
        this.miningCoolDown = max(0, this.miningCoolDown - 1)

        this.currentAction match {
            case Action.IDLE => ()
            case Action.ATTACK => {
                if this.attackCoolDown == 0 then
                    this.attack()
                    this.attackCoolDown = this.attackSpeed

                    //Check if the targets are still alive
                    (this.targetShip, this.targetBase) match {
                        case (None, None) => this.currentAction = Action.IDLE
                        case (Some(ship), None) => if ship.live == false then this.currentAction = Action.IDLE
                        case (None, Some(base)) => if base.live == false then this.currentAction = Action.IDLE
                        case (Some(ship), Some(base)) => if ship.live == false && base.live == false then this.currentAction = Action.IDLE
                    }
            }
            case Action.MINE => {
                //Check if cooldown is over
                if this.miningCoolDown == 0 then
                    //TODO: Implement gathering of resource for the team
                    this.mine()
                    this.miningCoolDown = this.miningSpeed

                    //Check if the target is still alive
                    if this.targetResource.get.live == false then
                        this.currentAction = Action.IDLE

                    //Check if full
                    if this.totalLoad == this.maxTotalLoad then
                        this.currentAction = Action.TRANSFER
            }
            case Action.TRANSFER => {
                //first, the ship needs to find a base to transfer to
                if this.targetBase == None then
                    this.targetBase = GameState.actors_list.find(actor => actor match {
                        case base : Base => base.team == this.team
                        case _ => false
                    }) match {
                        case Some(base : Base) => Some(base)
                        case _ => None
                    }

                //if the ship is close enough to the mase, it will transfer the resources
                else if norm(Vector2(this.position.x - this.targetBase.get.position.x, this.position.y - this.targetBase.get.position.y)) < 50 then
                    this.transfer()

                    //Check if empty
                    if this.totalLoad == 0 then
                        this.currentAction = Action.IDLE
                
                //if the ship is not close enough, it will move towards the base
                else
                    this.targetPosition = this.targetBase.get.position //TODO : not exactly the center of the base. Same for other targets, not exactly ther center.
            }
        }
}

