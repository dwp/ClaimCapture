package models

trait Timestamped {
  val created: Long = System.currentTimeMillis()
}