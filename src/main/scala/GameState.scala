package gamestate

import sfml.graphics.*
import sfml.window.*
import sfml.system.Vector2
import scala.collection.mutable.ListBuffer
import sfml.Immutable

import actor.*
import character.*
import tilemap.*
import gui.{Widget, DemoWidget}
import manager.FontManager
import camera.*
import controller.PlayerController

import ship.Drone

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

    //TODO : integrate those lists properly (ex : delete)
    var player_actors_list = ListBuffer[Actor]()
    var enemy_actors_list = ListBuffer[Actor]()

    var delete_list = new ListBuffer[Actor]()
    var camera : Camera = new Camera
    var widgets = new ListBuffer[Widget]()

    this.widgets += DemoWidget(window)

    def init(window: RenderWindow, view: View, windowView : View)={
      this.window = window
      this.view = view
      this.windowView = windowView
    }

    var font = FontManager.get("game_over.ttf")

    //this is for the demo. It will be removed later.
    print("Creating player")
    var player = this.createDrone(0, Vector2(0, 0))
    var ennemy = this.createDrone(1, Vector2(100, 100))
    print("Player created")

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

    //Création d'une table vide qui contiendra les TileMap, des bouts de la map chargés dynamiquement en fonction de la position de la vue.
    var map_array = Array.ofDim[Option[TileMap]](8,8)

    for i <- 0 to 7 do
      for j <- 0 to 7 do
        map_array(i)(j) = None

    private def drawMap() =
      //affichage de la map
      for i <- 0 to 7 do
        for j <- 0 to 7 do
          var map = map_array(i)(j)
          map match {
            case Some(tilemap) => window.draw(tilemap)
            case None => ()
          }

    private def drawActors() =
      actors_list.foreach(window.draw(_))

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
      drawActors()
      drawWidget()
      window.display()

    def createDrone(teamID : Int, initialPosition: Vector2[Float]) : Drone = {
        //create a new drone and add it to the right team.
        var drone = new Drone(teamID, initialPosition)
        this.actors_list += drone
        if teamID == 0 then
            this.player_actors_list += drone
        else
            this.enemy_actors_list += drone

        //create the connections to update the selection when the drone is clicked.
        var c1 = drone.onPressed.connect(Unit => {
            PlayerController.selectedUnits += drone
        })
        var c2 = drone.onTargeted.connect(Unit => {
            PlayerController.selectedTargets += drone
        })
        var c3 = drone.onReleased.connect(Unit => {
            PlayerController.selectedUnits -= drone
            PlayerController.selectedTargets -= drone
        })
        //when the drone is destroyed, we remove it from the lists and disconnect the connections.
        //TODO : verify that removing the drone from the lists doesn't cause any problem as it can happen at any time.
        drone.onDestroyed.connect(Unit => {
            this.player_actors_list -= drone
            this.enemy_actors_list -= drone
            this.actors_list -= drone
            PlayerController.selectedUnits -= drone
            PlayerController.selectedTargets -= drone
            c1.disconnect()
            c2.disconnect()
            c3.disconnect()
        })
        drone
    }
}

