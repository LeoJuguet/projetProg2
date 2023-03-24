package gamestate

import sfml.graphics.*
import sfml.window.*
import sfml.system.Vector2
import scala.collection.mutable.ListBuffer

import actor.*
import character.*
import controller.*
import tilemap.*
import sfml.Immutable
import gui.{Widget, DemoWidget}
import camera.*

/** Provides an interface for generate images
 * @constructor create a new GameState with a window.
 * @param window the RenderWindow
 */
class GameState(var window: RenderWindow)
{
    var actors_list = new ListBuffer[Actor]()
    var player : Ship = new Player(this, 0, 0, Vector2(0,0))
    var camera : Camera = new Camera(this)
    var widgets = new ListBuffer[Widget]()

    this.widgets += DemoWidget(window)

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

    var map_array = Array.ofDim[Option[TileMap]](8,8)

    for i <- 0 to 7 do
      for j <- 0 to 7 do
        map_array(i)(j) = None

    private def drawMap() =
      for i <- 0 to 7 do
        for j <- 0 to 7 do
          var map = map_array(i)(j)
          map match {
            case Some(mappe) => print("drawing " + i.toString + j.toString + "..."); window.draw(mappe); print("done\n")
            case None => ()
          }

      for actor <- actors_list do window.draw(actor)

    private def drawWidget()=
      window.view = Immutable(camera.guiView)
      this.textPlayerResources.string = this.player.scrap.toString
      this.textPlayerLife.string = this.player.health.toString
      window.draw(this.textPlayerResources)
      window.draw(this.textPlayerLife)
      widgets.foreach(window.draw(_))
      window.view = Immutable(camera.playerView)


    /** Draw all the images for the game
     */
    def drawGame() =
      window.clear(Color.Black())
      drawMap()
      drawWidget()
      window.display()
}

