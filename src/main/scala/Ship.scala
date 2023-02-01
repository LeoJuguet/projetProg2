import Weapon.*

class Crew:
    var name = "smith"
    var health = 100
    var max_health = 100
    var ship = -1
    var pos = (0, 0)


class Ship:
    var id

    var pos
    var speed_norm = 0
    var speed = (1, 0)

    var health = 100
    var max_health = 100

    var crew = List[Crew]()
    var weapons = List[Weapon]()

    var energy
    var max_energy
    var fuel
    
    var shield
    var max_shield


def init_player() : Ship =
    var player = new Ship()
    player.id = 0

    player.pos = (0, 0)
    player.speed = (0, 0)
    player.speed_norm = 0

    player.health = 100
    player.max_health = 100

    player.energy = 100
    player.max_energy = 100
    player.fuel = 100

    player.shield = 100
    player.max_shield = 100

    player.crew = List[Crew]()
    for i <- 0 to 3 do
        crew = new Crew()
        crew.name = "smith_" + i.toString
        crew.ship = 0
        player.crew = crew :: player.crew
    
    player.weapons = List[Weapon]()
    
    canon = new_weapon("Blaster I")
    ion = new_weapon("Ion Blaster I")