package ShipModules

import shipmodule.*
import gamestate.*
import controller.*
import character.Resource

import scala.math.*

class MinerModule extends ShipModule
{
    var targetResource: Option[Resource] = None;

    var miningDamage = 10;
    var miningSpeed = 200;
    var miningCoolDown = 0;

    def mine() : Unit ={
        //TODO: Implement mining function here and in ressource
      this.targetResource match
        case Some(r) => {
              r.mined(this.miningDamage)
              this.parent.scrap += this.miningDamage
            }
        case None => {}
    }

    def update()={
      this.targetResource match{
        case None => {}
        case Some(r) => {
          if this.miningCoolDown > 0 then
            this.miningCoolDown = max(0, this.miningCoolDown - 1)
          else
            //TODO: Implement gathering of resource for the team
            this.mine()
          this.miningCoolDown = this.miningSpeed
          if r.live == false then
            this.targetResource = None
        }
      }
    }


}
