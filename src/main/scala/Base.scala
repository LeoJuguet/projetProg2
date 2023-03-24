package base

import actor.*
import gamestate.*

class Base(gameState : GameState) extends Actor(gameState) {
    var maxHealth = 1000
    var health = 1000

    var regenerationRate = 10

    var scrap = 0
    var cooper = 0
    var iron = 0
    var uranium = 0

    var ethereum = 0

}