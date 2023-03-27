package camera

import sfml.graphics.*
import sfml.system.*

import actor.*

enum ViewBind {
    case POINT (point : Vector2[Float])
    case ACTOR (actor : Actor)
}

//The camera class is used to display the game from a certain point of view.
//This point can be fixed either on the background and dragged by the player, or follow an actor.
class Camera{
    var playerView = View(Vector2(540f, 360f), Vector2(1080, 720))
    var guiView = View(Vector2(540f, 360f), Vector2(1080, 720))
    
    var viewBind : ViewBind = ViewBind.POINT(Vector2(540f, 360f))
    
    //TODO : account for potential zoom
    def updateView() = {
        this.playerView.center = viewBind match {
            case ViewBind.POINT(point) => point
            case ViewBind.ACTOR(actor) => actor.position
        }
        //window.view = Immutable(this.view)
    }

    def updateBind(newBind : ViewBind) = {
        this.viewBind = newBind
    }
}
