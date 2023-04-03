package shipmodule

import scala.math.max

import sfml.system.norm

import shipmodule.ShipModule
import gamestate.GameState
import asteroid.Asteroid
import ship.Ship
import actor.Actor

enum MinerAction:
  case IDLE
  case MINE(target: Asteroid)

class MiningStats (
  var miningDamage: Int,
  var miningSpeed: Int,
  var miningRange: Int,
  //higher than 1. It has better results than the basic drone
  var efficiency: Float
) {}

class MinerModule(parent : Ship, stats : MiningStats) extends ShipModule(parent) {
    var miningDamage = stats.miningDamage
    var miningSpeed = stats.miningSpeed
    var miningCoolDown = 0
    var miningRange = stats.miningRange
    var efficiency = stats.efficiency
    
    var action = MinerAction.IDLE

    //difference between basic drone and miner module is that the miner module gives the resources to its parent
    def mine() : Unit =
        this.action match
        case MinerAction.MINE(target : Asteroid) =>
            var obtained = target.mined(this.miningDamage)
            this.parent.in(target, obtained * this.efficiency)
        case _ => print("Error : mine action not valid\n")

    //this function doesn't trigger the transfer action upon full load, nor does the parent. The player has to control entirely the capital ships.
    override def updateModule()={
      this.miningCoolDown = max(0, this.miningCoolDown - 1)

      this.action match
        case MinerAction.IDLE => {}
        case MinerAction.MINE(target) => {
          if norm(this.position - target.position) < this.miningRange then
            if this.miningCoolDown == 0 then
                this.mine()
                this.miningCoolDown = this.miningSpeed

                //if full load, stop mining
                if this.parent.maxLoad == this.parent.totalLoad then
                    this.action = MinerAction.IDLE
        }
    }
}
