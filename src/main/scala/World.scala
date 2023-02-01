import Ship.*
import Weapon.*

class asteroid:
    var pos
    var speed_norm
    var speed

    def draw(window: RenderWindow) =
        grey_circle = CircleShape(10)
        grey_circle.setFillColor(Color(100, 100, 100))
        grey_circle.setPosition(pos)
        window.draw(grey_circle)


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