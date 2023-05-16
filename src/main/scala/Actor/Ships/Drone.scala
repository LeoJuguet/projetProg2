package ship

import scala.math.{min, max}

import sfml.system.{Vector2, norm}
import sfml.graphics.Rect
import sfml.window.Keyboard.Key

import ship.Ship
import event.KeyboardState
import clickable.States
import controller.Camera
import asteroid.Asteroid
import container.Container
import gamestate.GameState
import actor.Actor
import manager.TextureManager

class DroneStats(
    var maxHealth : Int = 50,
    var regenerationRate : Int = 0,
    
    var attackDamage : Int = 5,
    var attackSpeed : Int = 500,
    var attackCoolDown : Int = 10,

    var miningDamage : Int = 10,
    var miningSpeed : Int = 200,

    var maxLoad : Int = 20
) {}

/**
 * extention of the basic ship class.
 * It has no additional features, but its click behavior is different.
*/
class Drone(
    teamID: Int,
    initialPosition: Vector2[Float],
    stats : DroneStats
)
extends Ship(teamID,"Drone") {
    texture = TextureManager.get("drone.png")
    this.applyTexture()
    this.moveActor(initialPosition)
    
    this.maxHealth = stats.maxHealth
    this.health = this.maxHealth
    this.regenerationRate = stats.regenerationRate

    var attackDamage = stats.attackDamage
    var attackSpeed = stats.attackSpeed
    var attackCoolDown = stats.attackCoolDown

    var miningDamage = stats.miningDamage
    var miningSpeed = stats.miningSpeed
    var miningCoolDown = 0

    this.maxLoad = stats.maxLoad
    
    def attack() : Unit =
        this.action match
        case Action.ATTACK(target : Ship) => target.takeDamage(this.attackDamage)
        case Action.ATTACK(target : Base) => target.takeDamage(this.attackDamage)
        case _ => print("Error : attack action not valid\n")
    
    def mine() : Unit =
        this.action match
        case Action.MINE(target : Asteroid) =>
            var obtained = target.mined(this.miningDamage)
            this.in(target, obtained)
        case _ => print("Error : mine action not valid\n")
    
    def transfer() : Unit =
        //TODO : implement transfer rate (here it is 10)
        this.action match { case Action.TRANSFER(target : Container) =>
            if this.ethereum > 0 then
                this.transfer(target, "etherum", 10)
            else if this.uranium > 0 then
                this.transfer(target, "uranium", 10)
            else if this.iron > 0 then
                this.transfer(target, "iron", 10)
            else if this.copper > 0 then
                this.transfer(target, "copper", 10)
            if this.scrap > 0 then
                this.transfer(target, "scrap", 10)
        case _ => print("Error : transfer action not valid\n")
        }
    
    //This function is the drone self controller, it decides what to do based on the current state of the drone.
    //It serves to perform the actions decided in the controller.
    override def updateUnit() =
        this.heal(this.regenerationRate)

        this.attackCoolDown = max(0, this.attackCoolDown - 1)
        this.miningCoolDown = max(0, this.miningCoolDown - 1)

        this.action match {
            case Action.IDLE => {
                //the drone allways follows the mururation fly, and if it has a target it will also fly towards it.
                //this.moveUnit(None)
            }
            case Action.MOVE(target) => {
                if this.moveUnit(Some(target)) then
                    this.action = Action.IDLE
            }
            case Action.ATTACK(target) => {
                //if the ship is close enough to the target, it will attack it
                if norm(this.position - target.position) < 20 + this.collisionRadius + target.collisionRadius then
                    this.moveUnit(None)

                    if this.attackCoolDown == 0 then
                        this.attack()
                        this.attackCoolDown = this.attackSpeed
                        //there is no need to check if the target is still alive, because the player controller will change the target if it dies
                //if the ship is not close enough, it will move towards the target
                else
                    this.moveUnit(Some(target.position))
            }
            case Action.MINE(target) => {
                //if the ship is close enough to the resource, it will mine it
                if norm(this.position - target.position) < 20 + this.collisionRadius + target.collisionRadius then
                    this.moveUnit(None)

                    //Check if cooldown is over
                    if this.miningCoolDown == 0 then
                        this.mine()
                        this.miningCoolDown = this.miningSpeed

                        //Check if full
                        if this.totalLoad == this.maxLoad then
                            //start transfering
                            this.action = Action.TRANSFER(GameState.actors_list.find(actor => actor match {
                                case base : Base => base.team == this.team
                                case _ => false
                            }) match {
                                case Some(base : Base) => base
                                case _ => print("Error : no base found for the team\n"); null
                            })
                //if the ship is not close enough, it will move towards the resource
                else
                    this.moveUnit(Some(target.position))
            }
            case Action.TRANSFER(target) => {
                //if the ship is close enough to the mase, it will transfer the resources
                if norm(this.position - target.asInstanceOf[Actor].position) < 20 + this.collisionRadius + target.asInstanceOf[Actor].collisionRadius then
                    this.moveUnit(None)

                    this.transfer()

                    //Check if empty
                    if this.totalLoad == 0 then
                        this.action = Action.IDLE
                
                //if the ship is not close enough, it will move towards the base
                else
                    this.moveUnit(Some(target.asInstanceOf[Actor].position))
            }
        }

    //for mor details ont the behavior of the drone, see the Actor class.
    if this.teamID == 0 then
        this.updateLeftClick = () =>
            if KeyboardState.is_Press(Key.KeyLControl) then
                var firstPos = KeyboardState.mouseHoldPos
                var secondPos = KeyboardState.mouseView
                var topLeft = Vector2[Float](min(firstPos.x, secondPos.x), min(firstPos.y, secondPos.y))
                var bottomRight = Vector2[Float](max(firstPos.x, secondPos.x), max(firstPos.y, secondPos.y))
                var size = bottomRight - topLeft
                var selectionRect = Rect(topLeft.x, topLeft.y, size.x, size.y)

                if this.clickBounds.intersects(selectionRect) then
                    this.state = States.PRESSED
                    this.onPressed(())
                else
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