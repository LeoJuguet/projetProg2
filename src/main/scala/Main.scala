import sfml.graphics.*
import sfml.window.*
import sfml.system.*
import gamestate.*
import actor.*

import gui.*

@main def main =

    scala.util.Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(1024, 768), "Hello world"))
        var currentGame = GameState(window);

        var but = Button(400,500,200,50,"cool");
        //but.onHoveredBind = () => {println("yes")}
        //but.onPressedBind = () => {println("the button is pressed")}
        var mouseView = Vector2(0.0f,0.0f)
        var mouseWindow = Vector2(0,0)
 //       var but2 = Button(300,300,200,50,"yes")
        var verticalBox = VerticalBox()
        verticalBox.childs += but
//        verticalBox.childs += but2

        var textBox = TextBox("textBox")
        textBox.isFocused = true

        var leftMouse = false

        while window.isOpen() do
            for event <- window.pollEvent() do
                event match {
                    case _: Event.Closed => window.close()
                    case Event.MouseButtonPressed(x,y,z) =>{
                        x match {
                            case Mouse.Button.Left => leftMouse = true
                            case Mouse.Button.Right => println("right")
                            case Mouse.Button.Middle => println("middle")
                            case _ => ()
                        }
                    }
                    case Event.MouseButtonReleased(x,y,z) =>
                        x match {
                            case Mouse.Button.Left => leftMouse = false
                            case _ => ()
                        }
                    case e: Event.TextEntered => textBox.typedOn(e)
                    case _ => ()
                }
            //currentGame.drawGame()
            window.clear(Color.Black())
            mouseWindow = Mouse.position(window)
            mouseView = window.mapPixelToCoords(mouseWindow)
            but.updateClick(mouseView, leftMouse)
   //         but2.updateClick(mouseView, leftMouse)
            textBox.updateClick(mouseView, leftMouse)
            window.draw(verticalBox)
            //window.draw(but2)
            //window.draw(but)
            window.draw(textBox)
            window.display()
    }
