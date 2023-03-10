package character

import actor.*
import gamestate.*
import controller.*

class Character(gameState: GameState, controller : Controller ) extends Actor(gameState, controller):
  private var speed = 0;
  private var maxSpeed = 100;
  private var minSpeed = -100;
  private var acceleration = 0;
  private var maxAcceleraton = 10;
  private var maxDecelaration = -10;
