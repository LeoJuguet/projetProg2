package ia

import sfml.system.*
import character.*

import perlin.*

def IA(ship : Ship, player : Ship) : Unit =
    //perlin noise pour mouvements aléatoires
    if norm(Vector2(ship.sprite.position.x - player.sprite.position.x, ship.sprite.position.y - player.sprite.position.y)) < 500 then
        ship.target = player
        ship.targetPosition = player.position
    
    else
        if ship.target != ship then
            ship.target = ship
        
        if ship.random_move_array.length == 1 then
            var x = perlinNoise(128, 1.2)
            var y = perlinNoise(128, 1.2)
            x = x.map(x => x * 2 - 1)
            y = y.map(y => y * 2 - 1)

            ship.random_move_array = Array.fill(128)(Vector2(0.0f, 0.0f))
            for i <- 0 to 127 do
                ship.random_move_array(i) = Vector2(x(i), y(i))

            ship.move_index = 0

        ship.move_index += 1
        if ship.move_index % 4 == 0 then
            var index = (ship.move_index / 10).toInt % 128
            ship.targetPosition += ship.random_move_array(index) * 10
