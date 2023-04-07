package gui

import sfml.system.*
import sfml.graphics.*

enum ClickStates:
  case CLICK_IDLE, CLICK_HOVER, CLICK_PRESSED

trait Clickable:
  /** Function call when it's clicked */
  var onClickedBind: () => Unit = () => {}
  /** Function call when it's pressed */
  var onPressedBind: () => Unit = () => {}
  /** Function call when it's released */
  var onReleasedBind: () => Unit = () => {}
  /** Function call when it's hovered */
  var onHoveredBind: () => Unit = () => {}
  /** Function call when it's unhovered */
  var onUnhoveredBind: () => Unit = () => {}

  def clickBounds: Rect[Float] = Rect[Float]()
  var clickState = ClickStates.CLICK_IDLE

  def onClicked() =
    onClickedBind()

  def onPressed() =
    onPressedBind()

  def onReleased() =
    onReleasedBind()

  def onHovered() =
    onHoveredBind()

  def onUnhovered() =
    onUnhoveredBind()

  def unFocused() = {}

  def updateClick(mousePos: Vector2[Float], leftMouse: Boolean): Boolean =
    if (this.clickBounds.contains(mousePos.x, mousePos.y)) {
      if (leftMouse) {
        this.clickState = ClickStates.CLICK_PRESSED
        this.onPressed()
        return true
      } else {
        if (this.clickState == ClickStates.CLICK_PRESSED){
          this.onClicked()
          this.onReleased()
        }
        this.clickState = ClickStates.CLICK_HOVER
        this.onHovered()
      }
    } else {
      if (leftMouse) this.unFocused()
      if (this.clickState != ClickStates.CLICK_IDLE) {
        this.clickState = ClickStates.CLICK_IDLE
        this.onUnhovered()
      }
    }
    return false
