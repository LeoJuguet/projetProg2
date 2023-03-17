import scala.util.Using

import sfml.system.*
import sfml.graphics.*
import sfml.window.*
import sfml.system.*
import gamestate.*
import actor.*

import scala.collection.mutable.ArrayBuffer
import gui.*

import character.*
import gamestate.*
import controller.*
import actor.*
import clickable.*
import ia.*
import controller.*
import camera.*
import sfml.Immutable

def game_window(window: RenderWindow, gamestate: GameState) : Unit =
    var continue = true
    var controller = Controller(window, gamestate)

    window.clear(Color(0, 0, 0))

    //TODO : la prochaine chose à faire est de centrer la vision sur le vaisseau, et de se déplacer sur l'image de fond.
    
    while window.isOpen() do
        controller.updateEvents()
        controller.updateClick()
        controller.updateActors()
        controller.updateView()

        gamestate.drawGame()


@main def main =
    val width = 1080
    val height = 720

    Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(width, height), "Slower Than Light"))
        val gamestate = GameState(window)
        val controller = Controller(window, gamestate)

        //window.view = Immutable(controller.view)

        var player = Player(gamestate, controller, 0, 0, Vector2(0, 0))
        var ennemy = Ship(gamestate, controller, 1, 1, Vector2(600, 600))
        var ressource = Resource(gamestate, controller, 0, Vector2(300, 300))
        
        gamestate.player = player
        gamestate.player.textures = "src/main/resources/ovni.png"
        gamestate.player.loadTexture()
        
        gamestate.camera.viewBind = ViewBind.ACTOR(gamestate.player)

        ennemy.textures = "src/main/resources/ovni.png"
        ennemy.loadTexture()
        gamestate.player.controller = controller

        ressource.textures = "src/main/resources/ore.png"
        ressource.loadTexture()

        while window.isOpen() do
            game_window(window, gamestate)

            window.display()
        
        for actor <- gamestate.actors_list do actor.destroy()
    }
