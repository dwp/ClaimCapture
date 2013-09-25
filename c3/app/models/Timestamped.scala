package models

trait Timestamped {
  val created = System.currentTimeMillis()
}