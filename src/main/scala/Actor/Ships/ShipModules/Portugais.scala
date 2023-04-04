package shipmodule

import scala.util.Random
import scala.math.{cos, sin, max}

import sfml.system.Vector2

import ship.DroneStats
import ship.{CapitalShip, Price}
import gamestate.GameState

enum BuilderAction:
  case IDLE
  case BUILDING

class BuildingStats (
  var buildSpeed : Int,
  var droneStats : DroneStats,
  var dronePrice : Price
) {}

class BuilderModule(parent : CapitalShip, stats : BuildingStats) extends ShipModule(parent) {
  var buildSpeed = stats.buildSpeed
  var buildCoolDown = 0

  var droneStats = stats.droneStats
  var dronePrice = stats.dronePrice

  var action = BuilderAction.IDLE

  override def updateModule()={
    this.action match
      case BuilderAction.IDLE => {}
      case BuilderAction.BUILDING => {
        this.buildCoolDown = max(0, this.buildCoolDown - 1)

        if this.buildCoolDown == 0 && this.parent.enough(dronePrice) then
          //create a drone at the edge of the ship in a random spot
          this.parent.spend(dronePrice)
          val drone = GameState.createDrone(this.parent.team, this.parent.position, this.droneStats)

          val angle = Random.nextFloat() * 2 * math.Pi.toFloat
          val dronePosition = this.parent.position + Vector2(cos(angle).toFloat, sin(angle).toFloat) * (this.parent.collisionRadius + drone.collisionRadius + 10)
          drone.moveActor(dronePosition)
      }
  }
}