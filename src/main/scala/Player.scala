package character

import sfml.system.*
import sfml.graphics.*

import actor.*
import clickable.*
import module.*
import gamestate.*

import scala.math.*

import character.Ship


//Attention ! Cette classe est amenée à être supprimée. Elle sert juste pour la démo pour montrer ce qu'on peut faire, en controllant un unique petit vaisseau.
class Player(team : Int, shipID : Int, initialPosition : Vector2[Float]) extends Ship(team, shipID, initialPosition)
{
    maxHealth = 100;
    _health = 100;
    override def updateClick(mousePos: Vector2[Float], leftMouse: Boolean, rightMouse: Boolean): Unit =
        if(this.transform.transformRect(this.sprite.globalBounds).contains(mousePos.x, mousePos.y)){
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

            if (rightMouse && this.state == States.PRESSED)// && this.targetShip == this)
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


class PlayerState(){

}
