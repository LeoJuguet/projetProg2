package character

import actor.*
import gamestate.*
import sfml.system.*


class Ship(gameState: GameState, team:Int, initialPosition: Vector2[Float]) extends GameUnit(gameState):
    var maxSpeed = 100.0;
    var maxHealth = 50;
    var currentHealth = 50;
    var regenerationRate = 0;
    var attackDamage = 10;
    var targetPosition = initialPosition;
    var currentAction = 0; //Action ID : 0 = Idle, 1 = Attacking, 2 = Mining
    var miningLength = 150;
    var remainingMiningTime = 0;
    var attackingLength = 15;
    var remainingAttackingTime = 0;
    var targetResource: Resources = _;
    var targetShip: Ship = _;
    
    def this(gameState: GameState, team:Int, shipId: Int, initialPosition: Vector2[Float],miningLength: Int)=
        this(gameState, team, initialPosition)
        shipId match { 
            case 0 => {
                this.maxSpeed = 100.0;
                this.maxHealth = 50;
                this.currentHealth = 50;
                this.regenerationRate = 0;
                this.attackDamage = 10;
                this.targetPosition = initialPosition;
                this.miningLength = miningLength
            }
            case _ => () //No other ship yet implemented. TODO: add other ship cases when implemented
        }

    def currentAction_(actionId: Int) =
        this.currentAction = actionId

    def startAttacking(targetShip: Ship) =
        this.currentAction_(1);
        this.remainingAttackingTime = attackingLength;
        this.targetShip = targetShip
    
    def startMining(targetResource: Resources) =
        this.currentAction_(2);
        this.remainingMiningTime = miningLength;
        this.targetResource = targetResource
    
    def advanceFrame() = 
        this.currentAction match {
            case 0 => ()
            case 1 => {
                var newAttackingTime = this.remainingAttackingTime - 1;
                this.remainingAttackingTime = newAttackingTime;
                if newAttackingTime == 0 then {
                    this.attack(targetShip);
                    this.currentAction_(0);
                }
            }
            case 2 => {
                var newMiningTime = this.remainingMiningTime - 1;
                this.remainingMiningTime = newMiningTime;
                if newMiningTime == 0 then {
                    //TODO: Implement gathering of resource for the team
                    targetResource.mined();
                    this.currentAction_(0);
                }
            }
        }