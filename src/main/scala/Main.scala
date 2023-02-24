import sfml.graphics.*
import sfml.window.*
import sfml.system.*
import gamestate.*
import actor.*

import scala.collection.mutable.ArrayBuffer
import gui.*

@main def main =
    scala.util.Using.Manager { use =>
        val window = use(RenderWindow(VideoMode(1024, 768), "Hello world"))
        var currentGame = GameState(window);

        var but = Button(0,0,200,50,"yes")
        //but.onPressedBind = () => {println("the button is pressed")}
        var mouseView = Vector2(0.0f,0.0f)
        var mouseWindow = Vector2(0,0)
        var but2 = Button(0,0,200,50,"Rage Quit")
        var verticalBox = VerticalBox(20f,40f)
        verticalBox.addChild(but)
        verticalBox.addChild(but2)
        var sliderBar = Slider(x = 100f, y = 500f, width = 200, height = 50)
        var selectBox = SelectBox(x= 400, y= 100, items = ArrayBuffer("oui","cool","ca fonctionne","test"))

        var textBox = TextBox("textBox")

        var leftMouse = false

        while window.isOpen() do {
            leftMouse = false
            for event <- window.pollEvent() do
                event match {
                    case _: Event.Closed => window.close()
                    case Event.MouseButtonPressed(x,y,z) =>{
                        x match {
                            case Mouse.Button.Left => leftMouse = true
                            case _ =>
                        }
                    }
                    case e: Event.TextEntered => textBox.typedOn(e)
                    case _ => ()
                }
            //currentGame.drawGame()
            window.clear(Color.Black())
            mouseWindow = Mouse.position(window)
            mouseView = window.mapPixelToCoords(mouseWindow)
            but.updateClick(mouseView, leftMouse)
            but2.updateClick(mouseView, leftMouse)
            selectBox.updateClick(mouseView, leftMouse)
            textBox.updateClick(mouseView, leftMouse)
            sliderBar.updateClick(mouseView,leftMouse)
            window.draw(verticalBox)
            window.draw(selectBox)
            window.draw(sliderBar)
            //window.draw(but2)
            //window.draw(but)
            window.draw(textBox)
            window.display()
        }
    }
