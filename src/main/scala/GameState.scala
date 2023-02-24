package gamestate

import sfml.graphics.*
import sfml.window.*
import scala.collection.mutable.ListBuffer
import actor.*

/** Provides an interface for generate images
  *
  * @constructor
  *   create a new GameState with a window.
  * @param window
  *   the RenderWindow
  */
class GameState(var window: RenderWindow):
  var actors_list = new ListBuffer[Actor]()

  private def drawMap() =
    for actor <- actors_list do window.draw(actor)

  /** Draw all the images for the game
    */
  def drawGame() =
    window.clear(Color.Black())
    drawMap()
    window.display()

end GameState
