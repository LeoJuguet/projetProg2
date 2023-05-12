package shipmodule

import scala.collection.mutable.ListBuffer
import scala.math.Pi

import sfml.system.{Vector2, distance}

trait SquareModuleGraph {
    var up : Option[SquareModuleGraph] = None
    var down : Option[SquareModuleGraph] = None
    var left : Option[SquareModuleGraph] = None
    var right : Option[SquareModuleGraph] = None

    var localPosition : Vector2[Float] = Vector2(0, 0)
    var size : Vector2[Float] = Vector2(1, 1)

    def setModuleUp(module : SquareModuleGraph) : Unit = {
        this.up = Some(module)
        module.down = Some(this)
        
        module.localPosition = Vector2(this.localPosition.x, this.localPosition.y - ((this.size.y) + (module.size.y)) / 2)
        module.checkOtherNeighoors()
    }

    def setModuleDown(module : SquareModuleGraph) : Unit = {
        this.down = Some(module)
        module.up = Some(this)
        
        module.localPosition = Vector2(this.localPosition.x, this.localPosition.y + ((this.size.y) + (module.size.y)) / 2)
        module.checkOtherNeighoors()
    }

    def setModuleLeft(module : SquareModuleGraph) : Unit = {
        this.left = Some(module)
        module.right = Some(this)
        
        module.localPosition = Vector2(this.localPosition.x - ((this.size.x) + (module.size.x)) / 2, this.localPosition.y)
        module.checkOtherNeighoors()
    }

    def setModuleRight(module : SquareModuleGraph) : Unit = {
        this.right = Some(module)
        module.left = Some(this)
        
        module.localPosition = Vector2(this.localPosition.x + ((this.size.x) + (module.size.x)) / 2, this.localPosition.y)
        module.checkOtherNeighoors()
    }

    def checkOtherNeighoors() = {
        //Performs a search in the module graph to see if any other module is close enough to be set as a neighbor.

        var seen = ListBuffer[SquareModuleGraph](this)
        var stack = ListBuffer[SquareModuleGraph]()

        def checkPosition(module : SquareModuleGraph) = {
            if module.localPosition.x == this.localPosition.x && module.localPosition.y == this.localPosition.y + ((this.size.y) + (module.size.y)) / 2 then
                this.setModuleUp(module)
            else if module.localPosition.x == this.localPosition.x && module.localPosition.y == this.localPosition.y - ((this.size.y) + (module.size.y)) / 2 then
                this.setModuleDown(module)
            else if module.localPosition.x == this.localPosition.x + ((this.size.x) + (module.size.x)) / 2 && module.localPosition.y == this.localPosition.y then
                this.setModuleRight(module)
            else if module.localPosition.x == this.localPosition.x - ((this.size.x) + (module.size.x)) / 2 && module.localPosition.y == this.localPosition.y then
                this.setModuleLeft(module)
        }

        def addNeighboorsToToDo(module : SquareModuleGraph) = {
            if module.up.isDefined && !seen.contains(module.up.get) then
                checkPosition(module.up.get)
                stack += module.up.get
            if module.down.isDefined && !seen.contains(module.down.get) then
                checkPosition(module.down.get)
                stack += module.down.get
            if module.left.isDefined && !seen.contains(module.left.get) then
                checkPosition(module.left.get)
                stack += module.left.get
            if module.right.isDefined && !seen.contains(module.right.get) then
                checkPosition(module.right.get)
                stack += module.right.get
        }

        addNeighboorsToToDo(this)

        while !stack.isEmpty do
            val currentModule = stack.head
            stack.remove(0)
            seen += currentModule
            addNeighboorsToToDo(currentModule)
        end while
    }
}

trait ModuleHexGraph {
    // Con1 points to the top. It is the reference connection, and connected to the other 1st connection.
    // Con2 points to the bottom left with an angle of 60 degrees wrt con1. It is connected to the other 3rd connection.
    // Con3 points to the bottom right with an angle of -60 degrees wrt con1. It is connected to the other 2nd connection.
    var con1 : Option[ModuleHexGraph] = None
    var con2 : Option[ModuleHexGraph] = None
    var con3 : Option[ModuleHexGraph] = None

    // A module will be flipped wrt its neighbors to implement the hexagonal grid.
    var flip : Boolean = false

    var localPosition : Vector2[Float] = Vector2(0, 0)

    //TODO : calibrate the radius with the sprites !!!
    var radius : Float = 1

    def setConnection(connection : Int, module : ModuleHexGraph) : Unit = {
        // connection is an integer between 1 and 3
        connection match {
            case 1 =>
                this.con1 = Some(module)
                module.con1 = Some(this)
            case 2 =>
                this.con2 = Some(module)
                module.con3 = Some(this)
            case 3 =>
                this.con3 = Some(module)
                module.con2 = Some(this)
        }

        module.flip = !this.flip
        
        var angle : Float = 0
        if this.flip then angle = Pi.toFloat
        angle = (angle + (connection - 1) * Pi / 3).toFloat

        module.localPosition = Vector2(this.localPosition.x + 2 * this.radius * Math.cos(angle).toFloat, this.localPosition.y + 2 * this.radius * Math.sin(angle).toFloat)
        module.checkOtherNeighoors()
    }

    def checkOtherNeighoors() = {
        //Performs a search in the module graph to see if any other module is close enough to be set as a neighbor.

        var seen = ListBuffer[ModuleHexGraph](this)
        var stack = ListBuffer[ModuleHexGraph]()

        def checkPosition(module : ModuleHexGraph) = {
            val angle = Math.atan2(module.localPosition.y - this.localPosition.y, module.localPosition.x - this.localPosition.x)
            val dist = distance(module.localPosition, this.localPosition)

            //if we are exactly at the right distance, as we constructed a grid, then we know there is a connection.
            //Thus if the angle is strictly between 0 and Pi then we know this is a second connection, regardless of the flip state.
            //If the angle is strictly between 0 and -Pi then we know this is a third connection, regardless of the flip state.
            //Else, we know this is a first connection, regardless of the flip state.

            //We make the comparison with epsilon to avoid floating point errors.
            val epsilon = 0.01 // as the distance is in pixel, 0.01 should be largely enough.
            if dist > 2 * this.radius - epsilon && dist < 2 * this.radius + epsilon then
                angle match {
                    case x if x > 0 + epsilon && x < Pi - epsilon =>
                        this.setConnection(2, module)
                    case x if x < 0 - epsilon && x > -Pi + epsilon =>
                        this.setConnection(3, module)
                    case _ =>
                        this.setConnection(1, module)
                }
        }

        def addNeighboorsToToDo(module : ModuleHexGraph) = {
            if module.con1.isDefined && !seen.contains(module.con1.get) then
                stack += module.con1.get
            if module.con2.isDefined && !seen.contains(module.con2.get) then
                stack += module.con2.get
            if module.con3.isDefined && !seen.contains(module.con3.get) then
                stack += module.con3.get
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
}