import scala.util.Using

import sfml.system.*
import sfml.graphics.*
import sfml.window.*

import character.*
import gamestate.*

@main def main =
    Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(1024, 768), "Hello world"))

        val gamestate = GameState(window)
        val player = use(Ship(gamestate, 0, 0, Vector2(0, 0)))
        player.textures = "src/main/resources/ovni.png"
        player.loadTexture()

        var mouseWindow = Vector2(0, 0)
        var mouseView = Vector2(0.0f, 0.0f)

        var left_click = false
        var right_click = false

        while window.isOpen() do
            left_click = false
            right_click = false
            for event <- window.pollEvent() do
                event match {
                    case _: Event.Closed => window.closeWindow()
                    case Event.MouseButtonPressed(button, x, y) : Event.MouseButtonPressed =>
                        if button == Mouse.Button.Left then
                            left_click = true
                        else if button == Mouse.Button.Right then
                            right_click = true
                    case _               => ()
                }
            
            mouseWindow = Mouse.position(window)
            mouseView = window.mapPixelToCoords(mouseWindow)
            
            player.targetPosition = mouseView
            //player.moveUnit()
            player.update(mouseView, left_click, right_click)
            
            window.clear(Color(100, 50, 170, 255))

            gamestate.drawGame()
        
        for actor <- gamestate.actors_list do actor.destroy()
    }