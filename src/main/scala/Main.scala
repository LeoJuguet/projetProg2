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

import ship.Ship
import resource.Resource
import gamestate.*
import controller.*
import actor.*
import clickable.*
import ia.*
import event.*
import controller.*
import camera.*
import sfml.Immutable


def game_loop(window: RenderWindow, controller: PlayerController) : Unit =
    window.clear(Color(0, 0, 0))
    
    while window.isOpen() do
        InputManager.update()
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
        val controller = PlayerController()
        GameState.init(window, View(Vector2(0f,0f), Vector2(1080, 720)), View(Vector2(width/2f,height/2f), Vector2(1080, 720)))

        InputManager.init(window)

        var resource = Resource(Vector2(300, 300))
        
        resource.texture = TextureManager.get("ore.png")
        resource.applyTexture()


        GameState.camera.updateBind(ViewBind.ACTOR(GameState.player))

        game_loop(window, controller)


        for actor <- GameState.actors_list do actor.destroy()
    }
