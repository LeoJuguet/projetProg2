package ship

import scala.math.{min, max, sqrt}

import scala.util.Random
import sfml.system.{Vector2, distance, norm}
import sfml.graphics.Transformable
import scala.collection.mutable.ListBuffer

import actor.Actor

//TODO : adapt those to the delta time, and ajust them so that the result looks good.
val visionRadius = 100f
val collisionDistance = 50f

val centeringFactor = 0.01f
val targetFactor = 0.05f
val avoidFactor = 0.03f / 0.15f
val matchingFactor = 0.05f
val chaosFactor = 0.02f

val maxSpeed = 1f
val k_nearest = 3

trait Bird extends Transformable {
    var speed = Vector2(0f, 0f)

    def kclosest(birds : ListBuffer[Actor], k : Int) : ListBuffer[Actor] =
        val sortedBirds = birds.sortBy(b => distance(b.position, this.position))

        if sortedBirds.length > k then
            sortedBirds.slice(1, k+1)
        else
            sortedBirds
    
    def findInRadius(birds : ListBuffer[Actor]) : ListBuffer[Actor] =
        birds.filter(b => distance(b.position, this.position) - b.collisionRadius < visionRadius)

    def flyTowardsCenter(birds : ListBuffer[Actor]) =
        var center = Vector2(0.0f, 0.0f)

        birds.foreach(b => center += b.position)

        center = center * (1 / max(birds.length, 1))

        this.speed = this.speed + (center - this.position) * centeringFactor

    def flyTowardsTarget(target_position : Vector2[Float]) =
        val normalized = (target_position - this.position) * (1 / norm(target_position - this.position))
        this.speed = this.speed + normalized * targetFactor
    
    def avoidCollision(birds : ListBuffer[Actor]) =
        var correction = Vector2(0f, 0f)
        var n = max(birds.length, 1)

        birds.foreach(b => {
            val dist = max(distance(b.position, this.position) - b.collisionRadius, 10)
            if dist < collisionDistance then
                correction = correction + (this.position - b.position) * (1 / distance(this.position, b.position)) * (1 / dist + 0.05f)
        })

        correction = correction * (avoidFactor)// / (norm(correction) + 1))

        val nor = min(norm(correction), avoidFactor)

        if correction != Vector2(0f, 0f) then
            this.speed += correction * (nor / norm(correction))

    def matchVelocity(birds : ListBuffer[Actor]) =
        var avgSpeed = Vector2(0f, 0f)

        birds.foreach(b => if b.isInstanceOf[Bird] then avgSpeed += b.asInstanceOf[Bird].speed)

        avgSpeed = avgSpeed * (1 / max(birds.length, 1))

        this.speed += avgSpeed * matchingFactor

    def addChaos() =
        this.speed = this.speed + (Vector2(Random.nextFloat, Random.nextFloat) * 2 - Vector2(1f, 1f)) * chaosFactor
    
    def updateBird(target_position : Option[Vector2[Float]], birds : ListBuffer[Actor], speedFactor : Float) = {
        //if the target is defined, the bird will fly towards it. Else, it simply flies according to the murmuration rules.
        val kClosestBirds = kclosest(birds, k_nearest)
        val birdsInRadius = findInRadius(kClosestBirds)

        if target_position.isDefined then
            flyTowardsTarget(target_position.get)
        else
            flyTowardsCenter(birdsInRadius)
        
        addChaos()
        avoidCollision(kClosestBirds)
        matchVelocity(birdsInRadius)

        val speedNorm = norm(this.speed)
        if speedNorm > maxSpeed then
            this.speed = this.speed * (maxSpeed / speedNorm)

        this.position = this.position + this.speed * speedFactor
    }
   }