import scala.util.Using

import sfml.system.*
import sfml.graphics.*
import sfml.window.*

import character.*
import gamestate.*
import actor.*
import clickable.*
import ia.*

def game_window(window: RenderWindow, gamestate: GameState) : Unit =
    var state = "running"
    var continue = true

    var mouseWindow = Vector2(0, 0)
    var mouseView = Vector2(0.0f, 0.0f)

    var left_click = false
    var right_click = false
    while continue do
        left_click = false
        right_click = false
        for event <- window.pollEvent() do
        event match {
            case _: Event.Closed =>
                window.closeWindow()
                continue = false
            case Event.MouseButtonPressed(button, x, y) : Event.MouseButtonPressed =>
                if button == Mouse.Button.Left then
                    left_click = true
                else if button == Mouse.Button.Right then
                    right_click = true
            case _ => ()
            case _ =>
                state = "pause"
        }

        mouseWindow = Mouse.position(window)
        mouseView = window.mapPixelToCoords(mouseWindow)

        for actor <- gamestate.actors_list do
            actor match {
                case ennemy : Ship  if ennemy.team == 1 =>
                    IA(ennemy, gamestate.player)
                    ennemy.update(mouseView, left_click, right_click)
                    ennemy.moveUnit()
                    if ennemy.sprite.globalBounds.contains(mouseView) && right_click && gamestate.player.state == States.PRESSED then
                        //gamestate.player.target = ennemy
                        ennemy.destroy()
                case _ => ()
            }
        
        gamestate.player.update(mouseView, left_click, right_click)
        gamestate.player.moveUnit()

        gamestate.drawGame()


@main def main =
    val height = 720
    val width = 1280

    Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(1024, 768), "Hello world"))

        val gamestate = GameState(window)
        val player = use(Player(gamestate, 0, 0, Vector2(0, 0)))
        var ennemy = use(Ship(gamestate, 1, 1, Vector2(600, 600)))

        print(ennemy.team)
        
        player.textures = "src/main/resources/ovni.png"
        player.loadTexture()

        ennemy.textures = "src/main/resources/ovni.png"
        ennemy.loadTexture()

        gamestate.player = player

        while window.isOpen() do
            game_window(window, gamestate)

            window.display()
        
        for actor <- gamestate.actors_list do actor.destroy()
    }