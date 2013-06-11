package models

trait CreationTimeStamp {
  val created = System.currentTimeMillis()
}