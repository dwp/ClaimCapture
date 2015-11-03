import play.api.i18n.{MMessages => Messages, Lang}
import play.api.Play
import play.twirl.api.Html
import scala.util.{Success, Try}

package object app {

  object AccountStatus {
    val BankBuildingAccount = "UK bank or building society account"
    val AppliedForAccount = "You don't have an account but intend to open one"
  }

  object ActingType {
    val Guardian = "Parent or guardian"
    val Attorney = "Attorney"
    val Appointee = "Appointee"
    val Judicial = "Judicial factor"
    val Deputy = "Deputy"
    val Curator = "Curator bonis"
  }

  /**
   * @deprecated This Whereabouts objects shouldn't be used anymore since all whereabouts are "Circs based" so either refactor that to be common
   *            for the entire application, or re-refactor to enable this one. (If you use this object for data input, nothing will work :D)
    */
  object Whereabouts {
    val Home = "Home"
    val Hospital = "Hospital"
    val Holiday = "Holiday"
    val RespiteCare = "Respite Care"
    val CareHome = "Care Home"
    val NursingHome = "Nursing Home"
    val Other = "Other"
  }

  object CircsBreaksWhereabouts {
    val Home = "At home"
    val Hospital = "In hospital"
    val Holiday = "On holiday"
    val RespiteCare = "In respite care"
    val SomewhereElse = "Somewhere else"

    def whereWasThePersonList(implicit lang: Lang) = {
      Seq(
        Hospital -> Html(Messages("circsBreaks.inhospital")),
        RespiteCare -> Html(Messages("circsBreaks.inrespitecare")),
        Holiday -> Html(Messages("circsBreaks.onholiday")),
        Home -> Html(Messages("circsBreaks.athome")),
        SomewhereElse -> Html(Messages("circsBreaks.somewhereelse")))
    }

    def whereWereYouList(implicit lang: Lang) = {
      Seq(
        Home -> Html(Messages("circsBreaks.athome")),
        Holiday -> Html(Messages("circsBreaks.onholiday")),
        Hospital -> Html(Messages("circsBreaks.inhospital")),
        SomewhereElse -> Html(Messages("circsBreaks.somewhereelse")))
    }

  }

  object PaymentFrequency {
    val EveryWeek = "Every week"
    val FourWeekly = "Every four weeks"
    val ThirteenWeekly = "Every thirteen weeks"
  }

  object PensionPaymentFrequency {
    val Weekly = "Weekly"
    val Fortnightly = "Fortnightly"
    val FourWeekly = "Four-Weekly"
    val Monthly = "Monthly"
    val Other = "Other" // TODO [SKW] the xsd is inconsistent and needs changing as there is no value for other, so I just made up a value and Jorge will change the schema and can replace this with a sensible value.
  }

  object StatutoryPaymentFrequency {
    val Weekly = "Weekly"
    val Fortnightly = "Fortnightly"
    val FourWeekly = "Four-Weekly"
    val DontKnowYet = "Dont-Know-Yet"
    val Monthly = "Monthly"
    val Other = "Other"

    private def mapToHumanReadableString(frequencyCode: String, otherCode: Option[String]): String = frequencyCode match {
      case Weekly => "Weekly"
      case Fortnightly => "Fortnightly"
      case FourWeekly => "Four-weekly"
      case DontKnowYet => "Don't know yet"
      case Monthly => "Monthly"
      case Other => otherCode match {
        case Some(s) => "Other: " + s
        case _ => "Other"
      } //+ paymentFrequency.other.getOrElse("")
      case _ => ""
    }

    def mapToHumanReadableString(paymentFrequencyOption: Option[models.PaymentFrequency]): String = paymentFrequencyOption match {
      case Some(s) => mapToHumanReadableString(s.frequency,None)
      case _ => ""
    }

    def mapToHumanReadableStringWithOther(paymentFrequencyOption: Option[models.PaymentFrequency]): String = paymentFrequencyOption match {
      case Some(s) => mapToHumanReadableString(s.frequency,s.other)
      case _ => ""
    }
  }

  object XMLValues {
    val NotAsked = "Not asked"
    val NotKnown = "Not known"
    val Yes = "Yes"
    val No = "No"
    val yes = "yes"
    val no = "no"
    val GBP = "GBP"
    val Other = "Other"
    val other = "other"
    val poundSign = "£"
  }

  object WhoseNameAccount {
    val YourName = "Your name"
    val Yourpartner = "Your partner's name"
    val Both = "Both you and your partner"
    val PersonActingBehalf = "Person acting on your behalf"
    val YouPersonBehalf = "You and the person acting on behalf"
  }

  object ReasonForBeingThere {
    val Home = "Home"
    val Holiday = "Holiday"
    val Work = "Work"
    val Other = "Other"
  }
  object MaritalStatus {
    val Married = "Married or civil partner"
    val Single = "Single"
    val Divorced = "Divorced or civil partnership dissolved"
    val Widowed = "Widowed or surviving civil partner"
    val Separated = "Separated"
    val Partner = "Living with partner"
  }
  val mb = 1024L * 1000L
  def convertToMB(bytes:Long) = {
    bytes / mb
  }

  def convertToBytes(megaBytes:Long) = {
    megaBytes * mb
  }

  object ConfigProperties  {
    def getProperty(property:String,default:Int) = Try(Play.current.configuration.getInt(property).getOrElse(default)) match { case Success(s) => s case _ => default}
    def getProperty(property:String,default:String) = Try(Play.current.configuration.getString(property).getOrElse(default)) match { case Success(s) => s case _ => default}
    def getProperty(property:String,default:Boolean) = Try(Play.current.configuration.getBoolean(property).getOrElse(default)) match { case Success(s) => s case _ => default}
    def getProperty(property:String,default:Long) = Try(Play.current.configuration.getLong(property).getOrElse(default)) match { case Success(s) => s case _ => default}
  }

  object ReportChange {
    val StoppedCaring = 'stoppedCaring
    val AddressChange = 'addressChange
    val SelfEmployment = 'selfEmployment
    val EmploymentChange = 'EmploymentChange
    val PaymentChange = 'paymentChange
    val AdditionalInfo = 'additionalInfo
    val BreakFromCaring = 'breakFromCaring
    val BreakFromCaringYou = 'breakFromCaringYou
  }
}