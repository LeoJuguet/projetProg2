package gamestate

import scala.collection.mutable.ListBuffer

import sfml.graphics.*
import sfml.system.Vector2
import sfml.Immutable

import actor.Actor
import tilemap.TileMap
import gui.{Widget, DemoWidget}
import manager.FontManager
import controller.Camera
import ship.{Drone, Base}
import controller.PlayerController
import resource.*

/** Provides an interface for generate images
 * @constructor create a new GameState with a window.
 * @param window the RenderWindow
 */
object GameState {
    var window : RenderWindow = _
    var view : View = _
    var windowView : View = _
    var actors_list = new ListBuffer[Actor]()

    var player_actors_list = ListBuffer[Actor]()
    var enemy_actors_list = ListBuffer[Actor]()

    var resources_list = new ListBuffer[Resource]()

    var delete_list = new ListBuffer[Actor]()
    var widgets = new ListBuffer[Widget]()

    this.widgets += DemoWidget(window)

    def init(window: RenderWindow, view: View, windowView : View)={
      this.window = window
      this.view = view
      this.windowView = windowView
    }

    var font = FontManager.get("game_over.ttf")

    //this is for the demo. It will be removed later.
    var player = this.createDrone(0, Vector2(0, 0))
    var ennemy = this.createDrone(1, Vector2(100, 100))
    var resource = new Resource(Vector2(300, 300))

    var playerBase = this.createBase(0, Vector2(500, 500))
    var ennemyBase = this.createBase(1, Vector2(1000, 1000))

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
      window.view = Immutable(Camera.backgroundView)
      for i <- 0 to 7 do
        for j <- 0 to 7 do
          var map = map_array(i)(j)
          map match {
            case Some(tilemap) => window.draw(tilemap)
            case None => ()
          }
      window.view = Immutable(Camera.playerView)

    private def drawActors() =
      actors_list.foreach(window.draw(_))

    private def drawWidget()=
      window.view = Immutable(Camera.guiView)
      this.textPlayerResources.string = this.player.scrap.toString
      this.textPlayerLife.string = this.player.health.toString
      window.draw(this.textPlayerResources)
      window.draw(this.textPlayerLife)
      widgets.foreach(window.draw(_))
      window.view = Immutable(Camera.playerView)


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

    def createBase(teamID : Int, position: Vector2[Float]) : Base = {
        //create a new base and add it to the right team.
        var base = new Base(teamID, position)
        this.actors_list += base
        if teamID == 0 then
            this.player_actors_list += base
        else
            this.enemy_actors_list += base
        
        //create the connections to update the selection when the base is clicked.
        var c2 = base.onTargeted.connect(Unit => {
            PlayerController.selectedTargets += base
        })
        var c3 = base.onReleased.connect(Unit => {
            PlayerController.selectedTargets -= base
        })
        //when the base is destroyed, we remove it from the lists and disconnect the connections.
        base.onDestroyed.connect(Unit => {
            this.player_actors_list -= base
            this.enemy_actors_list -= base
            this.actors_list -= base
            PlayerController.selectedUnits -= base
            PlayerController.selectedTargets -= base
            c2.disconnect()
            c3.disconnect()
        })
        base
    }

    def createResource(typ: String, position: Vector2[Float]) : Resource = {
        var resource = typ match {
          case "scrap" => new Scrap(position)
          case "cooper" => new Cooper(position)
          case "iron" => new Iron(position)
          case "uraniun" => new Uranium(position)
          case "ethereum" => new Ethereum(position)
        }
        this.actors_list += resource
        this.resources_list += resource
        
        var c2 = resource.onTargeted.connect(Unit => {
            PlayerController.selectedTargets += resource
        })
        var c3 = resource.onReleased.connect(Unit => {
            PlayerController.selectedTargets -= resource
        })
        resource.onDestroyed.connect(Unit => {
            this.actors_list -= resource
            this.resources_list -= resource
            PlayerController.selectedTargets -= resource
            c2.disconnect()
            c3.disconnect()
        })
        resource
    }
}

