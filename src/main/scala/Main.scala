import sfml.graphics.*
import sfml.window.*

import World.*
import Ship.*
import Weapon.*
import Menu.*

class Game:
    var settings : Settings
    var universe : Universe


@main def main =
    height = 720
    width = 1280

    var settings = Settings()
    var universe = Universe()
    var player = init_player()

    univers.player = player
    univers.ships = player :: univers.ships

    /* faire du perlin noise */
    univers.asteroids = create_asteroids(univers)
    univers.ennemies = create_ennemies(univers)
    univers.allies = create_allies(univers)

    univers.ships = univers.ships ++ univers.ennemies ++ univers.allies

    player.pos = (width / 2, height / 2)

    var game = Game()
    game.settings = settings
    game.universe = universe

    scala.util.Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(width, height), "Hello world"))

        /*
        val circle = use(sf::CircleShape(100))
        circle.setPosition(Vector2f(640, 360))
        circle.setFillColor(Color.Green())
        circle.setSize(Vector2f(100, 100))*/

        val texture = use(Texture())
        texture.loadFromFile("src/main/resources/sfml-logo.png")

        val sprite = use(Sprite(texture))
        
        window.clear(Color.Black())
        window.draw(sprite)
        /*window.draw(circle)*/
        window.display()

        val frame = 0
        val id = 0

        while window.isOpen() do
            /*t = time*/
            if frame % 60 == 0 then
                id += 1
                create_ennemy(world, id)

            for event <- window.pollEvent() do
                event match {
                    case _: Event.Closed => window.closeWindow()
                    /* TODO : take the actions */
                    case _ => ()
                }
            
            for ennemy <- world.ennemies do
                /* call ennemy IA depending on its name */
                /* si pas target, alors target ? */
                /* si target, alors move ou attaque */

                ()
            /*wait max 0 time - t - 1/60s*/
            frame += 1
    }