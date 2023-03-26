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
import sfml.Immutable


def game_loop(window: RenderWindow) : Unit =
    var continue = true
    var controller = Controller(window)

    window.clear(Color(0, 0, 0))

    //TODO : la prochaine chose à faire est de centrer la vision sur le vaisseau, et de se déplacer sur l'image de fond.
    
    while window.isOpen() do
        InputManager.update()
        controller.updateEvents()
        controller.updateClick()
        controller.updateActors()
        controller.updateView()

        //Delete actors
        GameState.actors_list --= GameState.delete_list
        GameState.delete_list.clear()

        GameState.drawGame()


@main def main =
    val width = 1080
    val height = 720

    Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(width, height), "Slower Than Light"))
        val controller = Controller(window)

        GameState.init(window, View(Vector2(0f,0f), Vector2(1080, 720)),View(Vector2(width/2f,height/2f), Vector2(1080, 720)))
        //window.view = Immutable(controller.view)
        InputManager.init(window)


        var map_texture = TextureManager.get("maps/purple/purple_00.png")
        var map_sprite = Sprite(map_texture)
        GameState.map_list += map_sprite

        val player = Player(controller, 0, 0, Vector2(0, 0))
        var ennemy = Ship(controller, 1, 1, Vector2(600, 600))
        var ressource = Resource(controller, 0, Vector2(300, 300))
        
        ressource.texture = TextureManager.get("ore.png")
        ressource.applyTexture()

        GameState.player = player

        while window.isOpen() do
            game_loop(window)

            window.display()
        
        for actor <- GameState.actors_list do actor.destroy()
    }
