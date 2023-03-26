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


class ModuleCard(ship: Ship, modulePos: Int , var module : ShipModule) extends Widget{
  var cardBox = VerticalBox(direction = E_Direction.Right)

  var button = Button(width = 100, height = 100)
  var md = ShipModule()
  button.onClickedBind = () => {
  }


  var verticalInfo = VerticalBox()

  var font = FontManager.get("game_over.ttf")

  var label = Label("default Text", font, 20)
  var descriptionLabel = Label("default decription text", font, 10)
  var scrapPrice = Label("Scrap : "+ module.price.scrap.toString, font, 20)
  var cooperPrice = Label("Cooper : "+ module.price.cooper.toString, font, 20)
  var ironPrice = Label("Iron : "+ module.price.iron.toString, font, 20)
  var uraniumPrice = Label("Uranium : "+ module.price.uranium.toString, font, 20)
  var ethereumPrice = Label("Ethereum : "+ module.price.ethereum.toString, font, 20)

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
