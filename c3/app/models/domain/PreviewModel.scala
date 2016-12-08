package models.domain

case class PreviewModel(email:Option[String] = None) extends QuestionGroup(PreviewModel)

object PreviewModel extends QGIdentifier(id = s"s12.g1")

