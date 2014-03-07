package models.domain



case class PreviewModel(showEmail:String,email:Option[String]) extends QuestionGroup(PreviewModel)

object PreviewModel extends QuestionGroup.Identifier {
  val id = s"s12.g1"
}

