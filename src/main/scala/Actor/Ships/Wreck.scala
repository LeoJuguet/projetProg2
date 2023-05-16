package container

import sfml.system.Vector2

import ship.Price
import actor.Actor
import manager.TextureManager

class Wreck(initialPosition : Vector2[Float], angle : Float) extends Actor with Container {
    texture = TextureManager.get("wreck.png")
    this.applyTexture()

    this.moveActor(initialPosition)
    this.rotation = angle

    def salvaged() : Price = {
        val out = Price(0, 0, 0, 0, 0)
        out.scrap = this.out("scrap", 10)
        out.copper = this.out("copper", 10)
        out.iron = this.out("iron", 10)
        out.uranium = this.out("uranium", 10)
        out.ethereum = this.out("ethereum", 10)

        if this.totalLoad == 0 then
            this.destroy()

        out
    }
}