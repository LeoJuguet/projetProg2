import scala.util.Using

import scala.collection.mutable.ArrayBuffer

import sfml.system.*
import sfml.graphics.*
import sfml.window.*
import sfml.system.*

import gamestate.*
import actor.*
import gui.*
import manager.*
import gamestate.*
import actor.*
import clickable.*
import controller.*
import event.InputManager
import ship.matchingFactor
import ship.{CapitalShip, Drone, Base}


def game_loop(window: RenderWindow) : Unit =
    window.clear(Color(0, 0, 0))
    
    while window.isOpen() do {
        InputManager.update()

        PlayerController.updateActors()
        IAController.updateActors()

        Camera.updateView()

        GameState.actors_list --= GameState.delete_list
        GameState.clearDeleteList()

        GameState.drawGame()
    }


@main def main =
    val width = 1080
    val height = 720

    Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(width, height), "Slower Than Light"))

        GameState.init(window, View(Vector2(0f,0f), Vector2(1080, 720)), View(Vector2(width/2f,height/2f), Vector2(1080, 720)))
        InputManager.init(window)

        game_loop(window)
    }.get
