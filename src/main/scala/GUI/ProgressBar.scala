package gui

import scala.collection.mutable.ArrayBuffer
import sfml.graphics.*
import sfml.system.*
import gui.{VerticalBox,UIComponent}
import gui.Style
import scala.math._

class ProgressBarStyle(
    var backGroundStyle: ShapeStyle = ShapeStyle(),
    var foreGroundStyle: ShapeStyle = ShapeStyle(style = Style(fillColor = Color.Green(),outlineThickness=0f)))
{
    def apply(backGround: Shape,foreGround: Shape)=
        backGroundStyle.apply(backGround)
        foreGroundStyle.apply(foreGround)
}

class ProgressBar(
    var x: Float,
    var y: Float,
    width: Float,
    height: Float,
    var minValue: Float = 0f,
    var maxValue: Float = 1f,
    var currentValue: Float = 0.5f,
    var style:ProgressBarStyle = ProgressBarStyle()
) extends UIComponent(width,height){
    this.globalBounds = Rect(this.x, this.y, this.width, this.height)
    
    var backGround = RectangleShape(this.width,this.height)
    var foreGround = RectangleShape(this.width,this.height)
    this.style.apply(backGround,foreGround)


    def updateValue(newValue: Float) = {
        currentValue =  max(minValue,min(newValue,maxValue))
        var fillProportion: Float = (currentValue - this.minValue) / (this.maxValue - this.minValue)
        foreGround.scale(fillProportion,1f)
    }


    override def draw(target: RenderTarget, states: RenderStates) =
        this.backGround.draw(target,states)
        this.foreGround.draw(target,states)
}
