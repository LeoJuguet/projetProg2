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

    var ID = shipID;
    var team : Int = teamID;
    var target = this

    var modules = List[Module]()

    var random_move_array : Array[Vector2[Float]] = Array(Vector2(0.0f, 0.0f))
    var move_index = 0
    
   
    gameState.actors_list += this
    if teamID == 0 then
        gameState.player = this
}

class Player(gameState : GameState, team : Int, shipID : Int, initialPosition : Vector2[Float]) extends Ship(gameState : GameState, team : Int, shipID : Int, initialPosition : Vector2[Float])
{
    override def update(mousePos: Vector2[Float], leftMouse: Boolean, rightMouse: Boolean): Unit =
        if(this.sprite.globalBounds.contains(mousePos.x, mousePos.y)){
            if(leftMouse){
                if(this.state != States.PRESSED) then
                    this.onClicked()
                    this.state = States.PRESSED
                else if(this.state == States.PRESSED) then
                    this.onReleased()
                    this.state = States.HOVER
            }else {
                if (rightMouse && this.state == States.PRESSED)
                    this.onReleased()
                    this.state = States.HOVER
                if this.state == States.IDLE then
                    this.state = States.HOVER
            }
        }else{
            if(this.state == States.HOVER)
                this.onUnhovered()
                this.state = States.IDLE

            if (leftMouse && this.state == States.PRESSED)
                this.onReleased()
                this.state = States.IDLE
            
            if (rightMouse && this.state == States.PRESSED && this.target == this)
                this.targetPosition = mousePos
        }

        state match
            case States.IDLE => {
                this.sprite.color= this.idleColor
            }
            case States.HOVER => {
                this.onHovered()
            }
            case States.PRESSED => {
                this.onPressed()
            }
}