package services

import app.ConfigProperties._
import controllers.mappings.Mappings
import models.domain._
import models.view.CachedClaim
import play.api.i18n.Lang
import services.mail.EmailActors

import scala.language.existentials
import play.modules.mailer._
import play.api.i18n.{MMessages => Messages}

object CadsEmail {
  def send(subject:String,body:String, r:String*) = {
    val recipients = r.map(to => Recipient(RecipientType.TO,EmailAddress(null,to)))

    EmailActors.manager ! Email(subject = subject,from = EmailAddress(null,getProperty("mailer.from","noreply@lab.3cbeta.co.uk")),text = "",htmlText = body,None,recipients)
  }
}
object EmailServices {

  private def isEmployment(claim:Claim) = {
    (claim.questionGroup[Employment] match {
      case Some(employment) => employment.beenEmployedSince6MonthsBeforeClaim == Mappings.yes || employment.beenSelfEmployedSince1WeekBeforeClaim == Mappings.yes
      case _ => false
    }) ||
      claim.questionGroup[CircumstancesEmploymentChange].exists(_ => true) ||
      claim.questionGroup[CircumstancesSelfEmployment].exists(_ => true)

  }

  private def sendClaimEmail(claim:Claim) = {
    claim.questionGroup[ContactDetails].get.email -> claim.questionGroup[AdditionalInfo].get.welshCommunication match {
      case (Some(email),welsh) =>
        val isWelsh = welsh == Mappings.yes
        implicit val lang = Lang("en")

        CadsEmail.send(subject = Messages("subject.claim"),body = views.html.mail(claim,isClaim = true,isEmployment(claim)).body,email)
      case _ =>
    }
  }

  private def sendCofcEmail(claim:Claim) = {
    claim.questionGroup[CircumstancesDeclaration].get.email -> claim.lang match {
      case (Some(email),welsh) =>
        val isWelsh = welsh == Some(Lang("cy"))
        CadsEmail.send(subject = Messages("subject.cofc"),body = views.html.mail(claim,isClaim = false,isEmployment(claim)).body,email)
      case _ =>
    }
  }

  def sendEmail(claim:Claim) = {
    claim.key match {
      case CachedClaim.key => EmailServices.sendClaimEmail(claim)
      case _ => EmailServices.sendCofcEmail(claim)
    }
  }

}
