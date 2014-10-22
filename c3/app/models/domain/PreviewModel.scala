package models.domain



case class PreviewModel(email:Option[String] = None) extends QuestionGroup(PreviewModel)

object PreviewModel extends QuestionGroup.Identifier {
  val id = s"s12.g1"
}

