package controller

import sfml.window.*
import sfml.graphics.*
import sfml.system.*

import gamestate.*
import clickable.*
import actor.*
import character.*
import ia.*

class Controller(window : RenderWindow, gamestate : GameState) {
    var mousePos = Vector2(0,0)
    var mouseView = Vector2(0.0f, 0.0f)

    var leftMouse = false
    var rightMouse = false

    var viewPos = Vector2(0.0f, 0.0f)
    //if None then player view
    var viewBind : Option[Actor] = None
    var view = View(viewPos, Vector2(1080, 720))

    var selectedActor : Option[Actor] = None
    var selectedSecondaryActor : Option[Actor] = None

    def updateEvents() = {
        this.leftMouse = false
        this.rightMouse = false
        for event <- window.pollEvent() do
        event match {
            case _: Event.Closed =>
                window.close()
            case Event.MouseButtonPressed(button, x, y) : Event.MouseButtonPressed =>
                if button == Mouse.Button.Left then
                    this.leftMouse = true
                else if button == Mouse.Button.Right then
                    this.rightMouse = true
            //TODO : cas pour bouger la view
            case _ => ()
        }
            
        this.mousePos = Mouse.position(window)
        this.mouseView = window.mapPixelToCoords(mousePos)
    }
    
    def updateClick() = {
        //TODO : faire une liste d'acteurs affichés (pour ne pas parcourir tous les acteurs)
        this.selectedSecondaryActor = None

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
                            player.targetShip = ship
                            player.currentAction = Action.ATTACK
                        case Some(resource : Resource) =>
                            print("player attack resource\n")
                            player.targetResource = resource
                            player.currentAction = Action.MINE
                        case _ => ()
                    }
                    case _ => ()}

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
        viewPos = viewBind match {
            case Some(actor) =>
                actor.position
            case None =>
                gamestate.player.position
        }
        this.view.center = viewPos
        window.view_=(this.view)
    }
}