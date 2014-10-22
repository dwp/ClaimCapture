package services.mail

case class SendEmail(body: String = "" ,subject: String = "", to: Seq[String] = Seq())