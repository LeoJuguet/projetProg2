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
import ship.{Action, Ship}
import resource.Resource


//This class is the brain controlling the actions of the player.
class PlayerController {
    var selectedUnits : ArrayBuffer[Actor] = ArrayBuffer[Actor]()
    var selectedTargets : ArrayBuffer[Actor] = ArrayBuffer[Actor]()
    var selectedPosTarget : Vector2[Float] = Vector2(0.0f, 0.0f)

    //this connection clears the selected actors.
    OnMouseButtonPressed.connect((button, x, y) => {
        if button == Mouse.Button.Left && KeyboardState.holdLeft == false then
            this.selectedUnits.clear()
            this.selectedTargets.clear()
        if button == Mouse.Button.Right && KeyboardState.holdRight == false then
            this.selectedTargets.clear()
    })

    //When the mouse is hold, the controller doesn't need to update anything, as the player hasn't finished his action.

    //this connection wait for the player to end his selection and take decisions accordingly.
    OnMouseButtonReleased.connect((button, x, y) => {
        //this case fills the selected units list with the units that are in the selected state.
        //player_actors_list is enought as the player can't give orders to any actor that is not under his control.
        if button == Mouse.Button.Left then
            GameState.player_actors_list.foreach(actor => {
                if actor.state == States.PRESSED then
                    this.selectedUnits += actor
                    //TODO : disconnect this connection when the actor is removed from the selected units list ! (in the press connection above)
                    actor.onDestroyed.connect(Unit => {
                        this.selectedUnits -= actor
                    })
            })
        //this case fills the selected targets list with the units that are in the targetted state.
        //here we need to check all the actors, as the orders may need a reference to any actor.
        if button == Mouse.Button.Right then
            GameState.actors_list.foreach(actor => {
                if actor.state == States.TARGET then
                    this.selectedTargets += actor
                    //TODO : disconnect this connection when the actor is removed from the selected targets list ! (in the press connection above)
                    //TODO : idle the actions of player_actors_list that are targeting this actor.
                    actor.onDestroyed.connect(Unit => {
                        this.selectedTargets -= actor
                    })
            })
            this.selectedPosTarget = KeyboardState.mouseWindow
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

            //TODO : when the target is destroyed, the action should be set to IDLE.
            unit.action = Action.ATTACK(target_ship)

        //otherwize, if there is a base, we attack it.
        } else if target_base.nonEmpty then {
                unit.action = Action.ATTACK(target_base(0))
        //else, if there is a resource, we mine it.
        } else if target_ressources.nonEmpty then {
                unit.action = Action.MINE(target_ressources(Random.nextInt(target_ressources.length)).asInstanceOf[Resource])
        } else {
            //finally, if there is no target, we move to the mouse position.
            this.selectedUnits.foreach(actor => {
                if actor.isInstanceOf[Ship] then {
                    actor.asInstanceOf[Ship].action = Action.MOVE(KeyboardState.mouseWindow)
                }
            })
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
    
    def updateActors() = {
        //at every turn, we check for unnocupied selected units and give them an action.
        this.selectedUnits.foreach(unit => {
            if unit.isInstanceOf[Ship] then
                if unit.asInstanceOf[Ship].action == Action.IDLE then
                    give_order(unit.asInstanceOf[Ship])
        })

        //update the player's units (not only the selected ones as they may have been selected in the previous turn and not finished their action)
        GameState.player_actors_list.foreach(_.asInstanceOf[Ship].updateUnit())

        //clear dead actions
        GameState.player_actors_list.foreach(actor =>
            if actor.isInstanceOf[Ship] then
                clearAction(actor.asInstanceOf[Ship]))
    }

    //TODO : this function should be in the main loop, not here.
    def updateView() = {/*
        GameState.camera.updateView()
        GameState.window.view = Immutable(GameState.camera.playerView)

        //TODO : the map should move twice as slow as the camera so that it looks like it is in the background (and it is artificially extended)
        //gestion de l'affichage des tilemaps.
        var x = GameState.camera.playerView.center.x
        var y = GameState.camera.playerView.center.y

        for i <- 0 to 7 do
            for j <- 0 to 7 do
                //On ne veut pas la distance entre le centre de la tilemap et le centre de la vue, mais la distance entre le centre de la tilemap et le bord de la vue.
                if abs(x - 540 - i * 512) < 540 + 512 && abs(y - 360 - j * 512) < 360 + 512 then
                    GameState.map_array(j)(i) match {
                        case Some(tilemap) => tilemap.loadTexture()
                        case None =>
                            GameState.map_array(j)(i) = Some(new TileMap("maps/purple/purple_" +  i.toString + j.toString + ".png", i, j))
                            GameState.map_array(j)(i).get.loadTexture()
                    }
                else
                    GameState.map_array(j)(i) = None*/
    }


}
