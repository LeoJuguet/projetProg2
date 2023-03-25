package character

//TODO : ATTENTION ! Ce fichier ne sert a rien !!!!

import actor.*
import gamestate.*

class Character(gameState: GameState) extends Actor(gameState):
  private var speed = 0;
  private var maxSpeed = 100;
  private var minSpeed = -100;
  private var acceleration = 0;
  private var maxAcceleraton = 10;
  private var maxDecelaration = -10;
