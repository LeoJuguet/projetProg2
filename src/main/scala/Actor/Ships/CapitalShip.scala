package ship

import sfml.system.Vector2

import shipmodule.ShipModule
import manager.TextureManager

//this is the capital ship class. They are large ship able to hold a lot of cargo and house a lot of modules.
//Their behavior is similar to the drone, except their action can only be IDLE or MOVE, the modules they are controlling will do the rest.
//They do not move based on actions, as opposed to the drone, the player have to put them in range of the module they want to use.
class CapitalShip(
    teamID : Int,
    initialPosition : Vector2[Float]
) extends Ship(teamID) {
    texture = TextureManager.get("ovni.png")
    this.applyTexture()
    this.moveActor(initialPosition)
    
    this.maxHealth = 1000
    this.health = this.maxHealth
    this.regenerationRate = 10
    
    var shipDimension = Vector2(5,5)
    var modules = Array.ofDim[Option[ShipModule]](shipDimension.x,shipDimension.y)
    
    def enough(price : Price) : Boolean =
        this.ethereum >= price.ethereum && this.uranium >= price.uranium && this.iron >= price.iron && this.copper >= price.copper && this.scrap >= price.scrap
    
    def spend(price : Price) : Unit =
        this.ethereum -= price.ethereum
        this.uranium -= price.uranium
        this.iron -= price.iron
        this.copper -= price.copper
        this.scrap -= price.scrap
    
    //This function is the capital ship self controller, same as for the drones.
    override def updateUnit() : Unit =
        this.heal(this.regenerationRate)

        this.action match {
            case Action.IDLE => ()
            case Action.MOVE(target) =>
                if this.moveUnit(target) then
                    this.action = Action.IDLE
            case _ => print("Action not valid for capital ship\n")
        }

        for i <- 0 until shipDimension.x do
            for j <- 0 until shipDimension.y do
                if modules(i)(j).isDefined then
                    modules(i)(j).get.updateModule()
}