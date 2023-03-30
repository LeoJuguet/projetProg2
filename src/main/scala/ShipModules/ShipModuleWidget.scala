import gui.*
import gui.Widget
import character.Ship
import shipmodule.*
import manager.FontManager

class ShipModuleWidget(ship: Ship) extends Widget{
  var size = ship.shipDimension

  var verticalsBox = VerticalBox(direction = E_Direction.Right)
  for i <- 0 to (size.x - 1) do {
    var v = VerticalBox()
    for j <- 0 to (size.y - 1) do {
      var moduleButton = Button(width = 100, height = 100)
      v.addChild(moduleButton)
    }
    verticalsBox.addChild(v)
  }


}


class ModuleCard(ship: Ship, modulePos: (Int,Int) , var module : ShipModule) extends Widget{
  var cardBox = VerticalBox(direction = E_Direction.Right)

  var button = Button(width = 100, height = 100)
  var md = ShipModule()
  button.onClickedBind = () => {
    if ship.modules(modulePos._1)(modulePos._2).getClass() != module.getClass() &&
      ship.scrap >= module.price.scrap &&
      ship.cooper >= module.price.cooper &&
      ship.iron >= module.price.iron &&
      ship.uranium >= module.price.uranium &&
      ship.ethereum >= module.price.ethereum
    then {
      ship.modules(modulePos._1)(modulePos._2) = this.module
      ship.scrap -= module.price.scrap
      ship.cooper -= module.price.cooper
      ship.iron -= module.price.iron
      ship.uranium -= module.price.uranium
      ship.ethereum -= module.price.ethereum
       }
  }


  var verticalInfo = VerticalBox()


  var label = Label("default Text", characterSize = 20)
  var descriptionLabel = Label("default decription text", characterSize = 10)
  var scrapPrice = Label("Scrap : "+ module.price.scrap.toString, characterSize = 20)
  var cooperPrice = Label("Cooper : "+ module.price.cooper.toString, characterSize = 20)
  var ironPrice = Label("Iron : "+ module.price.iron.toString, characterSize = 20)
  var uraniumPrice = Label("Uranium : "+ module.price.uranium.toString, characterSize = 20)
  var ethereumPrice = Label("Ethereum : "+ module.price.ethereum.toString, characterSize = 20)

  verticalInfo.addChild(label)
  verticalInfo.addChild(descriptionLabel)
  verticalInfo.addChild(scrapPrice)
  verticalInfo.addChild(cooperPrice)
  verticalInfo.addChild(ironPrice)
  verticalInfo.addChild(uraniumPrice)
  verticalInfo.addChild(ethereumPrice)


  cardBox.addChild(button)
  cardBox.addChild(verticalInfo)


  this.globalBounds = cardBox.globalBounds

  this.childs += cardBox
}



class SelectModuleWidget(ship: Ship) extends Widget {
  //TODO: create a ScrollBox
  var moduleList = VerticalBox()
  this.childs += moduleList

}
