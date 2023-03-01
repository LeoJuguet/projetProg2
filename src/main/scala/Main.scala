import scala.util.Using

import sfml.system.*
import sfml.graphics.*
import sfml.window.*

import character.*
import gamestate.*
import controller.*
import actor.*
import clickable.*
import ia.*
import controller.*

def game_window(window: RenderWindow, gamestate: GameState) : Unit =
    var continue = true
    var controller = Controller(window, gamestate)

    window.clear(Color(0, 0, 0))

    //TODO : la prochaine chose à faire est de centrer la vision sur le vaisseau, et de se déplacer sur l'image de fond.
    
    while window.isOpen() do
        controller.updateEvents()
        controller.updateClick()
        controller.updateActors()
        //controller.updateView()

        gamestate.drawGame()


@main def main =
    val height = 720
    val width = 1080

    Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(width, height), "Slower Than Light"))

        val gamestate = GameState(window)
        val controller = Controller(window, gamestate)
        val player = Player(gamestate, controller, 0, 0, Vector2(0, 0))
        var ennemy = Ship(gamestate, controller, 1, 1, Vector2(600, 600))
        var ressource = Resource(gamestate, controller, 0, Vector2(300, 300))
        
        player.textures = "src/main/resources/ovni.png"
        player.loadTexture()

        ennemy.textures = "src/main/resources/ovni.png"
        ennemy.loadTexture()

        ressource.textures = "src/main/resources/ore.png"
        ressource.loadTexture()

        gamestate.player = player

        while window.isOpen() do
            game_window(window, gamestate)

            window.display()
        
        for actor <- gamestate.actors_list do actor.destroy()

    }
