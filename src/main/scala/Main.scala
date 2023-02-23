import scala.util.Using

import sfml.system.*
import sfml.graphics.*
import sfml.window.*
import gamestate.*
import actor.*



import character.*
import gamestate.*
import actor.*
import clickable.*
import ia.*

def game_window(window: RenderWindow, gamestate: GameState) : Unit =
    var continue = true

    var mouseWindow = Vector2(0, 0)
    var mouseView = Vector2(0.0f, 0.0f)

    var left_click = false
    var right_click = false
    //TODO : la prochaine chose à faire est de centrer la vision sur le vaisseau, et de se déplacer sur l'image de fond.
    while continue do
        left_click = false
        right_click = false
        for event <- window.pollEvent() do
        event match {
            case _: Event.Closed =>
                window.close()
                continue = false
            case Event.MouseButtonPressed(button, x, y) : Event.MouseButtonPressed =>
                if button == Mouse.Button.Left then
                    left_click = true
                else if button == Mouse.Button.Right then
                    right_click = true
            case _ => ()
        }

        mouseWindow = Mouse.position(window)
        mouseView = window.mapPixelToCoords(mouseWindow)

        for actor <- gamestate.actors_list do
            actor match {
                case ennemy : Ship if ennemy.team == 1 =>
                    IA(ennemy, gamestate.player)
                    ennemy.updateClick(mouseView, left_click, right_click)
                    ennemy.updateUnit()
                    if ennemy.transform.transformRect(ennemy.sprite.globalBounds).contains(mouseView) && right_click && gamestate.player.state == States.PRESSED then
                        gamestate.player.targetShip = ennemy
                        gamestate.player.currentAction = Action.ATTACK

                case ressource : Resource =>
                    ressource.updateClick(mouseView, left_click, right_click)
                    if ressource.transform.transformRect(ressource.sprite.globalBounds).contains(mouseView) && right_click && gamestate.player.state == States.PRESSED then
                        gamestate.player.targetResource = ressource
                        gamestate.player.currentAction = Action.MINE
                case _ => ()
            }
        
        gamestate.player.updateClick(mouseView, left_click, right_click)
        gamestate.player.updateUnit()

        gamestate.drawGame()


@main def main =
    val height = 720
    val width = 1080

    Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(width, height), "Slower Than Light"))

        val gamestate = GameState(window)
        val player = Player(gamestate, 0, 0, Vector2(0, 0))
        var ennemy = Ship(gamestate, 1, 1, Vector2(600, 600))
        var ressource = Resource(gamestate, 0, Vector2(300, 300))
        
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
