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
import controller.*
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
        val gamestate = GameState(window, View(Vector2(0f,0f), Vector2(1080, 720)),View(Vector2(width/2f,height/2f), Vector2(1080, 720)))
        val controller = Controller(window, gamestate)

        //window.view = Immutable(controller.view)

        var map_name = "src/main/resources/maps/purple/purple_00.png"
        var map_texture = Texture()
        map_texture.loadFromFile(map_name)
        var map_sprite = Sprite(map_texture)
        gamestate.map_list += map_sprite

        val player = Player(gamestate, controller, 0, 0, Vector2(0, 0))
        var ennemy = Ship(gamestate, controller, 1, 1, Vector2(600, 600))
        var ressource = Resource(gamestate, controller, 0, Vector2(300, 300))
        
        player.textures = "ovni.png"
        player.loadTexture()
        ennemy.textures = "ovni.png"
        ennemy.loadTexture()
        ressource.textures = "ore.png"
        ressource.loadTexture()

        gamestate.player = player

        while window.isOpen() do
            game_window(window, gamestate)

            window.display()
        
        for actor <- gamestate.actors_list do actor.destroy()
    }
