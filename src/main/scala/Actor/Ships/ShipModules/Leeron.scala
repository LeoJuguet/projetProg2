package shipmodule

import scala.math.{max, min}

import sfml.system.norm

import container.Wreck
import ship.{CapitalShip, DroneStats}
import gamestate.GameState

enum RecyclerAction:
  case IDLE
  //TODO : when actions will behave as a list, change the behavior so that while not idle, the parent moves to the next target. Priority to the closest target.
  //       The priority between actions of different modules are not yet defined. It will most certainly depend on the module's position in the ship grid.
  case RECYCLING(target: Wreck, state : Int = 0)

class RecyclerStat(
    var recyclerSpeed: Int,
    var recyclerRange: Int,
    //TODO : when deltatime, do this in time, not in frame
    var recyclingTime: Int,
    var droneStats : DroneStats
) {}

class RecyclerModule(parent : CapitalShip, stat : RecyclerStat) extends ShipModule(parent) {
    var action = RecyclerAction.IDLE

    var recycleSpeed = stat.recyclerSpeed
    var recycleCoolDown = 0
    var recyclingTime = stat.recyclingTime

    var recycleRange = stat.recyclerRange

    var droneStats = stat.droneStats

    override def updateModule()={
        this.recycleCoolDown = max(0, this.recycleCoolDown - 1)

        this.action match
        case RecyclerAction.IDLE => {}
        case RecyclerAction.RECYCLING(target, state) => {
            if norm(this.position - target.position) < this.recycleRange then
                if this.recycleCoolDown == 0 then
                    this.recycleCoolDown = this.recycleSpeed
                    if state == this.recyclingTime then
                        val drone = GameState.createDrone(this.parent.team, target.position, this.droneStats)
                        this.action = RecyclerAction.IDLE
                    else
                        this.action = RecyclerAction.RECYCLING(target, state - 1)    
        }
    }
}