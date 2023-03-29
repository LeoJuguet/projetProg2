package ia

import sfml.system.*
import character.*

import scala.util.Random

import ship.{Action, Ship}
import ship.{Action, Ship}
//le but de cette fonction est de faire une démonstration que les fonctionnalités du jeu marchent.
//Ce n'est certainement pas, à ce stade, une véritable "IA" d'un ennemi du jeu final. Ce n'est qu'un démonstrateur.
def IA(ship : Ship, player : Ship) : Unit =
    //si l'ennemi est assez proche du joueur, il l'attaque
    //TODO : quand "le joueur" sera une armée, ce sera évidemment la distance la plus courte qui sera prise en compte
    //TODO : A REFAIRE EN ENTIER !!!
    if distance(ship.position, player.position) < 350 then
        ship.action = Action.ATTACK(player)
    
    //sinon, il se déplace aléatoirement
    else
        ship.action match {
            case Action.ATTACK(_) =>
                ship.action = Action.IDLE
            case _ => ()
        }
        
        //l'ennemi se déplace aléatoirement
        if ship.action == Action.IDLE then
            ship.action = Action.MOVE(ship.position + Vector2(Random.nextFloat() * 50, Random.nextFloat() * 50))
        
        //the AI does not allow for a demonstration of the mining behavior. However, it is demonstrated by the player.
        //The transfer behavior is also not demonstrated, but automatically after the mining is done.
