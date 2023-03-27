package event

import event.*

import sfml.graphics.RenderWindow
import sfml.window.Event
import sfml.window.Keyboard.Key
import sfml.window.Mouse.Wheel
import sfml.window.Mouse.Button
import sfml.window.Joystick.Axis
import sfml.window.Sensor
import sfml.window.Keyboard
import sfml.system.Vector2

object OnTextEntered extends event.Event[Int]
object OnKeyPressed extends event.Event[(Key,Boolean,Boolean,Boolean,Boolean)]
object OnKeyReleased extends event.Event[(Key,Boolean, Boolean, Boolean, Boolean)]
object OnMouseWheelScrolled extends event.Event[(Wheel, Float,Int,Int)]
object OnMouseButtonPressed extends event.Event[(Button, Int, Int)]
object OnMouseButtonReleased extends event.Event[(Button, Int, Int)]
object OnMouseMoved extends event.Event[(Int, Int)]
object OnMouseEntered extends event.Event[Unit]
object OnMouseLeft extends event.Event[Unit]
object OnJoystickButtonPressed extends event.Event[(Int,Int)]
object OnJoystickButtonReleased extends event.Event[(Int,Int)]
object OnJoystickMoved extends event.Event[(Int, Axis, Float)]
object OnJoystickConnected extends event.Event[Int]
object OnJoystickDisconnected extends event.Event[Int]
object OnTouchBegan extends event.Event[(Int, Int, Int)]
object OnTouchMoved extends event.Event[(Int, Int, Int)]
object OnTouchEnded extends event.Event[(Int, Int, Int)]
object OnSensorChanged extends event.Event[(Sensor.Type, Float, Float, Float)]


object KeyboardState {
  var keyboard = Map[Key,Boolean]()
  var alt = false
  var control = false
  var shift = false
  var system = false

  var leftMouse = false
  var rightMouse = false

  var mousePos = Vector2(0,0)
  var mouseView = Vector2(0.0f, 0.0f)
  var mouseWindow = Vector2(0.0f, 0.0f)

  def is_Press(key: Key)={
    keyboard.get(key) match {
      case None => false
      case Some(value) => value
    }
  }

  def set_key_status(key: Key,
                     isPressed : Boolean,
                     alt : Boolean,
                     control: Boolean,
                     shift: Boolean,
                     system: Boolean)={
    keyboard += key -> isPressed
    this.alt = alt
    this.control = control
    this.shift = shift
    this.system = system
  }

}


object InputManager {
  var windows : RenderWindow = _

  def init(window: RenderWindow) = {
    this.windows = window
  }

  def call_events(event: Event) = {
        event match{
          case Event.Closed() => windows.close()
          case Event.Resized(width, height) => ()
          case Event.LostFocus() => ()
          case Event.GainedFocus() => ()
          case Event.TextEntered(unicode) => OnTextEntered(unicode)
          case Event.KeyPressed(code, alt, control, shift, system) =>{
            KeyboardState.set_key_status(code,true,alt,control,shift,system)
            OnKeyPressed(code, alt, control, shift, system)
          }
          case Event.KeyReleased(code, alt, control, shift, system) =>{
            KeyboardState.set_key_status(code,false,alt,control,shift,system)
            OnKeyReleased(code, alt, control, shift, system)
          }
          case Event.MouseWheelScrolled(wheel, delta, x, y) => OnMouseWheelScrolled(wheel, delta, x, y)
          case Event.MouseButtonPressed(button, x, y) => OnMouseButtonPressed(button, x, y)
          case Event.MouseButtonReleased(button, x, y) => OnMouseButtonReleased(button, x, y)
          case Event.MouseMoved(x, y) => OnMouseMoved(x, y)
          case Event.MouseEntered() => OnMouseEntered(())
          case Event.MouseLeft() => OnMouseLeft(())
          case Event.JoystickButtonPressed(joystickId, button) => OnJoystickButtonPressed(joystickId, button)
          case Event.JoystickButtonReleased(joystickId, button) => OnJoystickButtonReleased(joystickId, button)
          case Event.JoystickMoved(joystickId, axis, position) => OnJoystickMoved(joystickId, axis, position)
          case Event.JoystickConnected(joystickId) => OnJoystickConnected(joystickId)
          case Event.JoystickDisconnected(joystickId) => OnJoystickDisconnected(joystickId)
          case Event.TouchBegan(finger, x, y) => OnTouchBegan(finger, x, y)
          case Event.TouchMoved(finger, x, y) => OnTouchMoved(finger, x, y)
          case Event.TouchEnded(finger, x, y) => OnTouchEnded(finger, y, x)
          case Event.SensorChanged(sensor, x, y, z) => OnSensorChanged(sensor, x, y, z)
          }
        }


  def update() = {
    windows.pollEvent().foreach(this.call_events(_))
  }

}
