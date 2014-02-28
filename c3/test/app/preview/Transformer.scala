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

case class MaritalTransformer(id:String)(implicit claim:TestData) extends Transformer{
  val rels = Map("m" -> "Married or civil partner","s" -> "Single","d" -> "Divorced or civil partnership dissolved","w" -> "Widowed or surviving civil partner","n" -> "Separated","p" -> "Living with partner")

  override def transform():String = {
    rels(claim.selectDynamic(id))
  }
}
