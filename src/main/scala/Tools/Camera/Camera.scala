package controller

import scala.math.abs

import sfml.graphics.View
import sfml.system.Vector2
import sfml.window.Keyboard.Key
import sfml.Immutable

import actor.Actor
import event.KeyboardState
import gamestate.GameState
import tilemap.TileMap

enum ViewBind {
    case POINT (point : Vector2[Float])
    case ACTOR (actor : Actor)
}

//The camera class is used to display the game from a certain point of view.
//This point can be fixed either on the background and dragged by the player, or follow an actor.
object Camera {
    var playerView = View(Vector2(540f, 360f), Vector2(1080, 720))
    var backgroundView = View(Vector2(270f, 180), Vector2(1080, 720))
    var guiView = View(Vector2(540f, 360f), Vector2(1080, 720))
    
    var viewBind : ViewBind = ViewBind.POINT(Vector2(540f, 360f))

    def moveViewBind(delta : Vector2[Float]) : Unit =
        this.viewBind = this.viewBind match {
            case ViewBind.POINT(point) => ViewBind.POINT(point + delta)
            case ViewBind.ACTOR(actor) => ViewBind.POINT(actor.position + delta)
        }
    
    //TODO : account for potential zoom
    def updatePlayerView() = {
        if KeyboardState.is_Press(Key.KeyUp) then
            this.moveViewBind(Vector2(0, -10))
        if KeyboardState.is_Press(Key.KeyDown) then
            this.moveViewBind(Vector2(0, 10))
        if KeyboardState.is_Press(Key.KeyLeft) then
            this.moveViewBind(Vector2(-10, 0))
        if KeyboardState.is_Press(Key.KeyRight) then
            this.moveViewBind(Vector2(10, 0))
            
        this.playerView.center = viewBind match {
            case ViewBind.POINT(point) => point
            case ViewBind.ACTOR(actor) => actor.position
        }

        this.backgroundView.center = Vector2(this.playerView.center.x / 2, this.playerView.center.y / 2)

        //window.view = Immutable(this.view)
    }

    def updateBind(newBind : Actor) = {
        this.viewBind = ViewBind.ACTOR(newBind)
    }
    def updateBind(newBind : Vector2[Float]) = {
        this.viewBind = ViewBind.POINT(newBind)
    }

    def updateView() = {
        this.updatePlayerView()
        GameState.window.view = Immutable(this.playerView)
        
        //gestion de l'affichage des tilemaps.
        var x = this.playerView.center.x / 2
        var y = this.playerView.center.y / 2

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
                //TODO : ça marche pas
                //else
                //    GameState.map_array(j)(i) = None
    }
}
