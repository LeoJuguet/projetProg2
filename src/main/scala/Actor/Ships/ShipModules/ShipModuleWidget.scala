package shipmodule

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


// Widget for buy modules with grid
class ShipModuleWidget(shipModule: ShipModule) extends Widget {

  var buyList = SelectModuleWidget(this,shipModule)

  var posHorizontalBox = Camera.guiView.size.y - 3 * (50 + 5)

  var horizontalBox = VerticalBox(5,posHorizontalBox,direction = E_Direction.Right)

  // Module Grid
  var moduleGrid = VerticalBox(direction = E_Direction.Right)

  private def createSpecialButton(i : Int) :  Button = 
    var buttonStyle = ButtonStyle()
    var buttonTexture = shipModule.connections_points(i) match
      case None => Texture()
      case Some(value) => value.texture

    buttonStyle.idleStyle.shapeStyle.texture = buttonTexture
    buttonStyle.hoverStyle.shapeStyle.texture = buttonTexture
    buttonStyle.pressedStyle.shapeStyle.texture = buttonTexture
    var moduleButton = Button(width = 50, height = 50, buttonStyle = buttonStyle)
    val pi = i                                     // copie the number i and avoid problems with the reference i
    moduleButton.onClickedBind = () => {
      buyList.showModules(i)
    }
    moduleButton

  def updateGrid()=
    moduleGrid.removeAllChilds()
    var h1 = VerticalBox(direction = E_Direction.Right)
    var h2 = VerticalBox(direction = E_Direction.Right)
    var h3 = VerticalBox(direction = E_Direction.Right)
    h1.addChild(createSpecialButton(3))
    h1.addChild(createSpecialButton(2))
    h1.addChild(createSpecialButton(1))
    moduleGrid.addChild(h1)
    moduleGrid.addChild(h2)
    h3.addChild(createSpecialButton(4))
    h3.addChild(createSpecialButton(5))
    h3.addChild(createSpecialButton(6))
    moduleGrid.addChild(h3)

  updateGrid()

  horizontalBox.addChild(moduleGrid)

  horizontalBox.addChild(buyList)
  childs += horizontalBox


  override def close()=
    super.close()
    GameState.widgets -= this

  print("oui")

}



// Compononent to display information about purchasable modules
class ModuleCard(
  parent: SelectModuleWidget,
  shipModule: ShipModule,
  modulePos: Int ,
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
    if shipModule.parent.scrap >= module.price.scrap &&
    shipModule.parent.copper >= module.price.copper &&
    shipModule.parent.iron >= module.price.iron &&
    shipModule.parent.uranium >= module.price.uranium &&
    shipModule.parent.ethereum >= module.price.ethereum then {
      var xi = modulePos
      shipModule.connections_points(xi) match{
        case None => {
          shipModule.connections_points(modulePos) = Some(MinerModule(shipModule.parent))
          shipModule.parent.scrap -= module.price.scrap
          shipModule.parent.copper -= module.price.copper
          shipModule.parent.iron -= module.price.iron
          shipModule.parent.uranium -= module.price.uranium
          shipModule.parent.ethereum -= module.price.ethereum
          parent.parent.updateGrid()
        }
        case Some(value) => {
          if value.name != module.name then {
            shipModule.connections_points(modulePos) = Some(MinerModule(shipModule.parent))
            shipModule.parent.scrap -= module.price.scrap
            shipModule.parent.copper -= module.price.copper
            shipModule.parent.iron -= module.price.iron
            shipModule.parent.uranium -= module.price.uranium
            shipModule.parent.ethereum -= module.price.ethereum
            parent.parent.updateGrid()
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
  var image : String = "Textures/Module/PNGs/Mining_module.png",
  var price : Price = Price()
)


// Widget to choose which module to buy
class SelectModuleWidget(var parent: ShipModuleWidget, shipModule: ShipModule) extends UIComponent {
  //TODO: create a ScrollBox


  // purchasable modules
  var moduleBuyable = Array[ShopModuleStruct](
    ShopModuleStruct(),
    ShopModuleStruct(name = "Test2", image = "Textures/Module/PNGs/Weapon_module.png")
  )

  private var selectedModule = 0
  private var selectedModulePos = 0

  var moduleWidget = VerticalBox()
  var moduleList = VerticalBox()

  var buttonPrev = Button(width = 100, height = 50, string = "prev")
  var buttonNext = Button(width = 100, height = 50,string = "next")

  buttonPrev.onClickedBind = () => {
    selectedModule = (moduleBuyable.length + selectedModule - 1) % moduleBuyable.length
    showModules(selectedModulePos)
  }
  buttonNext.onClickedBind = () => {
    selectedModule = (selectedModule + 1) % moduleBuyable.length
    showModules(selectedModulePos)
  }

  var controlModule = VerticalBox(direction = E_Direction.Right)
  controlModule.addChild(buttonPrev)
  controlModule.addChild(buttonNext)

  moduleWidget.addChild(controlModule)
  moduleWidget.addChild(moduleList)
  this.childs += moduleWidget



  def showModules(i: Int)=
    hide()
    selectedModulePos = i
    moduleList.addChild(ModuleCard(this, shipModule,i, moduleBuyable(selectedModule)))
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
