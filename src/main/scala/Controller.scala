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

class Controller(window : RenderWindow, gamestate : GameState) {
    var mousePos = Vector2(0,0)
    var mouseView = Vector2(0.0f, 0.0f)
    var mouseWindow = Vector2(0.0f, 0.0f)

    var leftMouse = false
    var rightMouse = false

    var selectedActor : Option[Actor] = None
    var selectedSecondaryActor : Option[Actor] = None

    def updateEvents() = {
        for event <- window.pollEvent() do
        event match {
            case _: Event.Closed =>
                window.close()
            case Event.MouseButtonPressed(button, x, y) : Event.MouseButtonPressed =>
                if button == Mouse.Button.Left then
                    this.leftMouse = true
                else if button == Mouse.Button.Right then
                    this.rightMouse = true
            case Event.MouseButtonReleased(button, x,y) =>
                if button == Mouse.Button.Left then
                    this.leftMouse = false
                else if button == Mouse.Button.Right then
                    this.rightMouse = false
            //TODO : cas pour bouger la view
            case _ => ()
        }
            
        this.mousePos = Mouse.position(window)
        this.mouseView = window.mapPixelToCoords(mousePos)
        this.mouseWindow = window.mapPixelToCoords(mousePos, this.gamestate.camera.guiView)
    }
    
    def updateClick() = {
        //TODO : faire une liste d'acteurs affichés (pour ne pas parcourir tous les acteurs)
        this.selectedSecondaryActor = None

        this.gamestate.widgets.foreach(_.updateClick(this.mouseWindow, this.leftMouse))

        for actor <- gamestate.actors_list do
            actor.updateClick(this.mouseView, this.leftMouse, this.rightMouse)
        
        for actor <- gamestate.actors_list do
            if this.leftMouse && actor.state == States.PRESSED then
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
            
            
            if this.rightMouse && actor.state == States.HOVER then
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
        for actor <- gamestate.actors_list do
            actor match {
                case player : Player =>
                    this.selectedActor match { case Some(_ : Player) =>
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
                    case _ => ()}

                    //TODO : ça et IA, c'est pour décider l'action, et updateUnit ça fait les actions
                    player.updateUnit()

                case ennemy : Ship if ennemy.team == 1 =>
                    IA(ennemy, gamestate.player)
                    ennemy.updateUnit()

                case ressource : Resource => () //éventuellement faire des ressources mouvantes (comme des astéroides minables)
                case _ => ()
            }
    }

    //TODO : fonction pas encore utilisée. Il faut faire la view.
    def updateView() = {
        this.gamestate.camera.updateView()
        this.gamestate.window.view = Immutable(this.gamestate.camera.playerView)

        //gestion de l'affichage des tilemaps.
        var x = this.gamestate.camera.playerView.center.x
        var y = this.gamestate.camera.playerView.center.y

        for i <- 0 to 7 do
            for j <- 0 to 7 do
                if abs(x - 540 - i * 512) < 540 + 512 && abs(y - 360 - j * 512) < 360 + 512 then
                    this.gamestate.map_array(j)(i) match {
                        case Some(tilemap) => tilemap.loadTexture()
                        case None =>
                            this.gamestate.map_array(j)(i) = Some(new TileMap("maps/purple/purple_" +  i.toString + j.toString + ".png", i, j))
                            this.gamestate.map_array(j)(i).get.loadTexture()
                    }
                else this.gamestate.map_array(j)(i) = None
    }
}