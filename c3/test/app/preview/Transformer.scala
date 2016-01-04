package app.preview

import utils.pageobjects.TestData
import org.joda.time.format.DateTimeFormat

trait Transformer{
  val id:String
  def transform():String
}

case class DateTransformer(id:String,patternIn:String="dd/MM/yyyy",patternOut:String = "dd MMMM, yyyy")(implicit claim:TestData) extends Transformer{
  override def transform(): String = DateTimeFormat.forPattern(patternIn).parseDateTime(claim.selectDynamic(id)).toString(patternOut)
}
case class AddressTransformer(id:String)(implicit claim:TestData) extends Transformer{
  override def transform(): String = claim.selectDynamic(id).replaceAll("&",",")
}

case class AnyYesTransformer(id:String)(implicit claim:TestData) extends Transformer{
  override def transform():String = {
    var valueSeq = Seq.empty[String]
    var i = 1

    while(claim.selectDynamic(id+"_"+i) != null){
      valueSeq = valueSeq.+:(claim.selectDynamic(id+"_"+i).toLowerCase)
      i = i+1
    }

    if(valueSeq.contains("yes")) "yes" else "no"
  }
}

case class PaymentTransformer(id:String)(implicit claim:TestData) extends Transformer{
  val rels = Map("bankBuildingAccount" -> "UK bank or building society account","appliedForAccount" -> "You don't have an account but intend to open one")

  override def transform():String = {
    rels(claim.selectDynamic(id))
  }
}

case class NumDetailsProvidedTransformer(id:String)(implicit claim:TestData) extends Transformer{
  override def transform():String = {
    var valueSeq = Seq.empty[String]
    var i = 1

    while(claim.selectDynamic(id+"_"+i) != null){
      valueSeq = valueSeq.+:(claim.selectDynamic(id+"_"+i).toLowerCase)
      i = i+1
    }
    val yesNum = valueSeq.map(a => if(a=="yes") 1 else 0).fold(0){(v1,v2) => v1+v2}
    s"Details provided for $yesNum break(s)"
  }
}

case class EmploymentDetailsTransformer(id:String,n:Int)(implicit claim:TestData) extends Transformer{
  val employerName = id
  val expenses = Seq("EmploymentDoYouPayForThingsToDoJob","EmploymentDoYouPayforAnythingNecessaryToDoYourJob")
  val pension = Seq("EmploymentDoYouPayForPensionExpenses")
  override def transform():String = {

    val anyExpenses = anyYes(expenses(0)+"_"+n,expenses(1)+"_"+n)
    val anyPension = anyYes(pension(0)+"_"+n)
    val details = s"Details provided for ${claim.selectDynamic(employerName+"_"+n).toLowerCase}"

    if (anyExpenses && !anyPension) details+", including expenses"
    else if (anyExpenses && anyPension) details+", including expenses and pension schemes"
    else if (!anyExpenses && anyPension) details+", including pension schemes"
    else details
  }

  def anyYes(ids:String*):Boolean = ids.map{claim.selectDynamic(_).toLowerCase}.contains("yes")

}


