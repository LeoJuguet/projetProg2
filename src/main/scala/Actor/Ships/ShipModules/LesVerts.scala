package shipmodule

import scala.math.max

import sfml.system.norm

import ship.{Ship, Price}
import container.Wreck

enum ScraperAction:
  case IDLE
  case RECYCLING(target: Wreck)

class RecyclingStats (
//between 0 and 1, the efficiency of the scraper
  var efficiency: Float,
  var scraperSpeed: Int,
  var scraperRange: Int
) {}

class ScraperModule(parent : Ship, stat : RecyclingStats) extends ShipModule(parent) {
    var action = ScraperAction.IDLE

    var efficiency = stat.efficiency

    var scrapSpeed = stat.scraperSpeed
    var scrapCoolDown = 0

    var scrapRange = stat.scraperRange

    def updateModule()={
        this.scrapCoolDown = max(0, this.scrapCoolDown - 1)

        this.action match
        case ScraperAction.IDLE => {}
        case ScraperAction.RECYCLING(target) => {
            if norm(this.position - target.position) < this.scrapRange then
                if this.scrapCoolDown == 0 then
                    this.scrapCoolDown = this.scrapSpeed
                    
                    val obtained = target.scraped() * this.efficiency
                    this.parent.in(obtained)
                    
                    if this.parent.maxLoad == this.parent.totalLoad then
                        this.action = ScraperAction.IDLE
                    
                    if target.totalLoad == 0 then
                        this.action = ScraperAction.IDLE            
        }
    }
}

