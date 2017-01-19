package utils.pageobjects

import play.api.test.TestBrowser

class PageObjectsContext(b:TestBrowser,i:IterationManager,p:Option[Page]) {

  val browser: TestBrowser = b
  var previousPage: Option[Page] = p
  val iterationManager = i
}

object PageObjectsContext {
  def apply(b:TestBrowser,i:IterationManager = IterationManager(),p:Option[Page] = None) = new PageObjectsContext(b,i,p)
}


class IterationManager{

  var iterationBlocks = Map[String,Int]()

  def apply(section:String):Int = {
    if (iterationBlocks.exists(_._1 == section)){
      iterationBlocks(section)
    }else{
      1
    }
  }

  def init() = {
    iterationBlocks = Map("Abroad" -> 1,"Breaks" -> 1,"Employment" -> 1)
    this
  }

  def increment(section:String):Int = {
    if (iterationBlocks.exists(_._1 == section)){
      iterationBlocks = iterationBlocks.filterNot(_._1 == section) ++ Map(section -> (iterationBlocks(section)+1))
      apply(section)
    }else{
      1
    }
  }
}

object IterationManager {
  val Abroad = "Abroad"
  val Breaks = "Breaks"
  val Employment = "Employment"
  val CircsBreaks="CircsBreaks"
  def apply() = new IterationManager().init()
}
