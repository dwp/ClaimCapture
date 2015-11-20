package controllers

object FakeData {
  val `36Characters` = (1 to 36).foldLeft("x")((x, _) => x + "x").mkString

  val `101Characters` = (1 to 101).foldLeft("x")((x, _) => x + "x").mkString
}
