import sfml.graphics.*
import sfml.window.*

import World.*
import Ship.*
import Weapon.*
import Menu.*

class Game:
    var settings : Settings
    var universe : Universe


def menu_window(window: RenderWindow, settings: Settings):
    state = "main_menu"
    continue = true
    while continue do
        if state == "main_menu" then
            for event <- window.pollEvent() do
            event match {
                case _: Event.Closed =>
                    window.closeWindow()
                    continue = false
                    "exit"
                /* TODO : cliquer sur les boutons */
                case _ => state = "option"
                case _ => state = "credits"
                case _ => state = "new_game"
                case _ => state = "load_game" /* ne sera pas implémenté pour raison de flemme */
                case _ => ()
            }
            draw(main_menu + boutons)
        else if state == "option" then
            for event <- window.pollEvent() do
            event match {
                case _: Event.Closed =>
                    window.closeWindow()
                    continue = false
                    "exit"
                case _ => state = "main_menu"
                case _ => settings.volume -- /* TODO : changer le volume */
                case _ => settings.fullscreen = !settings.fullscreen
                case _ => settings.resolution = (1920, 1080) /* TODO : changer la résolution */
                case _ => ()
            }
            draw(option_menu + boutons)
        else if state == "credits" then
            for event <- window.pollEvent() do
            event match {
                case _: Event.Closed =>
                    window.closeWindow()
                    continue = false
                    "exit"
                case _ => state = "main_menu"
                case _ => ()
            }
            draw(credits + boutons)
        else if state == "new_game" then
            for event <- window.pollEvent() do
            event match {
                case _: Event.Closed =>
                    window.closeWindow()
                    continue = false
                    "exit"
                case _ =>
                    continue = false
                    "game"
                case _ => ()
            }
        else if state == "load_game" then
            for event <- window.pollEvent() do
            event match {
                case _: Event.Closed =>
                    window.closeWindow()
                    continue = false
                    "exit"
                case _ => ()
            }
            state = "main_menu"

def game_window(window: RenderWindow, game: Game):
    state = "running"
    continue = true
    while continue do
        if state == "running" then
            for event <- window.pollEvent() do
            event match {
                case _: Event.Closed =>
                    window.closeWindow()
                    continue = false
                    "exit"
                case _ => state = "pause"
                case _ => ()
            }
            draw(game)
        else if state == "pause" then
            for event <- window.pollEvent() do
            event match {
                case _: Event.Closed =>
                    window.closeWindow()
                    continue = false
                    "exit"
                case _ => state = "running"
                case _ =>
                    continue = false
                    "main_menu"
                case _ => ()
            }

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

        val state = "menu"

        while window.isOpen() do
            if state == "menu" then
                /* afficher le menu */
                /* attendre un input */
                /* si input, alors state = "game" */
                state = menu_window(window, settings)
            else if state == "game" then
                /* afficher le jeu */
                /* attendre un input */
                /* si input, alors state = "menu" */
                state = game_window(window, game)
            else
                /* afficher un message d'erreur */
                ()
            /*t = time*/

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
    }