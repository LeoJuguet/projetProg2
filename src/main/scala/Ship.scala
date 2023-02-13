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
    
    def this(gameState: GameState, team:Int, shipId: Int, initialPosition: Vector2[Float])=
        this(gameState, team, initialPosition)
        shipId match { 
            case 0 => {
                this.maxSpeed = 100.0;
                this.maxHealth = 50;
                this.currentHealth = 50;
                this.regenerationRate = 0;
                this.attackDamage = 10;
                this.targetPosition = initialPosition
            }
            case _ => () //No other ship yet implemented
        }
