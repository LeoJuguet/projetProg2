package ShipModules

import gui.*
import ship.*
import gui.Widget
import shipmodule.*
import manager.FontManager
import gamestate.*

import sfml.system.*

class ShipModuleWidget(ship: CapitalShip) extends Widget {
  var size = ship.shipDimension

  var buyList = SelectModuleWidget(this,ship)

  var horizontalBox = VerticalBox(100,200,direction = E_Direction.Right)

  var verticalsBox = VerticalBox(direction = E_Direction.Right)
  for i <- 0 to (size.x - 1) do {
    var v = VerticalBox()
    for j <- 0 to (size.y - 1) do {
      var moduleButton = Button(width = 50, height = 50)
      val (x,y) = (i,j)
      moduleButton.onClickedBind = () => {
        buyList.showModules(x,y)
      }

      v.addChild(moduleButton)
    }
    verticalsBox.addChild(v)
  }

  horizontalBox.addChild(verticalsBox)

  horizontalBox.addChild(buyList)
  childs += horizontalBox


  override def close()=
    super.close()
    GameState.widgets -= this

}


class ModuleCard(parent: SelectModuleWidget,ship: CapitalShip, modulePos: (Int,Int) , var module : ShopModuleStruct) extends UIComponent{
  var cardBox = VerticalBox(direction = E_Direction.Right)

  var button = Button(width = 100, height = 100)

  button.onClickedBind = () => {
    if ship.modules(modulePos._1)(modulePos._2).getClass() != module.getClass() &&
      ship.scrap >= module.price.scrap &&
      ship.copper >= module.price.copper &&
      ship.iron >= module.price.iron &&
      ship.uranium >= module.price.uranium &&
      ship.ethereum >= module.price.ethereum
    then {
      ship.modules(modulePos._1)(modulePos._2) = Some(MinerModule(ship))
      ship.scrap -= module.price.scrap
      ship.copper -= module.price.copper
      ship.iron -= module.price.iron
      ship.uranium -= module.price.uranium
      ship.ethereum -= module.price.ethereum
      parent.close()

       }
  }


  var verticalInfo = VerticalBox()


  var label = Label("default Text", characterSize = 40)
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

  override def updateClick(mousePos: Vector2[Float], leftMouse: Boolean): Unit =
    this.childs.foreach(_.updateClick(mousePos,leftMouse))

  this.childs += cardBox
  this.globalBounds = cardBox.globalBounds
}


class ShopModuleStruct(
  var name : String = "Default Name",
  var description : String = "Default description",
  var image : String = "/src/ressources/sfml-logo.png",
  var price : Price = Price()
)

class SelectModuleWidget(parent: ShipModuleWidget,ship: CapitalShip) extends UIComponent {
  //TODO: create a ScrollBox
  var moduleBuyable = Array[ShopModuleStruct](ShopModuleStruct(),
  ShopModuleStruct(name = "Test2"))

  var moduleList = VerticalBox()


  this.childs += moduleList

  def showModules(i : Int, j : Int)=
    hide()
    for module <- moduleBuyable do {
      moduleList.addChild(ModuleCard(this, ship,(i,j), module))
    }
    this.globalBounds = moduleList.globalBounds

  override def close()=
    super.close()
    hide()
    parent.close()


  def hide()=
    moduleList.removeAllChilds()

  override def position: Vector2[Float]= this.moduleList.position

  override def position_=(position: Vector2[Float]) =
    this.moduleList.position = position
    this.globalBounds = moduleList.globalBounds

  override def updateClick(mousePos: Vector2[Float], leftMouse: Boolean): Unit =
    this.childs.foreach(_.updateClick(mousePos,leftMouse))
}
