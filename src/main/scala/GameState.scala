package gamestate

import sfml.graphics.*
import sfml.window.*
import sfml.system.Vector2
import scala.collection.mutable.ListBuffer

import actor.*
import character.*

/** Provides an interface for generate images
 * @constructor create a new GameState with a window.
 * @param window the RenderWindow
 */
class GameState(var window: RenderWindow)
{
    var actors_list = new ListBuffer[Actor]()
    var player = new Ship(this, 0, 0, Vector2(0, 0))

    private def drawMap() =
      for actor <- actors_list do window.draw(actor)

    /** Draw all the images for the game
     */
    def drawGame() =
      window.clear(Color.Black())
      drawMap()
      window.display()
}