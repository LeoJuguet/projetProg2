package ship

import gui.*
import sfml.system.Vector2
import controller.Camera

class ShipInfoWidget(ship : Ship) extends Widget {

  var verticalInfo = VerticalBox()
  var label = Label(ship.name, characterSize = 40)
  var scrapPrice = Label("Scrap : "+ ship.scrap.toString, characterSize = 40)
  var copperPrice = Label("Cooper : "+ ship.copper.toString, characterSize = 40)
  var ironPrice = Label("Iron : "+ ship.iron.toString, characterSize = 40)
  var uraniumPrice = Label("Uranium : "+ ship.uranium.toString, characterSize = 40)
  var ethereumPrice = Label("Ethereum : "+ ship.ethereum.toString, characterSize = 40)

  verticalInfo.addChild(label)
  verticalInfo.addChild(scrapPrice)
  verticalInfo.addChild(copperPrice)
  verticalInfo.addChild(ironPrice)
  verticalInfo.addChild(uraniumPrice)
  verticalInfo.addChild(ethereumPrice)

  childs += verticalInfo

  verticalInfo.position = Vector2(
    Camera.guiView.size.x - 200,
    Camera.guiView.size.y - verticalInfo.globalBounds.height - 20
  )


}
