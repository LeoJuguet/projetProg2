package event

import scala.collection.mutable.ArrayBuffer


/* Class for save connection between a function and a event
 * */
class Connection[T](var event : Event[T], var callback: T => Unit){
  type Callback = T => Unit

  def disconnect()=
    event.disconnect(this)


  def apply(args : T): Unit = callback(args)
}


/* Class for define Event
 * */
class Event[T] {
  type Callback = T => Unit

  private var slots = ArrayBuffer[Connection[T]]()

  /* Connect a function to an event. When the event is call,
   * all the functions connected are called
   *
   * @param callback the function to call
   * @return a Connection, this connection it's important if you want disconnect to the event
   * */
  def connect(callback : Callback): Unit={
    var connection = Connection(this,callback)
    slots += connection
    connection
  }



  /* Disconnet to an event,
   * for common user, use the disconnect function of the
   * connection return by the connect function
   *
   * */
  def disconnect(connection : Connection[T])={
    slots -= connection
  }


  /* Call the event with the parameters necessary
   * and call all the function connected
   *
   * @param args arguments
   * */
  def apply(args : T): Unit ={
    slots.foreach(_(args))
  }

}
