package shipmodule
import shipmodule.*
import ship.*
import event.KeyboardState
import sfml.window.Keyboard.Key
import clickable.States
import controller.Camera

class CockpitModule(
    parent : CapitalShip
) 
extends ShipModule(parent,"Cockpit","Textures/Module/module.png"){



   if this.parent.teamID == 0 then
        this.updateLeftClick = () =>
            if KeyboardState.is_Press(Key.KeyLControl) then
                if this.state == States.HOVER then
                    parent.onUnhovered(())
                    parent.state = States.IDLE
                    this.onUnhovered(())
                    this.state = States.IDLE
            
            else
                if this.clickBounds.contains(KeyboardState.mouseView) then
                    this.state = States.PRESSED
                    this.onPressed(())
                    parent.state = States.PRESSED
                    parent.onPressed(())
                else
                    if this.state == States.HOVER then
                        this.onUnhovered(())
                        this.state = States.IDLE
                        parent.onUnhovered(())
                        parent.state = States.IDLE
            
            if this.clickBounds.contains(KeyboardState.mouseView) && KeyboardState.is_Press(Key.KeyLAlt) then
                Camera.updateBind(this)
        
        this.updateRightPress = () => ()

        //this is necessary to ensure that selectioning targets do not release the actors selected by the player.
        val general_right_hold = this.updateRightHold

        this.updateRightHold = () =>
            if this.state != States.PRESSED then
                general_right_hold()
                parent.updateRightHold()
        
        this.updateRightClick = () =>
            if this.state == States.HOVER then
                this.state = States.IDLE
                this.onUnhovered(())
                parent.state = States.IDLE
                parent.onUnhovered(())
}
