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

