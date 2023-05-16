package shipmodule

import scala.math.{max, min}

import sfml.system.norm

import ship.{Ship, CapitalShip}

//TODO : here and for drones
//       add projectiles, projectile speed, ...

enum WeaponAction:
  case IDLE
  case FIRING(target: Ship)

class WeaponStat (
    var fireSpeed: Int = 1,
    var fireRange: Int = 5,
    var damage: Int = 5,
    //TODO : use this when shields are implemented
    var ballistic: Boolean = false,
    var accuracy: Float = 1f
) {}

class WeaponModule(parent : CapitalShip, stat : WeaponStat = WeaponStat())
extends ShipModule(parent, "Weapon", "Textures/Module/on_ship_icons/weapon_module.png") {
    var action = WeaponAction.IDLE

    var fireSpeed = stat.fireSpeed
    var fireCoolDown = 0

    var fireRange = stat.fireRange
    var damage = stat.damage
    var ballistic = stat.ballistic
    var accuracy = stat.accuracy

    override def updateModule()={
        this.fireCoolDown = max(0, this.fireCoolDown - 1)

        this.action match
        case WeaponAction.IDLE => {}
        case WeaponAction.FIRING(target) => {
            if norm(this.position - target.position) < this.fireRange then
                if this.fireCoolDown == 0 then
                    this.fireCoolDown = this.fireSpeed
                    target.takeDamage(this.damage)
        }
    }
}
