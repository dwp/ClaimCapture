package services

import app.ConfigProperties._
import controllers.mappings.Mappings
import models.domain._
import models.view.CachedClaim
import play.api.Logger
import play.api.i18n.Lang
import services.mail.{EmailWrapper, EmailActors}

import scala.language.existentials
import play.modules.mailer._
import play.api.i18n.{MMessages => Messages}

object CadsEmail {
  def send(transactionID:String, subject:String,body:String, r:String*) = {
    //EmailAddress object uses javax.mail.internet.InternetAddress behind scenes. On parameters EmailAddress("Peter","peter@gmail.com") will generate "Peter <peter@gmail.com>" on the email sending message
    //As we don't want to send emails, I tried "","email@email.com" but that produces " <email@email.com>", so we have to send null in the first parameter so the email on the output is just what we are sending.
    val recipients = r.map(to => Recipient(RecipientType.TO,EmailAddress(null,to)))
    Logger.debug(s"Sending email for transactionID:$transactionID")
    EmailActors.manager ! EmailWrapper(transactionID,Email(subject = subject,from = EmailAddress(null,getProperty("mailer.from","noreply@lab.3cbeta.co.uk")),text = "",htmlText = body,None,recipients))
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
        implicit val lang = if(isWelsh) Lang("cy") else Lang("en")

        CadsEmail.send(claim.transactionId.getOrElse(""),subject = Messages("subject.claim"),body = views.html.mail(claim,isClaim = true,isEmployment(claim)).body,email)
      case _ => Logger.error("Can't send claim email, email not pressent")
    }
  }

  private def sendCofcEmail(claim:Claim) = {
    claim.questionGroup[CircumstancesDeclaration].get.email -> claim.lang match {
      case (Some(email),language) =>
        implicit val lang = language.getOrElse(Lang("en"))

        CadsEmail.send(claim.transactionId.getOrElse(""), subject = Messages("subject.cofc"),body = views.html.mail(claim,isClaim = false,isEmployment(claim)).body,email)
      case _ => Logger.error("Can't send changes email, email not present")
    }
  }

  def sendEmail(claim:Claim) = {
    claim.key match {
      case CachedClaim.key => EmailServices.sendClaimEmail(claim)
      case _ => EmailServices.sendCofcEmail(claim)
    }
  }

}
