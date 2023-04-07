package shipmodule

import scala.collection.mutable.ArrayBuffer
import scala.math.{max, min}

import sfml.system.norm

import ship.{Ship, CapitalShip}

enum NurseAction:
  case IDLE
  case HEALING(targets : ArrayBuffer[Ship])

class NurseStat (
  var healingSpeed : Int,
  var healingRange : Int,
  var maxBurst : Int,
  var totalHeal : Int
) {}

class NurseModule(parent : CapitalShip, stat : NurseStat) extends ShipModule(parent, "Nurse") {
  var action = NurseAction.IDLE

  var healingSpeed = stat.healingSpeed
  var healingCoolDown = 0

  var healingRange = stat.healingRange

  var maxBurst = stat.maxBurst
  var totalHeal = stat.totalHeal

  override def updateModule()={
    this.healingCoolDown = max(0, this.healingCoolDown - 1)

    this.action match
    case NurseAction.IDLE => {}
    case NurseAction.HEALING(targets) => {
      if this.healingCoolDown == 0 then
        this.healingCoolDown = this.healingSpeed

        //starts by counting the number of ships in range that need healing
        var shipsInRange = 0
        targets.foreach(ship => {
          if norm(this.position - ship.position) < this.healingRange && ship.health < ship.maxHealth then
            shipsInRange += 1
        })

        //then finds how much each ship will be healed
        val healPerShip = min(this.totalHeal / shipsInRange, this.maxBurst)
        val unUsedHeal = this.totalHeal - healPerShip * shipsInRange

        //then heals each ship
        targets.foreach(ship => {
          if norm(this.position - ship.position) < this.healingRange && ship.health < ship.maxHealth then
            ship.heal(healPerShip)
        })
    }
  }
}
