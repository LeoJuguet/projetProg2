package ship

class Price(
    var scrap: Float = 0,
    var copper: Float = 0,
    var iron: Float = 0,
    var uranium: Float = 0,
    var ethereum: Float = 0,
){
    def *(factor: Float): Price = {
        Price(
            this.scrap * factor,
            this.copper * factor,
            this.iron * factor,
            this.uranium * factor,
            this.ethereum * factor,
        )
    }
}