package controller

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

import sfml.system.Vector2
import sfml.window.Mouse

import gamestate.GameState
import clickable.Clickable
import actor.Actor
import ship.Base
import event.{
    KeyboardState,
    OnMouseButtonPressed,
    OnMouseButtonReleased
}
import ship.{
    Action,
    Ship,
    Drone
}
import asteroid.Asteroid
import sfml.system.distance


//This class is the brain controlling the actions of the player.
object IAController {
    var selectedUnit : Option[Actor] = None
    var selectedTarget : Option[Actor] = None

    //Every frame, only one unit can be selected by the AI.
    def select(unit : Actor) = {
        this.selectedUnit = Some(unit)
    }

    def target(unit : Actor) = {
        this.selectedTarget = Some(unit)
    }
    
    def clearAction(ship : Ship) =
        ship.action match {
            case Action.ATTACK(target) => {
                if !target.live then
                    ship.action = Action.IDLE
            }
            case Action.MINE(target) => {
                if !target.live then
                    ship.action = Action.IDLE
            }
            case Action.TRANSFER(target) => {
                if target.isInstanceOf[Actor] then
                    if !target.asInstanceOf[Actor].live then
                        ship.action = Action.IDLE
            }
            case _ => {}
        }
    
    def selectUnit() = {
        //TODO : Even if the AI has been "improved" it is still very dumb. We really need a good AI for the game to be playable.
        //       For example decide the number of units to send on a target unit or resource, decide the urgence (as a probability) to
        //       go minig exploring regrouping, or attacking, etc.
        //
        //       To have a smarter IA we need to be able to know what unit is closest to a target, or what target is the closest to a unit.
        //       This is not possible with the current data structures as it would require to iterate over all the units and targets and
        //       the complexity would be stupid.
        
        //we select a random unit.
        var unit = GameState.enemy_actors_list(Random.nextInt(GameState.enemy_actors_list.length))
        this.select(unit)

        //we select a random target.
        var targ = GameState.actors_list(Random.nextInt(GameState.actors_list.length))
        if !targ.isInstanceOf[Asteroid] then
            targ = GameState.player_actors_list(Random.nextInt(GameState.player_actors_list.length))
        
        this.target(targ)
    }

    def decideAction() = {
        //If a unit is selected, we give it an action depending on the target.
        this.selectedUnit match {
        case Some(unit) => {
            if unit.isInstanceOf[Drone] then {
                val ship = unit.asInstanceOf[Drone]
                if ship.action == Action.IDLE then {
                this.selectedTarget match {
                case Some(target) => {
                if distance(unit.position, target.position) < 300 then {
                    if target.isInstanceOf[Ship] then {
                        ship.action = Action.ATTACK(target)
                    } else if target.isInstanceOf[Base] then {
                        ship.action = Action.ATTACK(target)
                    } else if target.isInstanceOf[Asteroid] then {
                        ship.action = Action.MINE(target.asInstanceOf[Asteroid])
                    }
                } else {
                    //move the ship to a random position
                    val radius = (Random.nextFloat() + 1 ) * 50
                    val angle = Random.nextFloat() * 2 * Math.PI
                    var targetPos = Vector2(radius * Math.cos(angle).toFloat, radius * Math.sin(angle).toFloat)
                    ship.action = Action.MOVE(ship.position + targetPos)
                }}
                case None => {}
                }}
            }
        }
        case None => {}
        }
    }

    def releaseUnit() = {
        this.selectedUnit = None
        this.selectedTarget = None
    }
    
    //this function updates the actions of the player's units.
    def updateActors() = {
        this.selectUnit()
        //this.decideAction()
        this.releaseUnit()

        //update the player's units (not only the selected ones as they may have been selected in the previous turn and not finished their action)
        GameState.enemy_actors_list.foreach(actor => {
            if actor.isInstanceOf[Ship] then
                actor.asInstanceOf[Ship].updateUnit()
        })
        //clear dead actions
        GameState.enemy_actors_list.foreach(actor =>
            if actor.isInstanceOf[Ship] then
                clearAction(actor.asInstanceOf[Ship]))
    }
}
