package shipmodule

import sfml.graphics.{
    Sprite,
    RenderTarget,
    RenderStates
}
import sfml.system.{Vector2, distance}


import event.KeyboardState
import sfml.window.Keyboard.Key
import clickable.States
import controller.Camera


import actor.Actor
import ship.{CapitalShip, Price}
import gamestate.GameState


import scala.collection.mutable.ListBuffer
import scala.math.Pi
//TODO : in all the types of modules, create precise variants.

//This is the base of what will be the ship modules in the final game (weapons, engines, etc.). It is an actor that can be placed on a ship.
class ShipModule(
    var parent : CapitalShip,
    var name : String,
    textureFile : String = "Textures/Module/module.png"
) extends Actor(textureFile) {

    //TODO : GameState function createModule(module, parent, position) -> onDestroyed, added to actor lists, etc.
    this.parent.onDestroyed.connect(Unit => this.destroy())

    var price: Price = _
    
    def updateModule() = {}

    // show widget for create module on Click
    var shopWidget : ShipModuleWidget = _
    onPressed.connect( Unit =>
        {
            print("clic")
            GameState.widgets -= shopWidget
            shopWidget = ShipModuleWidget(this)
            GameState.widgets += shopWidget
    })

    onReleased.connect( Unit =>
        {
            print("release")
            GameState.widgets -= shopWidget
        })
    
    def moveModule() = {
        var angle = this.parent.sprite.rotation
        this.sprite.rotation = angle
        angle = angle * Pi.toFloat / 180

        var pos = this.localPosition
        var new_position = Vector2(0f, 0f)
        new_position += Vector2(
            pos.x * Math.cos(angle).toFloat - pos.y * Math.sin(angle).toFloat,
            pos.x * Math.sin(angle).toFloat + pos.y * Math.cos(angle).toFloat
        )
        new_position += this.parent.position
        this.moveActor(new_position)
    }

    this.parent.drawModule.connect( (target,states) => {draw(target, states)})
    this.parent.updateModule.connect( - => {
        this.updateModule()
        this.moveModule()
    })

    //
    // Gestion of Module connections
    //

    // number of connection arround the module
    var connection_number = 6
    // connections_points different point all arrounds.
    var connections_points : Array[Option[ShipModule]] = Array.fill(connection_number) { None }

    var localPosition : Vector2[Float] = Vector2(0, 0)

    //TODO : calibrate the radius with the sprites !!!
    var radius : Float = 50

    def setConnection(connection : Int, module : ShipModule) : Unit = {
        // connection is an integer between 0 and 5
        this.connections_points(connection) = Some(module)
        module.connections_points((connection + connection_number/2) % connection_number)
        println(connection)
        println((connection + connection_number/2) % connection_number)
        
        var angle : Float = 0
        angle = (angle + (connection) * 2 * Pi / connection_number).toFloat

        module.localPosition = Vector2(this.localPosition.x + 2 * this.radius * Math.cos(angle).toFloat, this.localPosition.y + 2 * this.radius * Math.sin(angle).toFloat)
        module.checkOtherNeighoors()
        println(module.localPosition)
    }

    def checkOtherNeighoors() = {
        //Performs a search in the module graph to see if any other module is close enough to be set as a neighbor.

        var seen = ListBuffer[ShipModule](this)
        var stack = ListBuffer[ShipModule]()

        def checkPosition(module : ShipModule) = {
            val angle = Math.atan2(module.localPosition.y - this.localPosition.y, module.localPosition.x - this.localPosition.x)
            val dist = distance(module.localPosition, this.localPosition)

            //if we are exactly at the right distance, as we constructed a grid, then we know there is a connection.
            //Thus if the angle is strictly between 0 and Pi then we know this is a second connection, regardless of the flip state.
            //If the angle is strictly between 0 and -Pi then we know this is a third connection, regardless of the flip state.
            //Else, we know this is a first connection, regardless of the flip state.

            //We make the comparison with epsilon to avoid floating point errors.
            val epsilon = 0.01 // as the distance is in pixel, 0.01 should be largely enough.
            if dist > 2 * this.radius - epsilon && dist < 2 * this.radius + epsilon then
                val number = ((connection_number * angle / (2 * Pi)).round.toInt + connection_number) % connection_number
                this.setConnection( number , module)
        }

        def addNeighboorsToToDo(module : ShipModule) = {
            for i <- 0 to connection_number -1 do {
                if module.connections_points(i).isDefined && !seen.contains(module.connections_points(i).get) then
                    stack += module.connections_points(i).get
            }
        }

        addNeighboorsToToDo(this)

        while !stack.isEmpty do
            val currentModule = stack.head
            stack.remove(0)
            seen += currentModule

            checkPosition(currentModule)
            addNeighboorsToToDo(currentModule)
        end while
    }

    if this.parent.teamID == 0 then
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
