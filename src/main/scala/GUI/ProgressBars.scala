package gui

import scala.collection.mutable.ArrayBuffer
import sfml.graphics.*
import sfml.system.*
import gui.{VerticalBox,UIComponent}
import gui.Style
import scala.math._

//A prendre globalement comme un schema de code et pas vraiment du code pour l'instant
val ProgressBarFillStyle = Style (
    fillColor = Color.Green(),
    outlineThickness = 0f
)

val bgStyle = ShapeStyle()
val fgStyle = ShapeStyle(style = ProgressBarFillStyle)

class ProgressBar(
    var x: Float,
    var y: Float,
    width: Float,
    height: Float,
    var minValue: Float = 0f,
    var maxValue: Float = 1f,
    var currentValue: Float = 0.5f
) extends UIComponent(width,height){
    this.globalBounds = Rect(this.x, this.y, this.width, this.height)
    
    var backgroundBar = RectangleShape(this.width,this.height)
    bgStyle.apply(backgroundBar)

    
    var progressBar = RectangleShape(this.width,this.height)
    fgStyle.apply(progressBar)


    def updateValue(newValue: Float) = {
        currentValue =  max(minValue,min(newValue,maxValue))
        var fillProportion: Float = (currentValue - this.minValue) / (this.maxValue - this.minValue)
        progressBar.scale(fillProportion,1f)
    }


    override def draw(target: RenderTarget, states: RenderStates) =
        this.backgroundBar.draw(target,states)
        this.progressBar.draw(target,states)
}
