package event

import scala.collection.mutable.ArrayBuffer


/** Class for save connection between a function and a event
 *
 * @constructor Create a connection between an event and a function
 * @param event the event to connect
 * @param callback the function to call when the event is call
 * */
class Connection[T](var event : Event[T], var callback: T => Unit) {
  type Callback = T => Unit


  /**
    * Disconnect the connection to the event.
    */
  def disconnect(): Unit =
    event.disconnect(this)

/**
  * call the callback function
  *
  * @param args argument use for call the callback function
  */
  def apply(args : T): Unit = callback(args)
}


/** Class for defining an event.
 *
 * This class can be used to create :
 * - child classes for events specific to a single actor
 * - child objects for more global events
 * */
class Event[T] {
  type Callback = T => Unit

  private var slots = ArrayBuffer[Connection[T]]()

  /** Connect a function to an event. When the event is call,
   * all the functions connected are called
   *
   * @param callback the function to call
   * @return a Connection, this connection it's important if you want disconnect to the event
   * */
  def connect(callback : Callback): Connection[T]={
    var connection = Connection(this,callback)
    slots += connection
    connection
  }



  /** Disconnet to an event,
   * for common user, use the disconnect function of the
   * connection return by the connect function
   *
   * @param connection connection to disconnect of this event
   * */
  def disconnect(connection : Connection[T]): Unit={
    slots -= connection
  }


  /** Call the event with the parameters necessary
   * and call all the function connected
   *
   * @param args arguments for call the event
   * */
  def apply(args : T): Unit ={
    slots.foreach(_(args))
  }

}
