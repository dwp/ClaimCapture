package services

import app.ConfigProperties._
import app.XMLValues
import controllers.mappings.Mappings
import models.domain._
import models.view.CachedClaim
import models.yesNo.YesNoWithText
import play.api.Logger
import services.mail.{SaveForLaterEmailWrapper, EmailWrapper, EmailActors}

import scala.language.existentials
import play.modules.mailer._
import play.api.i18n.{I18nSupport, MMessages, MessagesApi, Lang}
import play.api.Play.current

object CadsEmail {
  def send(transactionID: String, subject: String, body: String, r: String*) = {
    //EmailAddress object uses javax.mail.internet.InternetAddress behind scenes. On parameters EmailAddress("Peter","peter@gmail.com") will generate "Peter <peter@gmail.com>" on the email sending message
    //As we don't want to send emails with the chevrons, I tried "","email@email.com" but that produces " <email@email.com>", so we have to send null in the first parameter so the email on the output is just what we are sending.
    val recipients = r.map(to => Recipient(RecipientType.TO, EmailAddress(null, to)))
    Logger.info(s"Sending email for transactionId [$transactionID]")
    EmailActors.manager ! EmailWrapper(transactionID, Email(subject = subject, from = EmailAddress(null, getProperty("mailer.from", "noreply@lab.3cbeta.co.uk")), text = "", htmlText = body, None, recipients))
  }

  def sendSaveForLater(transactionID: String, subject: String, body: String, r: String*) = {
    //EmailAddress object uses javax.mail.internet.InternetAddress behind scenes. On parameters EmailAddress("Peter","peter@gmail.com") will generate "Peter <peter@gmail.com>" on the email sending message
    //As we don't want to send emails with the chevrons, I tried "","email@email.com" but that produces " <email@email.com>", so we have to send null in the first parameter so the email on the output is just what we are sending.
    val recipients = r.map(to => Recipient(RecipientType.TO, EmailAddress(null, to)))
    Logger.info(s"Sending email for transactionId [$transactionID]")
    EmailActors.manager ! SaveForLaterEmailWrapper(transactionID, Email(subject = subject, from = EmailAddress(null, getProperty("mailer.from", "noreply@lab.3cbeta.co.uk")), text = "", htmlText = body, None, recipients))
  }
}

object EmailServices extends I18nSupport {
  val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  private def isEmployment(claim: Claim) = {
    (claim.questionGroup[Employment] match {
      case Some(employment) => employment.beenEmployedSince6MonthsBeforeClaim == Mappings.yes || employment.beenSelfEmployedSince1WeekBeforeClaim == Mappings.yes
      case _ => false
    }) ||
      claim.questionGroup[CircumstancesEmploymentChange].exists(_ => true) ||
      claim.questionGroup[CircumstancesSelfEmployment].exists(_ => true)

  }

  private def sendClaimEmail(claim: Claim) = {
    claim.questionGroup[ContactDetails] -> claim.questionGroup[AdditionalInfo] match {
      case (Some(contactDetails), Some(additionalInfo)) if contactDetails.email.isDefined =>
        val isWelsh = additionalInfo.welshCommunication == Mappings.yes
        implicit val lang = if (isWelsh) Lang("cy") else Lang("en")

        CadsEmail.send(claim.transactionId.getOrElse(""), subject = claimEmailSubject(claim), body = views.html.mail(claim, isClaim = true, isEmployment(claim)).body, contactDetails.email.get)
      case (Some(contactDetails), Some(additionalInfo)) if contactDetails.email.isEmpty =>
        Logger.info(s"Not sending claim email because the user didn't input an address for transid: [${claim.transactionId.getOrElse("id not present")}]")
      //We do nothing in this case, they have selected not to send email
      case _ => Logger.error(s"Can't send claim email, email not present for transactionId[${claim.transactionId.getOrElse("id not present")}]")
    }
  }

  def claimEmailSubject(claim: Claim) = (claim.questionGroup[Employment], claim.questionGroup[SelfEmploymentPensionsAndExpenses]) match {
    case (Some(Employment(XMLValues.yes, _)), _) | (Some(Employment(_, XMLValues.yes)), _) => messagesApi("subject.claim.employed")
    case (_, Some(SelfEmploymentPensionsAndExpenses(YesNoWithText(XMLValues.yes, _), _))) => messagesApi("subject.claim.employed")
    case _ => messagesApi("subject.claim.notemployed")
  }


  private def sendCofcEmail(claim: Claim) = {
    claim.questionGroup[CircumstancesReportChange] -> claim.lang match {
      case (Some(circumstancesRepChange), language) if circumstancesRepChange.email.isDefined =>
        implicit val lang = language.getOrElse(Lang("en"))

        CadsEmail.send(claim.transactionId.getOrElse(""), subject = messagesApi("subject.cofc"), body = views.html.mail(claim, isClaim = false, isEmployment(claim)).body, circumstancesRepChange.email.get)
      case (Some(circumstancesDecl), language) if circumstancesDecl.email.isEmpty => //We do nothing in this case, they have selected not to send email
      case _ => Logger.error(s"Can't send changes email, email not present for transactionId[${claim.transactionId.getOrElse("id not present")}]")
    }
  }

  def sendEmail(claim: Claim) = {
    claim.key match {
      case CachedClaim.key => sendClaimEmail(claim)
      case _ => sendCofcEmail(claim)
    }
  }

  def sendSaveForLaterEmail(claim: Claim) = {
    claim.questionGroup[ContactDetails] match {
      case Some(contactDetails) => {
        if (contactDetails.email.isEmpty) Logger.info(s"Not sending save-for-later email because the user didn't input an address for transid: [${claim.transactionId.getOrElse("id not present")}]")
        else CadsEmail.send(claim.transactionId.getOrElse(""), subject = saveForLaterEmailSubject, body = views.html.savedMail(claim).body, contactDetails.email.get)
      }
    }
  }

  def saveForLaterEmailSubject = {
    messagesApi("subject.claim.saved")
  }
}
