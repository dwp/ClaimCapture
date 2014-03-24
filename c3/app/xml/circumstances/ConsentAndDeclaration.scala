package xml.circumstances

import models.domain.{Claim, CircumstancesDeclaration}
import scala.xml.NodeSeq
import xml.XMLHelper._
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import play.api.i18n.{MMessages => Messages}
import controllers.Mappings

object ConsentAndDeclaration {
  def xml(circs: Claim): NodeSeq = {
    val declaration = circs.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())
    <Declaration>
      {
        var textLines = NodeSeq.Empty
        textLines ++= textLine(Messages("declaration.title"))
        textLines ++= textLine(Messages("circs.declaration.1.pdf"))
        for(i <- 2 to 4) {
          textLines ++= textLine((Messages(s"circs.declaration.$i")))
        }
        declaration.circsSomeOneElse match {
          case Some(n) =>
            textLines ++= textLine(Messages("circsSomeOneElse"), {booleanStringToYesNo(n)})
            declaration.nameOrOrganisation match {
              case Some(nOrO) => textLines ++= textLine(Messages("nameOrOrganisation")+". =", nOrO)
              case _ =>
            }
          case _ =>
        }
        textLines ++= textLine(Messages("confirm"), {booleanStringToYesNo(declaration.confirm)})
        textLines ++= textLine(Messages("obtainInfoAgreement"), declaration.obtainInfoAgreement)
        declaration.obtainInfoAgreement match{
          case Mappings.no => textLines ++= textLine("I don't agree to you obtaining information from any other persons or organisations because ", declaration.obtainInfoWhy)
          case _ =>
        }
        textLines ++= textLine("XML Generated at: "+{DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(DateTime.now())})
        textLines
      }
    </Declaration>
  }
}