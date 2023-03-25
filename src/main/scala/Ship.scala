package character

import actor.*
import controller.*
import clickable.*
import gamestate.*
import shipmodule.*

import sfml.system.*
import sfml.graphics.*

import scala.math.*
import manager.TextureManager

enum Action:
    case IDLE
    case ATTACK
    case MINE

class Ship( controller : Controller, teamID : Int, shipID : Int, initialPosition : Vector2[Float]) extends GameUnit(controller)
{
    texture = TextureManager.get("ovni.png")
    this.applyTexture()
    var maxSpeed = 100.0;
    var speed = Vector2(0.0f, 0.0f);

    var maxHealth = 50;
    var health = 50;
    var regenerationRate = 0;

    var attackDamage = 5;
    var attackSpeed = 500;
    var attackCoolDown = 10;

    var miningDamage = 10;
    var miningSpeed = 200;
    var miningCoolDown = 0;

    var scrap = 0;

    this.position = initialPosition;
    var targetPosition = initialPosition;

    var currentAction = Action.IDLE;
    
    var targetResource: Resource = _;
    var targetShip: Ship = this;

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
        this.targetShip.takeDamage(this.attackDamage)
    
    def mine() : Unit =
        //TODO: Implement mining function here and in ressource
        this.targetResource.mined(this.miningDamage)
        this.scrap += this.miningDamage
    
    def updateUnit() =
        this.moveUnit()
        this.regenerate()
        this.currentAction match {
            case Action.IDLE => ()
            case Action.ATTACK => {
                if this.attackCoolDown > 0 then
                    this.attackCoolDown = max(0, this.attackCoolDown - 1)
                else
                    this.attack()
                    this.attackCoolDown = this.attackSpeed
                    if this.targetShip.live == false then
                        this.currentAction = Action.IDLE
            }
            case Action.MINE => {
                if this.miningCoolDown > 0 then
                    this.miningCoolDown = max(0, this.miningCoolDown - 1)
                else
                    //TODO: Implement gathering of resource for the team
                    this.mine()
                    this.miningCoolDown = this.miningSpeed
                    if this.targetResource.live == false then
                        this.currentAction = Action.IDLE
            }
        }
}

