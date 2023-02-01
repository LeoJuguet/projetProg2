import Ship.*
import Weapon.*

class asteroid:
    var pos
    var speed_norm
    var speed


class Universe:
    var player : Ship
    var ennemies : List[Ship]()
    var allies : List[Ship]()

    var ships : List[Ship]()
    var projectiles : List[Projectile]()

    var asteroids : List[asteroid]()


class Settings:
    var volume = 100
    var fullscreen = false
    var resolution = (1280, 720)