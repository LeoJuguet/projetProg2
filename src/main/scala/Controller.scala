package controller

import scala.math.*

import sfml.window.*
import sfml.graphics.*
import sfml.system.*

import gamestate.*
import clickable.*
import actor.*
import character.*
import ia.*
import tilemap.*
import base.*
import sfml.Immutable
import event.{OnMouseButtonPressed, OnMouseButtonReleased}
import event.KeyboardState
import event.InputManager


//This class is used to controll the game.
//It is in charge of dealing with inputs and events, and updating the gamestate and game units actions accordingly.
//TODO : update this file to match the new architecture of events.
//TODO : This class should not be used to controll the game but only one player. Update the class so that it only controlls one player.
//       And create a new IAController class.
class Controller(window : RenderWindow) {
    //the main selected actor is persistant between frames.
    var selectedActor : Option[Actor] = None
    //the secondary selected actor is reset at the end of each frame.
    //it is used to select a secondary target for an action.
    var selectedSecondaryActor : Option[Actor] = None

    //exemple de gestion d'event correcte avec le nouveau système d'event :

    def updateClick() = {
        //TODO : faire une liste d'acteurs affichés (pour ne pas parcourir tous les acteurs)
        this.selectedSecondaryActor = None

        GameState.widgets.foreach(_.updateClick(KeyboardState.mouseWindow, KeyboardState.leftMouse))
        
        for actor <- GameState.actors_list do
            if KeyboardState.leftMouse && actor.state == States.PRESSED then
                actor match {
                    case player : Player =>
                        this.selectedActor = Some(player)
                    case ship : Ship =>
                        this.selectedActor match {
                            case Some(player : Player) => ()
                            case _ => this.selectedActor = Some(ship)
                        }
                    case resource : Resource =>
                        this.selectedActor match {
                            case Some(player : Player) => ()
                            case Some(ship : Ship) => ()
                            case _ => this.selectedActor = Some(resource)
                        }
                    case _ => ()
                }
            
            
            if KeyboardState.rightMouse && actor.state == States.HOVER then
                actor match {
                    //case player : Player =>
                        //this.selectedSecondaryActor = Some(player)
                    case ship : Ship =>
                        this.selectedSecondaryActor match {
                            case Some(player : Player) => ()
                            case _ => this.selectedSecondaryActor = Some(ship)
                        }
                    case resource : Resource =>
                        this.selectedSecondaryActor match {
                            case Some(player : Player) => ()
                            case Some(ship : Ship) => ()
                            case _ => this.selectedSecondaryActor = Some(resource)
                        }
                    case _ => ()
                }
    }
    
    def updateActors() = {
        for actor <- GameState.actors_list do
            actor match {
                case player : Player =>
                    this.selectedActor match {
                        case Some(_ : Player) =>
                            this.selectedSecondaryActor match {
                                case Some(ship : Ship) if ship != player =>
                                    player.targetShip = Some(ship)
                                    player.currentAction = Action.ATTACK
                                case Some(resource : Resource) =>
                                    player.targetResource = Some(resource)
                                    player.currentAction = Action.MINE
                                case Some(base : Base) =>
                                    player.targetBase = Some(base)
                                    if base.team == 0 then player.currentAction = Action.TRANSFER
                                    else player.currentAction = Action.ATTACK //TODO : faire une action pour attaquer une base
                                case _ => ()
                            }

                            //TODO : ça et IA, c'est pour décider l'action, et updateUnit ça fait les actions
                            player.updateUnit()

                        case Some(ennemy : Ship) if ennemy.team == 1 =>
                            IA(ennemy, GameState.player)
                            ennemy.updateUnit()

                        case Some(ressource : Resource) => () //éventuellement faire des ressources mouvantes (comme des astéroides minables)
                        case _ => ()
                    }
                case _ => {}
            }
    }

    //TODO : fonction pas encore utilisée. Il faut faire la view.
    def updateView() = {
        GameState.camera.updateView()
        GameState.window.view = Immutable(GameState.camera.playerView)

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
                    GameState.map_array(j)(i) = None
    }


}
