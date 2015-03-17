package services

import app.ConfigProperties._
import services.mail.EmailActors

import scala.language.existentials
import play.modules.mailer._

object EmailServices {

  private[services] val SUBJECT = "Carer's Allowance claim"
  private[services] val BODY = "Your claim has finished without trouble"

  def sendEmail = new {

    def to(r:String*) = {
      val recipients = r.map(to => Recipient(RecipientType.TO,EmailAddress(null,to)))

      EmailActors.manager ! Email(subject = SUBJECT,from = EmailAddress(null,getProperty("mailer.from","noreply@lab.3cbeta.co.uk")),text = BODY,htmlText = BODY,None,recipients)
    }
      //Email(subject = SUBJECT,getProperty("mailer.from","noreply@carersallowance.service.gov.uk"),r,bodyHtml = Some(BODY))

  }

}
