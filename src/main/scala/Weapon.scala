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

def new_weapon(name)
    var weapon = new Weapon()
    weapon.name = name
    if name == "Blaster I":
        weapon.strength = 1
        weapon.ion_strength = 0
        weapon.projectile_speed = 10
        weapon.reload_time = 10
        weapon.energy = 1
        weapon.target = -1
        weapon.ballistic = false
        weapon.pos = (0, 0)
    else if name == "Blaser II":
        weapon.strength = 2
        weapon.ion_strength = 0
        weapon.projectile_speed = 10
        weapon.reload_time = 10
        weapon.energy = 2
        weapon.target = -1
        weapon.ballistic = false
        weapon.pos = (0, 0)
    else if name == "Blaster III":
        weapon.strength = 3
        weapon.ion_strength = 0
        weapon.projectile_speed = 10
        weapon.reload_time = 10
        weapon.energy = 3
        weapon.target = -1
        weapon.ballistic = false
        weapon.pos = (0, 0)
    else if name == "Heavy Blaster I":
        weapon.strength = 2
        weapon.ion_strength = 0
        weapon.projectile_speed = 10
        weapon.reload_time = 20
        weapon.target = -1
        weapon.ballistic = false
        weapon.pos = (0, 0)
    else if name == "Heavy Blaster II":
        weapon.strength = 4
        weapon.ion_strength = 0
        weapon.projectile_speed = 10
        weapon.reload_time = 20
        weapon.energy = 2
        weapon.target = -1
        weapon.ballistic = false
        weapon.pos = (0, 0)
    else if name == "Heavy Blaster III":
        weapon.strength = 6
        weapon.ion_strength = 0
        weapon.projectile_speed = 10
        weapon.reload_time = 20
        weapon.energy = 3
        weapon.target = -1
        weapon.ballistic = false
        weapon.pos = (0, 0)
    else if name == "Machine Blaster":
        weapon.strength = 1
        weapon.ion_strength = 0
        weapon.projectile_speed = 10
        weapon.reload_time = 1
        weapon.energy = 4
        weapon.target = -1
        weapon.ballistic = false
        weapon.pos = (0, 0)
    else if name == "Ion Blaster I":
        weapon.strength = 0
        weapon.ion_strength = 1
        weapon.projectile_speed = 5
        weapon.reload_time = 5
        weapon.energy = 1
        weapon.target = -1
        weapon.ballistic = false
        weapon.pos = (0, 0)
    else if name == "Ion Blaster II":
        weapon.strength = 0
        weapon.ion_strength = 2
        weapon.projectile_speed = 5
        weapon.reload_time = 5
        weapon.energy = 2
        weapon.target = -1
        weapon.ballistic = false
        weapon.pos = (0, 0)
    else if name = "Ion Machine":
        weapon.strength = 0
        weapon.ion_strength = 1
        weapon.projectile_speed = 5
        weapon.reload_time = 1
        weapon.energy = 4
        weapon.target = -1
        weapon.ballistic = false
        weapon.pos = (0, 0)
    else if name = "Space Revolver"
        weapon.strength = 6
        weapon.ion_strength = 0
        weapon.projectile_speed = 20
        weapon.reload_time = 20
        weapon.energy = 1
        weapon.target = -1
        weapon.ballistic = true
        weapon.pos = (0, 0)
    else if name = "Ocelot"
        /* TODO : badass item description, no information displayed */
        weapon.strength = 6
        weapon.ion_strength = 0
        weapon.projectile_speed = 20
        weapon.reload_time = 10
        weapon.energy = 1
        weapon.target = -1
        weapon.ballistic = true
        weapon.pos = (0, 0)