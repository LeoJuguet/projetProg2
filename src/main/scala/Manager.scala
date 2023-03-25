package manager

import scala.collection.mutable.HashMap
import sfml.graphics.*

abstract class Manager[T](var path : String = "src/main/resources/"):
    var resourcesLoaded: Map[String,T] = Map()

    def loadResource(name: String): T

    def get(name : String) : T =
      resourcesLoaded.get(name) match{
        case Some(r) => r
        case None => {
          var r = loadResource(name)
          resourcesLoaded += (name -> r)
          r
        }
      }


object TextureManager extends Manager[Texture]:
    def loadResource(name: String): Texture =
        var texture = Texture()
        texture.loadFromFile(this.path + name)
        texture


object FontManager extends Manager[Font](path = "src/main/resources/fonts/"):
    def loadResource(name: String): Font =
        var font = Font()
        font.loadFromFile(this.path + name)
        font
