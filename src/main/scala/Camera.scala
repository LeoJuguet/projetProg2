package camera

import sfml.graphics.*
import sfml.system.*

import gamestate.*
import actor.*

enum ViewBind {
    case POINT (point : Vector2[Float])
    case ACTOR (actor : Actor)
}

class Camera(gamestate : GameState) {
    var playerView = View(Vector2(540f, 360f), Vector2(1080, 720))
    var guiView = View(Vector2(540f, 360f), Vector2(1080, 720))
    
    var viewBind : ViewBind = ViewBind.POINT(Vector2(540f, 360f))
    
    def updateView() = {
        this.playerView.center = viewBind match {
            case ViewBind.POINT(point) => point
            case ViewBind.ACTOR(actor) => actor.position
        }
        //window.view = Immutable(this.view)
    }
}