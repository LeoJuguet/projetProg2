package gamestate

import sfml.graphics.*
import sfml.window.*
import sfml.system.Vector2
import scala.collection.mutable.ListBuffer

import actor.*
import character.*
import controller.*

/** Provides an interface for generate images
 * @constructor create a new GameState with a window.
 * @param window the RenderWindow
 */
class GameState(var window: RenderWindow)
{
    var actors_list = new ListBuffer[Actor]()
    var player : Ship = new Ship(this, new Controller(this.window, this), 0, 0, Vector2(0,0))

    var font = Font()

    this.font.loadFromFile("src/main/resources/fonts/game_over.ttf")

    var textPlayerLife = new Text()
    this.textPlayerLife.position = (50,50)
    this.textPlayerLife.characterSize = 50
    this.textPlayerLife.font = this.font
    this.textPlayerLife.string = this.player.health.toString


    var textPlayerResources = new Text()
    this.textPlayerResources.position = (50,100)
    this.textPlayerResources.characterSize = 50
    this.textPlayerResources.font = this.font
    this.textPlayerResources.string = this.player.scrap.toString

    var map_list = ListBuffer[Sprite]()

    private def drawMap() =
      for map <- map_list do window.draw(map)
      for actor <- actors_list do window.draw(actor)

    private def drawWidget()=
      this.textPlayerResources.string = this.player.scrap.toString
      this.textPlayerLife.string = this.player.health.toString
      window.draw(this.textPlayerResources)
      window.draw(this.textPlayerLife)


    /** Draw all the images for the game
     */
    def drawGame() =
      window.clear(Color.Black())
      drawMap()
      drawWidget()
      window.display()
}

