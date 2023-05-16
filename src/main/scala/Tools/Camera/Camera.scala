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
import event.OnMouseWheelScrolled
import sfml.window.Mouse.Wheel

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

    var zoomSensitivity = 0.1f


    var viewBind : ViewBind = ViewBind.POINT(Vector2(540f, 360f))


    OnMouseWheelScrolled.connect((wheel,delta,x,y) =>
        {
            if wheel == Wheel.VerticalWheel then
                /* zoom = 1 : size unchanged
                 * zoom > 1 : view bigger = zoom
                 * zoom < 1 : view smaller = unzoom
                 * */
                var zoom = 1 - delta * this.zoomSensitivity
                playerView.zoom(zoom)
                backgroundView.zoom(zoom)
        }
    )

    def moveViewBind(delta : Vector2[Float]) : Unit =
        this.viewBind = this.viewBind match {
            case ViewBind.POINT(point) => ViewBind.POINT(point + delta)
            case ViewBind.ACTOR(actor) => ViewBind.POINT(actor.position + delta)
        }

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
        //due to some forgotten black magic, the coordinates have to be flipped ...
        var x = this.playerView.center.y / 2
        var y = this.playerView.center.x / 2

        for i <- 0 to 7 do
            for j <- 0 to 7 do
                //On ne veut pas la distance entre le centre de la tilemap et le centre de la vue, mais la distance entre le centre de la tilemap et le bord de la vue.
                // Account for the zoom
                var x_size = this.playerView.size.x / 1.5
                var y_size = this.playerView.size.y / 1.5

                if abs(x - 540 - i * 512) < x_size + 512 && abs(y - 360 - j * 512) < y_size + 512 then
                    GameState.map_array(j)(i) match {
                        case Some(tilemap) => tilemap.loadTexture()
                        case None =>
                            GameState.map_array(j)(i) = Some(new TileMap("maps/purple/purple_" +  i.toString + j.toString + ".png", i, j))
                            GameState.map_array(j)(i).get.loadTexture()
                    }
    }
}
