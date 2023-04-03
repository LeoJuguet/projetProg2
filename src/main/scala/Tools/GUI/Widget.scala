package gui

import gui.{UIComponent, Slider, SelectBox, Button, VerticalBox, TextBox}

import scala.collection.mutable.ArrayBuffer
import sfml.system.Vector2
import sfml.graphics.RenderWindow


class Widget extends UIComponent{

  override def updateClick(mousePos: Vector2[Float], leftMouse: Boolean): Unit =
    this.childs.foreach(_.updateClick(mousePos,leftMouse))
}


class DemoWidget(var window : RenderWindow) extends Widget{
  var but = Button(0,0,200,50,"yes")
  but.onPressedBind = () => {println("the button is pressed")}
  var but2 = Button(0,0,200,50,"Rage Quit")
  but2.onPressedBind = () => {window.close()}
  var verticalBox = VerticalBox(20f,40f)
  verticalBox.addChild(but)
  verticalBox.addChild(but2)
  var sliderBar = Slider(x = 100f, y = 500f, width = 200, height = 50)
  var selectBox = SelectBox(x= 400, y= 100, items = ArrayBuffer("oui","cool","ca fonctionne","test"))

  var textBox = TextBox("textBox")

  var hpBar = ProgressBar(x = 400, y = 25, width = 200, height = 50)

  this.childs += verticalBox
  this.childs += selectBox
  this.childs += textBox
  this.childs += hpBar
}
