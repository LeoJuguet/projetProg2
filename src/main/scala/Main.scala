import scala.util.Using

import sfml.system.*
import sfml.graphics.*
import sfml.window.*
import sfml.system.*
import gamestate.*
import actor.*

import scala.collection.mutable.ArrayBuffer
import gui.*
import manager.*

import character.*
import gamestate.*
import controller.*
import actor.*
import clickable.*
import ia.*
import event.*
import controller.*
import camera.*
import sfml.Immutable


def game_loop(window: RenderWindow, controller: Controller) : Unit =
    window.clear(Color(0, 0, 0))
    
    while window.isOpen() do
        InputManager.update()
        controller.updateEvents()
        controller.updateClick()
        controller.updateActors()
        controller.updateView()

        //Delete actors destroy
        GameState.actors_list --= GameState.delete_list
        GameState.delete_list.clear()

        GameState.drawGame()


@main def main =
    val width = 1080
    val height = 720

    Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(width, height), "Slower Than Light"))
        val controller = Controller(window)
        GameState.init(window, View(Vector2(0f,0f), Vector2(1080, 720)), View(Vector2(width/2f,height/2f), Vector2(1080, 720)))

        InputManager.init(window)

        var ennemy = Ship(1, 1, Vector2(600, 600))
        var ressource = Resource(Vector2(300, 300))
        
        ressource.texture = TextureManager.get("ore.png")
        ressource.applyTexture()


        GameState.camera.updateBind(ViewBind.ACTOR(GameState.player))

        game_loop(window, controller)


        for actor <- GameState.actors_list do actor.destroy()
    }
