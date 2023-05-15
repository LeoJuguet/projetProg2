package gamestate

import scala.collection.mutable.ListBuffer
import scala.util.Random

import sfml.graphics.*
import sfml.system.Vector2
import sfml.Immutable

import actor.Actor
import tilemap.TileMap
import gui.{Widget, DemoWidget}
import manager.FontManager
import controller.Camera
import ship.{Drone, Base, DroneStats}
import controller.PlayerController
import controller.IAController
import asteroid.*
import perlin.generateField
import ship.CapitalShip

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

    //those lists are used to check collisions between actors
    var playerCollisionList = new ListBuffer[Actor]()
    var enemyCollisionList = new ListBuffer[Actor]()

    var delete_list = new ListBuffer[Actor]()
    var widgets = new ListBuffer[Widget]()


    //Uncomment this for show WidgetDemo
    //this.widgets += DemoWidget(window)

    def init(window: RenderWindow, view: View, windowView : View)={
        /*
         * The definitive code for this init fuction is delimited by "/*********************/"
         * All the code below is for testing and demonstration purpose
         */
        /*********************/
        //initialise the window
        this.window = window
        this.view = view
        this.windowView = windowView

        /*
         TODO : To match the map size, the noise size should be of 256. The problem is that there would then be more than 12k asteroids, which is too much for the moment.
                Maybe when better data structures are implemented, it will be possible to have more asteroids.
        
        var map_size = 32384
        var noise_size = 64

        var asteroids = generateField(noise_size, 1.2, 1, 0.02)
        var cooper = generateField(noise_size, 1.2, 1, 0.06)
        var iron = generateField(noise_size, 1.2, 1, 0.05)
        var uranium = generateField(noise_size, 1.2, 1, 0.03)

        //initial placement of the resources
        var min_x = 0
        var min_y = 0
        var max_x = 0
        var max_y = 0
        for i <- 0 to noise_size - 1 do
            for j <- 0 to noise_size - 1 do
                if asteroids(0)(i)(j) == 0.0 then
                    min_x = i
                    min_y = j
                else if asteroids(0)(i)(j) == 1.0 then
                    max_x = i
                    max_y = j

                if asteroids(1)(i)(j) then
                    if uranium(1)(i)(j) then
                        this.createResource("uranium", Vector2(map_size / 256 * (i +  Random.nextFloat()), map_size / 256 * (j +  Random.nextFloat())))
                    else if iron(1)(i)(j) then
                        this.createResource("iron", Vector2(map_size / 256 * (i +  Random.nextFloat()), map_size / 256 * (j +  Random.nextFloat())))
                    else if cooper(1)(i)(j) then
                        this.createResource("cooper", Vector2(map_size / 256 * (i +  Random.nextFloat()), map_size / 256 * (j +  Random.nextFloat())))
                    else
                        this.createAsteroid(Vector2(map_size / 256 * (i +  Random.nextFloat()), map_size / 256 * (j +  Random.nextFloat())))

        //initial placement of the ships

        //find the brightest and darkest points of asteroid(0) and place the bases there.
        //then, place some drones around the bases in random positions on a ring

        var base_0_pos = Vector2(map_size / 256 * (min_x +  Random.nextFloat()), map_size / 256 * (min_y +  Random.nextFloat()))
        var base_1_pos = Vector2(map_size / 256 * (max_x +  Random.nextFloat()), map_size / 256 * (max_y +  Random.nextFloat()))
        
        this.createBase(0, base_0_pos)
        this.createBase(1, base_1_pos)

        var nb_starting_drone = 10

        for i <- 0 to nb_starting_drone do
            val radius = Random.nextFloat() * 100 + 200
            val angle = Random.nextFloat() * 2 * Math.PI
            val x = radius * Math.cos(angle) + base_0_pos.x
            val y = radius * Math.sin(angle) + base_0_pos.y
            this.createDrone(0, Vector2(x.toFloat, y.toFloat))

        for i <- 0 to nb_starting_drone do
            val radius = Random.nextFloat() * 100 + 200
            val angle = Random.nextFloat() * 2 * Math.PI
            val x = radius * Math.cos(angle) + base_1_pos.x
            val y = radius * Math.sin(angle) + base_1_pos.y
            this.createDrone(1, Vector2(x.toFloat, y.toFloat))
        */
        /*********************/

        createMotherShip(0, Vector2(100,100))

        //adding some resources and ships for the demo only
        //player ships
        var nb_starting_drone = 5
        var map_size = 512
        for i <- 0 to nb_starting_drone * 4 do
            var offset = Vector2(-600, -600)
            var x = Random.nextFloat() * map_size + offset.x
            var y = Random.nextFloat() * map_size + offset.y
            this.createDrone(0, Vector2(x, y))

        //enemy
        for i <- 0 to nb_starting_drone do
            var offset = Vector2(-600, 600)
            var x = Random.nextFloat() * map_size + offset.x
            var y = Random.nextFloat() * map_size + offset.y
            this.createDrone(1, Vector2(x, y))

        map_size = 1024
        var nb_asteroid = 10
        for i <- 0 to nb_asteroid do
            var offset = Vector2(-600, 0)
            var x = Random.nextFloat() * map_size + offset.x
            var y = Random.nextFloat() * map_size + offset.y
            this.createAsteroid(Vector2(x, y))

        var nb_scrap = 2
        for i <- 0 to nb_scrap do
            var x = Random.nextFloat() * map_size
            var y = Random.nextFloat() * map_size
            this.createResource("scrap",Vector2(x, y))

        var nb_cooper = 2
        for i <- 0 to nb_cooper do
            var x = Random.nextFloat() * map_size
            var y = Random.nextFloat() * map_size
            this.createResource("cooper",Vector2(x, y))

        var nb_iron = 2
        for i <- 0 to nb_iron do
            var x = Random.nextFloat() * map_size
            var y = Random.nextFloat() * map_size
            this.createResource("iron",Vector2(x, y))

    }

    //this is for the demo. It will be removed later.
    var player = this.createDrone(0, Vector2(0, 0))
    var ennemy = this.createDrone(1, Vector2(100, 100))
    var resource = this.createResource("scrap", Vector2(300, 300))

    var playerBase = this.createBase(0, Vector2(500, 500))
    var ennemyBase = this.createBase(1, Vector2(1000, 1000))

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

    def createDrone(teamID : Int, initialPosition: Vector2[Float], stats : DroneStats = DroneStats()) : Drone = {
        //create a new drone and add it to the right team.
        var drone = new Drone(teamID, initialPosition, stats)
        this.actors_list += drone
        if teamID == 0 then
            this.player_actors_list += drone
            this.playerCollisionList += drone
        else
            this.enemy_actors_list += drone
            this.enemyCollisionList += drone

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
            this.delete_list += drone

            c1.disconnect()
            c2.disconnect()
            c3.disconnect()
        })
        drone
    }

    def createMotherShip(teamID : Int, initialPosition: Vector2[Float]) : CapitalShip = {
        //create a new drone and add it to the right team.
        var motherShip = new CapitalShip(teamID, initialPosition)
        this.actors_list += motherShip
        if teamID == 0 then
            this.player_actors_list += motherShip
            this.playerCollisionList += motherShip
        else
            this.enemy_actors_list += motherShip
            this.enemyCollisionList += motherShip

        //create the connections to update the selection when the drone is clicked.
        var c1 = motherShip.onPressed.connect(Unit => {
            PlayerController.selectedUnits += motherShip
        })
        var c2 = motherShip.onTargeted.connect(Unit => {
            PlayerController.selectedTargets += motherShip
        })
        var c3 = motherShip.onReleased.connect(Unit => {
            PlayerController.selectedUnits -= motherShip
            PlayerController.selectedTargets -= motherShip
        })
        //when the drone is destroyed, we remove it from the lists and disconnect the connections.
        //TODO : verify that removing the drone from the lists doesn't cause any problem as it can happen at any time.
        motherShip.onDestroyed.connect(Unit => {
            this.delete_list += motherShip

            c1.disconnect()
            c2.disconnect()
            c3.disconnect()
        })
        motherShip
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
            this.delete_list += base

            c2.disconnect()
            c3.disconnect()
        })
        base
    }


    def createResource(typ: String, position: Vector2[Float]) : Asteroid = {
        var resource = typ match {
          case "scrap" => new Scrap(position)
          case "cooper" => new Cooper(position)
          case "iron" => new Iron(position)
          case "uranium" => new Uranium(position)
          case "ethereum" => new Ethereum(position)
          case _ =>
            print("error: resource type not found")
            new Asteroid(position)
        }
        this.actors_list += resource
        this.playerCollisionList += resource
        this.enemyCollisionList += resource
        
        var c2 = resource.onTargeted.connect(Unit => {
            PlayerController.selectedTargets += resource
        })
        var c3 = resource.onReleased.connect(Unit => {
            PlayerController.selectedTargets -= resource
        })
        resource.onDestroyed.connect(Unit => {
            this.delete_list += resource

            c2.disconnect()
            c3.disconnect()
        })
        resource
    }


    def createAsteroid(position: Vector2[Float]) : Asteroid = {
        var asteroid = new Asteroid(position)
        this.actors_list += asteroid
        this.playerCollisionList += asteroid
        this.enemyCollisionList += asteroid
        
        var c2 = asteroid.onTargeted.connect(Unit => {
            PlayerController.selectedTargets += asteroid
        })
        var c3 = asteroid.onReleased.connect(Unit => {
            PlayerController.selectedTargets -= asteroid
        })

        asteroid.onDestroyed.connect(Unit => {
            this.delete_list += asteroid
        })
        asteroid
    }

    def clearDeleteList() = {
        this.delete_list.foreach(actor => 
            actor match {
                case drone : Drone => this.clearDrone(drone)
                case motherShip : CapitalShip => this.clearMotherShip(motherShip)
                case base : Base => this.clearBase(base)
                case asteroid : Asteroid => this.clearAsteroid(asteroid)
            }
        )
        this.delete_list.clear()
    }

    def clearDrone(drone : Drone) = {
        this.player_actors_list -= drone
        this.enemy_actors_list -= drone
        this.actors_list -= drone
        this.playerCollisionList -= drone
        this.enemyCollisionList -= drone

        PlayerController.selectedUnits -= drone
        PlayerController.selectedTargets -= drone
    }

    def clearMotherShip(motherShip : CapitalShip) = {
        this.player_actors_list -= motherShip
        this.enemy_actors_list -= motherShip
        this.actors_list -= motherShip
        this.playerCollisionList -= motherShip
        this.enemyCollisionList -= motherShip

        PlayerController.selectedUnits -= motherShip
        PlayerController.selectedTargets -= motherShip
    }

    def clearBase(base : Base) = {
        this.player_actors_list -= base
        this.enemy_actors_list -= base
        this.actors_list -= base

        PlayerController.selectedUnits -= base
        PlayerController.selectedTargets -= base
    }

    def clearAsteroid(asteroid : Asteroid) = {
        this.actors_list -= asteroid
        this.playerCollisionList -= asteroid
        this.enemyCollisionList -= asteroid

        PlayerController.selectedTargets -= asteroid
    }
}

