package gamestate

import sfml.graphics.*
import sfml.window.*
import sfml.system.Vector2
import scala.collection.mutable.ListBuffer

import actor.*
import character.*
import controller.*
import sfml.Immutable
import gui.{Widget, DemoWidget}
import manager.FontManager

/** Provides an interface for generate images
 * @constructor create a new GameState with a window.
 * @param window the RenderWindow
 */
object GameState
{
    var window : RenderWindow = _
    var view : View = _
    var windowView : View = _
    var actors_list = new ListBuffer[Actor]()
    var delete_list = new ListBuffer[Actor]()

    var player : Ship = new Ship(new Controller(this.window), 0, 0, Vector2(0,0))
    var widgets = new ListBuffer[Widget]()

    this.widgets += DemoWidget(window)


    def init(window: RenderWindow, view: View, windowView : View)={
      this.window = window
      this.view = view
      this.windowView = windowView
    }

    var font = FontManager.get("game_over.ttf")

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
      map_list.foreach(window.draw(_))

    private def drawActors() =
      actors_list.foreach(window.draw(_))

    private def drawWidget()=
      window.view = Immutable(this.windowView)
      this.textPlayerResources.string = this.player.scrap.toString
      this.textPlayerLife.string = this.player.health.toString
      window.draw(this.textPlayerResources)
      window.draw(this.textPlayerLife)
      widgets.foreach(window.draw(_))


    /** Draw all the images for the game
     */
    def drawGame() =
      window.clear(Color.Black())
      drawMap()
      drawActors()
      drawWidget()
      window.display()
      this.window.view = Immutable(this.view)
}

