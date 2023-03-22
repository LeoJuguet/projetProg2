package ia

import sfml.system.*
import character.*

import perlin.*

def IA(ship : Ship, player : Ship) : Unit =
    //si l'ennemi est assez proche du joueur, il l'attaque
    //TODO : quand "le joueur" sera une armée, ce sera évidemment la distance la plus courte qui sera prise en compte
    if norm(Vector2(ship.position.x - player.position.x, ship.position.y - player.position.y)) < 350 then
        ship.targetShip = player
        ship.targetPosition = player.position
        ship.currentAction = Action.ATTACK
    
    //sinon, il se déplace aléatoirement
    else
        //TODO : faire une Option[Ship] pour le targetShip, comme ça on peut mettre None à la place de ship
        if ship.targetShip != ship then
            ship.targetShip = ship
        
        //si l'ennemi est en train d'attaquer, il s'arrête
        if ship.currentAction == Action.ATTACK then
            ship.currentAction = Action.IDLE
        
        //on génère un bruit de perlin pour déterminer le mouvement de l'ennemi, si il n'a pas déjà été généré
        if ship.random_move_array.length == 1 then
            var x = perlinNoise(128, 1.2)
            var y = perlinNoise(128, 1.2)
            x = x.map(x => x * 2 - 1)
            y = y.map(y => y * 2 - 1)

            ship.random_move_array = Array.fill(128)(Vector2(0.0f, 0.0f))
            for i <- 0 to 127 do
                ship.random_move_array(i) = Vector2(x(i), y(i))

            ship.move_index = 0

        //l'ennemi se déplace aléatoirement
        ship.move_index += 1
        if ship.move_index % 4 == 0 then
            var index = (ship.move_index / 10).toInt % 128
            ship.targetPosition += ship.random_move_array(index) * 10
