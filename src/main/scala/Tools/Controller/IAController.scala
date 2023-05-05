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
        //TODO : replace this random behavior by a real IA.
        //       select only from available units.
        //       target only ennemy units or resources.
        //       decide the priority of the actions.
        //       decide the number of units to send on a target unit or resource.
        //       ...
        //we select a random unit.
        var unit = GameState.enemy_actors_list(Random.nextInt(GameState.enemy_actors_list.length))
        this.select(unit)

        //we select a random target.
        // /!\ currently, the target can be a friendly unit. It can even be the self !
        var targ = GameState.actors_list(Random.nextInt(GameState.actors_list.length))
        this.target(targ)
    }

    def decideAction() = {
        //If a unit is selected, we give it an action depending on the target.
        this.selectedUnit match {
        case Some(unit) => {
            if unit.isInstanceOf[Ship] then {
                var ship = unit.asInstanceOf[Ship]
                if ship.action == Action.IDLE then {
                print("deciding action...")
                this.selectedTarget match {
                case Some(target) => {
                    if distance(unit.position, target.position) < 300 then {
                    if target.isInstanceOf[Ship] then {
                        print("attacking ship\n")
                        ship.action = Action.ATTACK(target)
                    } else if target.isInstanceOf[Base] then {
                        print("attacking base\n")
                        ship.action = Action.ATTACK(target)
                    } else if target.isInstanceOf[Asteroid] then {
                        print("mining\n")
                        ship.action = Action.MINE(target.asInstanceOf[Asteroid])
                    }
                } else {
                    print("moving\n")
                    //move the ship to a random position
                    var targetPos = Vector2(Random.nextFloat() * 500, Random.nextFloat() * 500)
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
        this.decideAction()
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
