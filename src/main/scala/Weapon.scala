class Projectile:
    var pos : (Int, Int)
    var speed_norm : Int
    var speed : (Int, Int)
    
    var target : Int

    /* TODO : les dégats ion bloquent l'énergie, les autres la détruisent et font mal au vaisseau.
    Ordre de priorité : shield > weapon > ...*/
    var damage : Int
    var ion_damage : Int

    /* ballistic weapons can pass through shields */
    var ballistic : Boolean

class Weapon:
    var name : String

    var strength : Int
    var ion_strength : Int

    var projectile_speed : Int
    var reload_time : Int
    var reloading : Int

    var energy : Int
    var ballistic : Boolean

    var target : Int /*ship id, we target a ship, not its components as is ftl.*/

    /* si l'arme est dans un vaisseau, c'est / au vaisseau, sinon, à l'univers */
    var pos : (Int, Int)

    def fire(src_pos, trg_pos):
        /* ship's x and y are the origin of the projectile */
        /* TODO : quand le ship sera vraiment un ship et pas un rond, il faudra ajuster x  et y */
        var projectile = new Projectile()
        projectile.pos = pos
        projectile.speed = (1, 0) /* TODO : calculer la direction du tir */
        projectile.speed_norm = projectile_speed

        projectile.target = target
        projectile.damage = damage
        projectile.ion_damage = ion_damage

        projectile

class Blaster_I extends Weapon:
    var name = "Blaster I"
    var strength = 1
    var projectile_speed = 10
    var reload_time = 10
    var energy = 1
    var target = -1
    var ballistic = false
    var pos = (0, 0)

class Blaster_II extends Weapon:
    var name = "Blaster II"
    var strength = 2
    var projectile_speed = 10
    var reload_time = 10
    var energy = 2
    var target = -1
    var ballistic = false
    var pos = (0, 0)

class Blaster_III extends Weapon:
    var name = "Blaster III"
    var strength = 3
    var projectile_speed = 10
    var reload_time = 10
    var energy = 3
    var target = -1
    var ballistic = false
    var pos = (0, 0)

class Heavy_Blaster_I extends Weapon:
    var name = "Heavy Blaster I"
    var strength = 2
    var projectile_speed = 10
    var reload_time = 20
    var target = -1
    var ballistic = false
    var pos = (0, 0)

class Heavy_Blaster_II extends Weapon:
    var name = "Heavy Blaster II"
    var strength = 4
    var projectile_speed = 10
    var reload_time = 20
    var energy = 2
    var target = -1
    var ballistic = false
    var pos = (0, 0)

class Heavy_Blaster_III extends Weapon:
    var name = "Heavy Blaster III"
    var strength = 6
    var projectile_speed = 10
    var reload_time = 20
    var energy = 3
    var target = -1
    var ballistic = false
    var pos = (0, 0)

class Machine_Blaster extends Weapon:
    var name = "Machine Blaster"
    var strength = 1
    var projectile_speed = 10
    var reload_time = 1
    var energy = 4
    var target = -1
    var ballistic = false
    var pos = (0, 0)

class Ion_Blaster_I extends Weapon:
    var name = "Ion Blaster I"
    var ion_strength = 1
    var projectile_speed = 10
    var reload_time = 10
    var energy = 1
    var target = -1
    var ballistic = false
    var pos = (0, 0)

class Ion_Blaster_II extends Weapon:
    var name = "Ion Blaster II"
    var ion_strength = 2
    var projectile_speed = 10
    var reload_time = 10
    var energy = 2
    var target = -1
    var ballistic = false
    var pos = (0, 0)

class Ion_Machine extends Weapon:
    var name = "Ion Machine"
    var ion_strength = 1
    var projectile_speed = 10
    var reload_time = 1
    var energy = 4
    var target = -1
    var ballistic = false
    var pos = (0, 0)

class Space_Revolver extends Weapon:
    var name = "Space Revolver"
    var strength = 6
    var projectile_speed = 20
    var reload_time = 20
    var energy = 1
    var target = -1
    var ballistic = true
    var amunition = 6 * 15
    var pos = (0, 0)

class Ocelot extends Weapon:
    /* TODO : badass item description, no information displayed */
    var name = "Ocelot"
    var strength = 6
    var projectile_speed = 20
    var reload_time = 10
    var energy = 1
    var target = -1
    var ballistic = true
    var amunition = 6 * 15
    var pos = (0, 0)

class Advanced_Hacking_L3_Slave extends Weapon:
    var name = "Advanced Hacking L3 Slave"
    var target = -1
    var ballistic = false
    var pos = (0, 0)

    def fire(src_pos, trg_pos):
        /* TODO : vérifier si il y a une synthaxe particulière pour réécrire une méthode */
        /* TODO : générer un langage aléatoire */
        course_choice = random(0, 3)
        match course_choice:
            case 0:
                /* écrit en gros le langage en mode OBJECTION ! */
                draw scala
                puts target power to 0
                -> retourner le choix pour que le jeu s'en charge
            case 1:
                draw lisp
                kill target's crew
            case 2:
                draw python
                heals target's hull
            case 3:
                draw C++
                heals target's shield and power
            case 4:
                draw C
                inflicts 3 damage constant
            case 5:
                draw assembly x86_64
                deactivate target's shield
            case 6:
                draw LaTeX
                inflicts 2 damage
            case 7:
                draw Ocaml
                inflicts 3 damage
            case 8:
                draw SFML
                kills the whole ally fleet and prints a special defeat message
            case GimML:
                draw GimML
                kills half of the enemy fleet