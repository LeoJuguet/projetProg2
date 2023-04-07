package ShipModules

import gui.*
import ship.*
import gui.Widget
import shipmodule.*
import manager.FontManager
import gamestate.*

import sfml.system.*
import sfml.graphics.View
import controller.Camera
import manager.TextureManager
import sfml.graphics.Texture

class ShipModuleWidget(ship: CapitalShip) extends Widget {
  var size = ship.shipDimension

  var buyList = SelectModuleWidget(this,ship)

  var posHorizontalBox = Camera.guiView.size.y - size.y * (50 + 5)

  var horizontalBox = VerticalBox(5,posHorizontalBox,direction = E_Direction.Right)

  // Module Grid
  var moduleGrid = VerticalBox(direction = E_Direction.Right)
  for i <- 0 to (size.x - 1) do {
    var v = VerticalBox()
    for j <- 0 to (size.y - 1) do {
      var buttonStyle = ButtonStyle()
      var buttonTexture = ship.modules(i)(j) match
      case None => Texture()
      case Some(value) => value.texture

      buttonStyle.idleStyle.shapeStyle.texture = buttonTexture
      buttonStyle.hoverStyle.shapeStyle.texture = buttonTexture
      buttonStyle.pressedStyle.shapeStyle.texture = buttonTexture
      var moduleButton = Button(width = 50, height = 50, buttonStyle = buttonStyle)
      val (x,y) = (i,j)
      moduleButton.onClickedBind = () => {
        buyList.showModules(x,y)
      }

      v.addChild(moduleButton)
    }
    moduleGrid.addChild(v)
  }

  horizontalBox.addChild(moduleGrid)

  horizontalBox.addChild(buyList)
  childs += horizontalBox


  override def close()=
    super.close()
    GameState.widgets -= this

}


class ModuleCard(
  parent: SelectModuleWidget,
  ship: CapitalShip,
  modulePos: (Int,Int) ,
  var module : ShopModuleStruct
) extends UIComponent{
  var cardBox = VerticalBox(direction = E_Direction.Right)


  var buttonStyle = ButtonStyle()
  var buttonTexture = TextureManager.get(module.image)
  buttonStyle.idleStyle.shapeStyle.texture = buttonTexture
  buttonStyle.hoverStyle.shapeStyle.texture = buttonTexture
  buttonStyle.pressedStyle.shapeStyle.texture = buttonTexture
  var button = Button(width = 100, height = 100, buttonStyle = buttonStyle)

  button.onClickedBind = () =>
  {
    if ship.scrap >= module.price.scrap &&
    ship.copper >= module.price.copper &&
    ship.iron >= module.price.iron &&
    ship.uranium >= module.price.uranium &&
    ship.ethereum >= module.price.ethereum then {
      var (x1,x2) = modulePos
      ship.modules(x1)(x2) match{
        case None => {
          ship.modules(modulePos._1)(modulePos._2) = Some(MinerModule(ship))
          ship.scrap -= module.price.scrap
          ship.copper -= module.price.copper
          ship.iron -= module.price.iron
          ship.uranium -= module.price.uranium
          ship.ethereum -= module.price.ethereum
        }
        case Some(value) => {
          if value.name != module.name then {
            ship.modules(modulePos._1)(modulePos._2) = Some(MinerModule(ship))
            ship.scrap -= module.price.scrap
            ship.copper -= module.price.copper
            ship.iron -= module.price.iron
            ship.uranium -= module.price.uranium
            ship.ethereum -= module.price.ethereum
          }
        }
      }
    }
  }



  var verticalInfo = VerticalBox()


  var label = Label(module.name, characterSize = 40)
  var descriptionLabel = Label("default decription text", characterSize = 40)
  var scrapPrice = Label("Scrap : "+ module.price.scrap.toString, characterSize = 40)
  var copperPrice = Label("Cooper : "+ module.price.copper.toString, characterSize = 40)
  var ironPrice = Label("Iron : "+ module.price.iron.toString, characterSize = 40)
  var uraniumPrice = Label("Uranium : "+ module.price.uranium.toString, characterSize = 40)
  var ethereumPrice = Label("Ethereum : "+ module.price.ethereum.toString, characterSize = 40)

  verticalInfo.addChild(label)
  verticalInfo.addChild(descriptionLabel)
  verticalInfo.addChild(scrapPrice)
  verticalInfo.addChild(copperPrice)
  verticalInfo.addChild(ironPrice)
  verticalInfo.addChild(uraniumPrice)
  verticalInfo.addChild(ethereumPrice)


  cardBox.addChild(button)
  cardBox.addChild(verticalInfo)

  override def position: Vector2[Float]= this.cardBox.position

  override def position_=(position: Vector2[Float]) =
    this.cardBox.position = position
    this.globalBounds = cardBox.globalBounds

  override def updateClick(mousePos: Vector2[Float], leftMouse: Boolean): Boolean =
    this.childs.exists(_.updateClick(mousePos,leftMouse))

  this.childs += cardBox
  this.globalBounds = cardBox.globalBounds
}


class ShopModuleStruct(
  var name : String = "Default Name",
  var description : String = "Default description",
  var image : String = "sfml-logo.png",
  var price : Price = Price()
)

class SelectModuleWidget(parent: ShipModuleWidget,ship: CapitalShip) extends UIComponent {
  //TODO: create a ScrollBox
  var moduleBuyable = Array[ShopModuleStruct](
    ShopModuleStruct(),
    ShopModuleStruct(name = "Test2")
  )

  private var selectedModule = 0
  private var selectedModulePos = (0,0)

  var moduleWidget = VerticalBox()
  var moduleList = VerticalBox()

  var buttonPrev = Button(width = 100, height = 50, string = "prev")
  var buttonNext = Button(width = 100, height = 50,string = "next")

  buttonPrev.onClickedBind = () => {
    selectedModule = (moduleBuyable.length + selectedModule - 1) % moduleBuyable.length
    showModules(selectedModulePos._1, selectedModulePos._2)
  }
  buttonNext.onClickedBind = () => {
    selectedModule = (selectedModule + 1) % moduleBuyable.length
    showModules(selectedModulePos._1, selectedModulePos._2)
  }

  var controlModule = VerticalBox(direction = E_Direction.Right)
  controlModule.addChild(buttonPrev)
  controlModule.addChild(buttonNext)

  moduleWidget.addChild(controlModule)
  moduleWidget.addChild(moduleList)
  this.childs += moduleWidget



  def showModules(i : Int, j : Int)=
    hide()
    selectedModulePos = (i,j)
    moduleList.addChild(ModuleCard(this, ship,(i,j), moduleBuyable(selectedModule)))
    this.globalBounds = moduleList.globalBounds

  override def close()=
    super.close()
    hide()
    parent.close()


  def hide()=
    moduleList.removeAllChilds()

  override def position: Vector2[Float]= this.moduleWidget.position

  override def position_=(position: Vector2[Float]) =
    this.moduleWidget.position = position
    this.globalBounds = moduleWidget.globalBounds

  override def updateClick(mousePos: Vector2[Float], leftMouse: Boolean): Boolean =
    this.childs.exists(_.updateClick(mousePos,leftMouse))
}
