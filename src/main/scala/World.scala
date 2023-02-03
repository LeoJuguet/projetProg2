import Ship.*
import Weapon.*

class asteroid:
    var pos : (Int, Int)
    var speed_norm : Int
    var speed : (Int, Int)

    def draw(window: RenderWindow) =
        grey_circle = CircleShape(10)
        grey_circle.setFillColor(Color(100, 100, 100))
        grey_circle.setPosition(pos)
        window.draw(grey_circle)


class Universe:
    var player : Ship
    var enemies : List[Ship]()
    var allies : List[Ship]()

    var ships : List[Ship]()
    var projectiles : List[Projectile]()

    var asteroids : List[asteroid]()