package shipmodule

import scala.math.max

import sfml.system.norm

import ship.{CapitalShip, Price}
import container.Wreck

enum SalvageAction:
  case IDLE
  case SALVAGING(target: Wreck)

class SalvagingStats (
//between 0 and 1, the efficiency of the scalvage
  var efficiency: Float = 0.5f,
  var scalvageSpeed: Int = 1,
  var scalvageRange: Int = 5
) {}

class SalvageModule(parent : CapitalShip, stat : SalvagingStats = SalvagingStats()) extends ShipModule(parent, "Salvage") {
    var action = SalvageAction.IDLE

    var efficiency = stat.efficiency

    var salvageSpeed = stat.scalvageSpeed
    var salvageCoolDown = 0

    var salvageRange = stat.scalvageRange

    override def updateModule()={
        this.salvageCoolDown = max(0, this.salvageCoolDown - 1)

        this.action match
        case SalvageAction.IDLE => {}
        case SalvageAction.SALVAGING(target) => {
            if norm(this.position - target.position) < this.salvageRange then
                if this.salvageCoolDown == 0 then
                    this.salvageCoolDown = this.salvageSpeed
                    
                    val obtained = target.salvaged() * this.efficiency
                    this.parent.in(obtained)
                    
                    if this.parent.maxLoad == this.parent.totalLoad then
                        this.action = SalvageAction.IDLE
                    
                    if target.totalLoad == 0 then
                        this.action = SalvageAction.IDLE            
        }
    }
}

