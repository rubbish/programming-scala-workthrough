package ui

import observer._

abstract class Widget

trait Clickable {
  def click()
}

class Button(label: String) extends Widget with Clickable {
  def click() = {
    // logic to do some clicking stuff...
  }
}

trait ObservableClicks extends Clickable with Subject {
  abstract override def click() = {
    super.click
    notifyObservers
  }
}

class ObservableButton(label:String) extends Button(label) with Subject {
  override def click() = {
    super.click
    notifyObservers
  }
}

trait VetoableClicks extends Clickable {
  val maxAllowed = 1
  private var count = 0

  abstract override def click() = {
    if (count < maxAllowed) {
      super.click()
      count += 1
    }
  }

  def reset() = {
    count = 0
  }
}

class ButtonWithCallbacks(val label: String, val clickedCallbacks: List[() => Unit]) extends Widget {

  require(clickedCallbacks != null, "Callback list cannot be null")

  def this(label: String, clickedCallback: () => Unit) =
    this(label, List(clickedCallback))

  def this(label: String) = {
    this(label, Nil)
    println("Warning.. no callback")
  }

  def click() = {
    // pretend to click
    clickedCallbacks.foreach(f => f())
  }
}

class RadioButtonWithCallbacks(var on: Boolean, label: String, clickCallbacks: List[() => Unit]) extends ButtonWithCallbacks(label, clickCallbacks) {

  def this(on: Boolean, label: String, clickCallback: () => Unit) = this(on, label, List(clickCallback))

  def this(on: Boolean, label: String) = this(on, label, Nil)
}
