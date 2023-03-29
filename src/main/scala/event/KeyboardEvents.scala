package event

import event.*
import gamestate.GameState

import sfml.graphics.RenderWindow
import sfml.window.Event
import sfml.window.Keyboard.Key
import sfml.window.Mouse.Wheel
import sfml.window.Mouse.Button
import sfml.window.Joystick.Axis
import sfml.window.Sensor
import sfml.window.Keyboard
import sfml.system.Vector2
import sfml.window.Mouse

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
  var ctrl = false
  var shift = false
  var system = false

  var leftMouse = false
  var rightMouse = false
  var holdLeft = false
  var holdRight = false

  var mouseHoldPos = Vector2(0.0f,0.0f)

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
                     ctrl: Boolean,
                     shift: Boolean,
                     system: Boolean)={
    keyboard += key -> isPressed
    this.alt = alt
    this.ctrl = ctrl
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
          case Event.KeyPressed(code, alt, ctrl, shift, system) =>{
            KeyboardState.set_key_status(code,true,alt,ctrl,shift,system)
            OnKeyPressed(code, alt, ctrl, shift, system)
          }
          case Event.KeyReleased(code, alt, ctrl, shift, system) =>{
            KeyboardState.set_key_status(code,false,alt,ctrl,shift,system)
            OnKeyReleased(code, alt, ctrl, shift, system)
          }
          case Event.MouseWheelScrolled(wheel, delta, x, y) => OnMouseWheelScrolled(wheel, delta, x, y)
          case Event.MouseButtonPressed(button, x, y) =>
            button match {
              case Button.Left =>
                KeyboardState.leftMouse = true
              case Button.Right =>
                KeyboardState.rightMouse = true
              case _ =>
            }
            OnMouseButtonPressed(button, x, y)
            // /!\ the order is important, as some events happen the first frame that the button is pressed
            button match {
              case Button.Left =>
                if KeyboardState.leftMouse == true && KeyboardState.holdLeft == false then
                  KeyboardState.mouseHoldPos = KeyboardState.mouseView
                  KeyboardState.holdLeft = true
              case Button.Right =>
                if KeyboardState.rightMouse == true && KeyboardState.holdRight == false then
                  KeyboardState.mouseHoldPos = KeyboardState.mouseView
                  KeyboardState.holdRight = true
              case _ => ()
            }
          case Event.MouseButtonReleased(button, x, y) =>
            button match {
              case Button.Left =>
                KeyboardState.holdLeft = false
                KeyboardState.leftMouse = false
              case Button.Right =>
                KeyboardState.holdRight = false
                KeyboardState.rightMouse = false
              case _ =>
            }
            OnMouseButtonReleased(button, x, y)
          case Event.MouseMoved(x, y) =>
            KeyboardState.mousePos = Vector2(x,y)
            KeyboardState.mouseView = windows.mapPixelToCoords(KeyboardState.mousePos)
            KeyboardState.mouseWindow = windows.mapPixelToCoords(KeyboardState.mousePos, GameState.camera.guiView)
            OnMouseMoved(x, y)
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
