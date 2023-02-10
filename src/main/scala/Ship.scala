package character

import actor.*
import gamestate.*
import sfml.system.*
import sfml.graphics.*

//importing math for sqrt
import scala.math.*

//norm takes a vector and returns its norm

class Ship(gameState : GameState, team : Int, initialPosition : Vector2[Float]) extends GameUnit(gameState):
    var maxSpeed = 100.0;
    var speed = Vector2(0.0f, 0.0f);
    var maxHealth = 50;
    var health = 50;
    var regenerationRate = 0;
    var attackDamage = 10;
    var pos = initialPosition;
    var targetPosition = initialPosition;
    
    //TODO : each ship should be its own class, so this constructor should be removed
    def this(gameState: GameState, team: Int, shipID: Int, initialPosition: Vector2[Float]) =
        this(gameState, team, initialPosition)
        shipID match { 
            case 0 => {
                this.maxSpeed = 100.0;
                this.speed = Vector2(0.0f, 0.0f);
                this.maxHealth = 50;
                this.health = 50;
                this.regenerationRate = 0;
                this.attackDamage = 10;
                this.pos = initialPosition;
                this.targetPosition = initialPosition
            }
            case _ => () //No other ship yet implemented
        }
        gameState.actors_list += this
