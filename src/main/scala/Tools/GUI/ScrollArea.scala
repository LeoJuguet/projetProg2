package gui

import gui.*


class ScrollArea() extends UIComponent{

  //TODO: scrolbar
  var scrollBar = false

  var isScrollBarEnabled: Boolean = _
  var hasScrollBar: Boolean = _

  def isScrollbarVisible()=
    this.hasScrollBar && this.isScrollBarEnabled


  def enableScrollbar(enable: Boolean)={
    if(this.isScrollBarEnabled != enable){
      this.isScrollBarEnabled = enable

      var isVisible = this.isScrollbarVisible()
      //TODO
      //scrollBar.show(isVisible)
    }
  }



}
