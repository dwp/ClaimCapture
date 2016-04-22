package utils.pageobjects.preview

import utils.pageobjects.xml_validation.{ClaimValue, XMLBusinessValidation}
import utils.pageobjects.{PageObjectException, TestData}

import scala.xml.Elem

case class PreviewTestableData(ids:Seq[String] = Seq(),transforms:Seq[(String,(String)=>String)] = Seq()){

  def +(id:String) = PreviewTestableData(ids :+ id,transforms)

  def +(transform:(String,(String)=>String)) = PreviewTestableData(ids, transforms :+ transform)

}


class PreviewBusinessValidation(testableData:PreviewTestableData) extends XMLBusinessValidation {

  override def validateXMLClaim(claim: TestData, htmlString: String, throwException: Boolean): List[String] = {

    val list = testableData.ids.map(Left(_)) ++ testableData.transforms.map(Right(_))

    val lowerCaseHtmlString = htmlString.toLowerCase

    list.foreach{
      case Left(id) => validate(claim,lowerCaseHtmlString,id)
      case Right(tuple) => validate(claim,lowerCaseHtmlString,tuple._1,Some(tuple._2))
    }


    if (errors.nonEmpty && throwException) throw new PageObjectException("XML validation failed", errors.toList)
    errors.toList
  }

  private def validate(claim: TestData, htmlString: String,id:String,transform:Option[(String)=>String] = None) = {
    try{

      val default = claim.selectDynamic(id)
      if (default != null){
        val defLower = default.toLowerCase
        val claimData = transform.getOrElse((p:String) => p).apply(defLower)

        if (!htmlString.contains(claimData)) {
          errors += s"$id with data '$claimData' not found in html"
        }
      }

    }catch{
      case e:Exception =>
        errors += id + " Error: " + e.getMessage
    }

  }



  override def objValue(attribute: String, value: String, question: String) = {
    ClaimValue(attribute,value,question)
  }

  override def validateXMLClaim(claim: TestData, xml: Elem, throwException: Boolean): List[String] = {
    super.validateXMLClaim(claim, xml.mkString, throwException)

  }
}
