package models.view.example

abstract class Form

case class BenefitsForm(hasBenefits: Option[Boolean] = None) extends Form

case class HoursCaringForm(enoughHours: Option[Boolean] = None) extends Form

