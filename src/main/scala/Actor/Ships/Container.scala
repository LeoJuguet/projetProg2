package container

import scala.math.min

import resource.*

trait Container {
    var team = 0

    var scrap = 0
    var copper = 0
    var iron = 0
    var uranium = 0

    var ethereum = 0

    var totalLoad = 0
    var maxLoad = 1000

    def in(resource : String, amount : Int) : Int =
        var quantity = min(amount, this.maxLoad - this.totalLoad)
        resource match {
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
        }
        this.totalLoad += quantity
        quantity
    
    def in(resource : Resource, amount : Int) : Int =
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
        }
    
    def out(resource : String, amount : Int) : Int =
        resource match{
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
        }
    
    def out(resource : Resource, amount : Int) : Int =
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
        }
        
    
    def transfer(target : Container, resource : String, amount : Int) : Int =
        var quantity = resource match {
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
        }
        this.out(resource, quantity)
        target.in(resource, quantity)
}