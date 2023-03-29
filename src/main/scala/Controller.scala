package controller

import sfml.system.Vector2
import sfml.window.Mouse

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

import gamestate.*
import clickable.*
import actor.*
import base.*
import event.{OnMouseButtonPressed, OnMouseButtonReleased}
import event.KeyboardState
import event.InputManager
import ship.{Action, Ship, Drone}
import resource.Resource


//This class is the brain controlling the actions of the player.
object PlayerController {
    var selectedUnits : ArrayBuffer[Actor] = ArrayBuffer[Actor]()
    var selectedTargets : ArrayBuffer[Actor] = ArrayBuffer[Actor]()
    var selectedPosTarget : Option[Vector2[Float]] = None

    var justReleased : Boolean = false

    OnMouseButtonPressed.connect((button, x, y) => {
        if button == Mouse.Button.Right || button == Mouse.Button.Left then
            this.selectedPosTarget = None
    })
    //this connection wait for the player to end his selection and take decisions accordingly.
    OnMouseButtonReleased.connect((button, x, y) => {
        if button == Mouse.Button.Right then
            this.selectedPosTarget = Some(KeyboardState.mouseView)
            this.justReleased = true
    })
    
    def updateClick() = {
        //TODO : LEO : il faut intégrer les nouveaux event à ta GUI pour que cette ligne disparaisse.
        GameState.widgets.foreach(_.updateClick(KeyboardState.mouseWindow, KeyboardState.leftMouse))
    }

    def give_order(unit : Ship) =
        //we check if he has selected some targets.
        if this.selectedTargets.nonEmpty then {
        var target_ships = this.selectedTargets.filter(actor => actor.isInstanceOf[Ship])
        var target_base = this.selectedTargets.filter(actor => actor.isInstanceOf[Base])
        var target_ressources = this.selectedTargets.filter(actor => actor.isInstanceOf[Resource])

        //if there is an ennemy ship, we attack it.
        if target_ships.nonEmpty then {
            var target_ship = target_ships(Random.nextInt(target_ships.length))
            unit.action = Action.ATTACK(target_ship)

        //otherwize, if there is a base, we attack it.
        } else if target_base.nonEmpty then {
            unit.action = Action.ATTACK(target_base(0))
        //else, if there is a resource, we mine it.
        } else if target_ressources.nonEmpty then {
            unit.action = Action.MINE(target_ressources(Random.nextInt(target_ressources.length)).asInstanceOf[Resource])
        }}
         else {
            //finally, if there is no target, we move to the mouse position.
            if this.selectedPosTarget != None then {
                unit.action = Action.MOVE(this.selectedPosTarget.get)
            }
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
    
    //this function gives orders to the selected units if we just released the right mouse button.
    def force_order() = {
        if this.justReleased then
            this.selectedUnits.foreach(unit => {
                if unit.isInstanceOf[Ship] then
                    give_order(unit.asInstanceOf[Ship])
            })
        this.justReleased = false
    }
    
    //this function updates the actions of the player's units.
    def updateActors() = {
        //at every turn, we check for unnocupied selected units and give them an action.
        this.selectedUnits.foreach(unit => {
            if unit.isInstanceOf[Ship] then
                if unit.asInstanceOf[Ship].action == Action.IDLE then
                    give_order(unit.asInstanceOf[Ship])
        })

        //update the player's units (not only the selected ones as they may have been selected in the previous turn and not finished their action)
        GameState.player_actors_list.foreach(actor => {
            if actor.isInstanceOf[Ship] then
                actor.asInstanceOf[Ship].updateUnit()
        })
        //clear dead actions
        GameState.player_actors_list.foreach(actor =>
            if actor.isInstanceOf[Ship] then
                clearAction(actor.asInstanceOf[Ship]))
    }
}
