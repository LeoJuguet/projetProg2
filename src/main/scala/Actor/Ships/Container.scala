package container

import scala.math.min

import asteroid.*
import ship.Price

trait Container {
    var scrap : Float = 0
    var copper : Float = 0
    var iron : Float = 0
    var uranium : Float = 0

    var ethereum : Float = 0

    var totalLoad : Float = 0
    var maxLoad : Float = 1000

    def in(resource : String, amount : Float) : Float =
        var quantity = min(amount, this.maxLoad - this.totalLoad)
        resource match {
            case "asteroid" =>
                ()
            case "scrap" =>
                this.scrap += quantity
                this.totalLoad += quantity
            case "copper" =>
                this.copper += quantity
                this.totalLoad += quantity
            case "iron" =>
                this.iron += quantity
                this.totalLoad += quantity
            case "uranium" =>
                this.uranium += quantity
                this.totalLoad += quantity
            case "ethereum" =>
                this.ethereum += quantity
                this.totalLoad += quantity
            case _ =>
                print("trying to obtain illegal products.")
        }
        this.totalLoad += quantity
        quantity
    
    //TODO : this function serves only for capital ships, it should be moved to a different class
    def in(price : Price) : Unit =
        this.in("scrap", price.scrap)
        this.in("copper", price.copper)
        this.in("iron", price.iron)
        this.in("uranium", price.uranium)
        this.in("ethereum", price.ethereum)
    
    def in(resource : Asteroid, amount : Float) : Float =
        resource match {
            case _ : Scrap =>
                this.in("scrap", amount)
            case _ : Cooper =>
                this.in("copper", amount)
            case _ : Iron =>
                this.in("iron", amount)
            case _ : Uranium =>
                this.in("uranium", amount)
            case _ : Ethereum =>
                this.in("ethereum", amount)
            case _ : Asteroid =>
                this.in("asteroid", amount)
        }
    
    def out(resource : String, amount : Float) : Float =
        resource match{
            case "asteroid" =>
                0
            case "scrap" =>
                var quantity = min(amount, this.scrap)
                this.scrap -= quantity
                this.totalLoad -= quantity
                quantity
            case "copper" =>
                var quantity = min(amount, this.copper)
                this.copper -= quantity
                this.totalLoad -= quantity
                quantity
            case "iron" =>
                var quantity = min(amount, this.iron)
                this.iron -= quantity
                this.totalLoad -= quantity
                quantity
            case "uranium" =>
                var quantity = min(amount, this.uranium)
                this.uranium -= quantity
                this.totalLoad -= quantity
                quantity
            case "ethereum" =>
                var quantity = min(amount, this.ethereum)
                this.ethereum -= quantity
                this.totalLoad -= quantity
                quantity
            case _ =>
                print("trying to obtain illegal products.")
                0
        }
    
    //TODO : same as in
    def out(price : Price) : Unit =
        this.out("scrap", price.scrap)
        this.out("copper", price.copper)
        this.out("iron", price.iron)
        this.out("uranium", price.uranium)
        this.out("ethereum", price.ethereum)
    
    def out(resource : Asteroid, amount : Float) : Float =
        resource match {
            case _ : Scrap =>
                this.out("scrap", amount)
            case _ : Cooper =>
                this.out("copper", amount)
            case _ : Iron =>
                this.out("iron", amount)
            case _ : Uranium =>
                this.out("uranium", amount)
            case _ : Ethereum =>
                this.out("ethereum", amount)
            case _ : Asteroid =>
                this.out("asteroid", amount)
        }
        
    
    def transfer(target : Container, resource : String, amount : Float) : Float =
        var quantity = resource match {
            case "asteroid" =>
                0
            case "scrap" =>
                var q1 = min(amount, this.scrap)
                var q2 = min(amount, target.maxLoad - target.totalLoad)
                var quantity = min(q1, q2)
                quantity
            case "copper" =>
                var q1 = min(amount, this.copper)
                var q2 = min(amount, target.maxLoad - target.totalLoad)
                var quantity = min(q1, q2)
                quantity
            case "iron" =>
                var q1 = min(amount, this.iron)
                var q2 = min(amount, target.maxLoad - target.totalLoad)
                var quantity = min(q1, q2)
                quantity
            case "uranium" =>
                var q1 = min(amount, this.uranium)
                var q2 = min(amount, target.maxLoad - target.totalLoad)
                var quantity = min(q1, q2)
                quantity
            case "ethereum" =>
                var q1 = min(amount, this.ethereum)
                var q2 = min(amount, target.maxLoad - target.totalLoad)
                var quantity = min(q1, q2)
                quantity
            case _ =>
                print("trying to obtain illegal products.")
                0
        }
        this.out(resource, quantity)
        target.in(resource, quantity)
}