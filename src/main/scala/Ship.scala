package character

import actor.*
import clickable.*
import gamestate.*
import module.*

import sfml.system.*
import sfml.graphics.*

import scala.math.*

class Ship(gameState : GameState, teamID : Int, shipID : Int, initialPosition : Vector2[Float]) extends GameUnit(gameState)
{
    var maxSpeed = 100.0;
    var speed = Vector2(0.0f, 0.0f);

    var maxHealth = 50;
    var health = 50;
    var regenerationRate = 0;

    var attackDamage = 10;

    this.position = initialPosition;
    var targetPosition = initialPosition;

    var currentAction = 0; //Action ID : 0 = Idle, 1 = Attacking, 2 = Mining
    
    //bruh, tout ça ce sera dans les modules dédiés, pourquoi tu les met ici ? :o
    var miningSpeed = 150;
    var miningCoolDown = 0;
    var attackSpeed = 15;
    var attackCoolDown = 0;
    var targetResource: Resources = _;
    var targetShip: Ship = _;

    var ID = shipID;
    var team : Int = teamID;
    var target = this

    var modules = List[Module]()

    var random_move_array : Array[Vector2[Float]] = Array(Vector2(0.0f, 0.0f))
    var move_index = 0
    
   
    gameState.actors_list += this
    if teamID == 0 then
        gameState.player = this
    
    
    def attack() : Unit =
        this.currentAction = 1
        this.attackCoolDown = this.attackSpeed
        //mais... on n'attaque rien là :o
    
    def mine() : Unit =
        this.currentAction = 2
        this.miningCoolDown = this.miningSpeed
    
    def updateUnit() =
        this.moveUnit()
        this.regenerate()
        this.currentAction match {
            case 0 => ()
            case 1 => {
                if this.attackCoolDown > 0 then
                    this.attackCoolDown = min(0, this.attackCoolDown - 1)
                else
                    this.attack()
            }
            case 2 => {
                if this.miningCoolDown > 0 then
                    this.miningCoolDown = min(0, this.miningCoolDown - 1)
                else
                    //TODO: Implement gathering of resource for the team
                    this.mine()
            }
        }
}

