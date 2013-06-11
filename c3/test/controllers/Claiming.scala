package controllers

import java.util.UUID._
import org.specs2.specification.Scope

trait Claiming extends Scope {
  val claimKey = randomUUID().toString
}