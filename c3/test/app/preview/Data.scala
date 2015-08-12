package app.preview

import utils.pageobjects.{PageObjectsContext, TestData}
import org.openqa.selenium.By
import play.api.Logger
import scala.language.implicitConversions


case class Data(toReviewData:Seq[(String,Seq[Object])]){

  def assertReview(claim: TestData, context: PageObjectsContext,trace:Boolean = false):Boolean = {
    toReviewData.foreach {
      t =>
        import scala.collection.JavaConverters._
        var matchesAny = false
		val valueId = t._1 + "_value"
        val elems = context.browser.webDriver.findElements(By.xpath( s"""//td[@id="${valueId}"]""")).asScala

        if(trace) {
          Logger("PageObject").debug("")
          Logger("PageObject").debug(s"Checking for ${t._1} and found ${elems.size} elems")
        }

        elems.foreach {
          webElem =>
            val elemValue = webElem.getText.toLowerCase.replaceAll(" ", "")
            t._2.foreach {
              fieldId =>
                val claimValue = fieldId match {
                  case id: String => claim.selectDynamic(id)
                  case transformer: Transformer => transformer.transform()
                }

                if (claimValue != null && claimValue.length > 0) {
                  val modifiedClaimValue = claimValue.toLowerCase.replaceAll(" ", "")
                  if(trace) Logger.debug(s"$modifiedClaimValue in $elemValue ? ${elemValue.contains(modifiedClaimValue)}")
                  if (elemValue.contains(modifiedClaimValue)) matchesAny = true
                }

            }
        }

        if (!matchesAny){
          Logger("PageObject").error(s"${t._1} does not match its value ${t._2}")
          return false
        }

    }
    true
  }


}
object Data{
  def build(tuples: (String,Seq[Object])*) = new Data(tuples)

  implicit def displaying(s:String) = new {
    def displays(s2:String) = s -> Seq(s2)

    def displays(s2:Object*) = s -> s2

    def displays(t:Transformer) = s -> Seq(t)
  }
}
