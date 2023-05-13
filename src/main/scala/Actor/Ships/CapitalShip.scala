package ship

import sfml.system.Vector2
import sfml.graphics.*

import shipmodule.ShipModule
import manager.TextureManager
import gamestate.*
import event.KeyboardState
import sfml.window.Keyboard.Key
import clickable.States
import controller.Camera
import shipmodule.{ModuleHexGraph, ShipModuleWidget}

//this is the capital ship class. They are large ship able to hold a lot of cargo and house a lot of modules.
//Their behavior is similar to the drone, except their action can only be IDLE or MOVE, the modules they are controlling will do the rest.
//They do not move based on actions, as opposed to the drone, the player have to put them in range of the module they want to use.
class CapitalShip(
    teamID : Int,
    initialPosition : Vector2[Float]
) extends Ship(teamID,"CapitalShip") {
    texture = Texture()
    this.applyTexture()
    this.moveActor(initialPosition)
    
    this.maxHealth = 1000
    this.health = this.maxHealth
    this.regenerationRate = 10
    
    var capitalShipModule = ShipModule(this,"CapitalShipModule")

    // TODO : remove that !!! Now we have a graph !
    var shipDimension = Vector2(5,5)
    var modules = Array.ofDim[Option[ShipModule]](shipDimension.x,shipDimension.y)

    for x <- 0 until shipDimension.x do {
        for y <- 0 until shipDimension.y do {
            modules(x)(y) = None
        }
    }



    def enough(price : Price) : Boolean =
        this.ethereum >= price.ethereum && this.uranium >= price.uranium && this.iron >= price.iron && this.copper >= price.copper && this.scrap >= price.scrap
    
    def spend(price : Price) : Unit =
        this.ethereum -= price.ethereum
        this.uranium -= price.uranium
        this.iron -= price.iron
        this.copper -= price.copper
        this.scrap -= price.scrap


    override def draw(target: RenderTarget, states: RenderStates) =
        val render_states = RenderStates(this.transform.combine(states.transform))
        target.draw(capitalShipModule, render_states)

    //This function is the capital ship self controller, same as for the drones.
    override def updateUnit() : Unit =
        this.heal(this.regenerationRate)

        this.action match {
            case Action.IDLE => {
                //this.moveUnit(None)
            }
            case Action.MOVE(target) =>
                if this.moveUnit(Some(target)) then
                    this.action = Action.IDLE
            case _ => print("Action not valid for capital ship\n")
        }

        for i <- 0 until shipDimension.x do
            for j <- 0 until shipDimension.y do
                if modules(i)(j).isDefined then
                    modules(i)(j).get.updateModule()

    //click behavior of the capital ships. They have to be pressed by a simple clic, not a hold clic with ctrl.
    if this.teamID == 0 then
        this.updateLeftClick = () =>
            if KeyboardState.is_Press(Key.KeyLControl) then
                if this.state == States.HOVER then
                    this.onUnhovered(())
                    this.state = States.IDLE
            
            else
                if this.clickBounds.contains(KeyboardState.mouseView) then
                    this.state = States.PRESSED
                    this.onPressed(())
                else
                    if this.state == States.HOVER then
                        this.onUnhovered(())
                        this.state = States.IDLE
            
            if this.clickBounds.contains(KeyboardState.mouseView) && KeyboardState.is_Press(Key.KeyLAlt) then
                Camera.updateBind(this)
        
        this.updateRightPress = () => ()

        //this is necessary to ensure that selectioning targets do not release the actors selected by the player.
        val general_right_hold = this.updateRightHold

        this.updateRightHold = () =>
            if this.state != States.PRESSED then
                general_right_hold()
        
        this.updateRightClick = () =>
            if this.state == States.HOVER then
                this.state = States.IDLE
                this.onUnhovered(())
}
