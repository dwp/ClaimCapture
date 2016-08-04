import play.api.i18n.{Lang, Messages}
import play.api.{Logger, Play}
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

    def whereWasThePersonList(implicit lang: Lang, messages: Messages) = {
      Seq(
        Hospital -> Html(messages("circsBreaks.inhospital")),
        RespiteCare -> Html(messages("circsBreaks.inrespitecare")),
        Holiday -> Html(messages("circsBreaks.onholiday")),
        Home -> Html(messages("circsBreaks.athome")),
        SomewhereElse -> Html(messages("circsBreaks.somewhereelse")))
    }

    def whereWereYouList(implicit lang: Lang, messages: Messages) = {
      Seq(
        Home -> Html(messages("circsBreaks.athome")),
        Holiday -> Html(messages("circsBreaks.onholiday")),
        Hospital -> Html(messages("circsBreaks.inhospital")),
        SomewhereElse -> Html(messages("circsBreaks.somewhereelse")))
    }

  }

  object PaymentFrequency {
    val EveryWeek = "Every week"
    val FourWeekly = "Every four weeks"
    val ThirteenWeekly = "Every thirteen weeks"
  }

  object PaymentTypes {
    val MaternityPaternity = "MaternityOrPaternityPay"
    val Adoption = "AdoptionPay"
    val FosteringAllowance = "FosteringAllowance"
    val LocalAuthority = "LocalAuthority"
    val Other = "Other"
  }

  object PensionPaymentFrequency {
    val Weekly = "Weekly"
    val Fortnightly = "Fortnightly"
    val FourWeekly = "Four-Weekly"
    val Monthly = "Monthly"
    val Other = "Other" // TODO [SKW] the xsd is inconsistent and needs changing as there is no value for other, so I just made up a value and Jorge will change the schema and can replace this with a sensible value.
  }

  object BreaksInCareGatherOptions {
    val You = "You"
    val DP = "DP"
  }

  object StatutoryPaymentFrequency {
    val Weekly = "Weekly"
    val Fortnightly = "Fortnightly"
    val FourWeekly = "Four-Weekly"
    val DontKnowYet = "Dont-Know-Yet"
    val Monthly = "Monthly"
    val Other = "Other"
    val ItVaries = "Other"

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
      case ItVaries => "It varies"
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
    def getIntProperty(property: String, throwError: Boolean = true): Int = getProperty(property, "Int", throwError).toInt

    def getStringProperty(property: String, throwError: Boolean = true): String = getProperty(property, "String", throwError).toString

    def getBooleanProperty(property: String, throwError: Boolean = true): Boolean = getProperty(property, "Boolean", throwError).toBoolean

    private def getProperty(property: String, propertyType: String, throwError: Boolean): String = {
      if (!throwError && Play.unsafeApplication == null) {
        defaultValue(propertyType)
      }
      else {
        (Play.current.configuration.getString(property), throwError) match {
          case (Some(s), _) => s.toString
          case (_, false) => defaultValue(propertyType)
          case (_, _) => {
            Logger.error("ERROR - getProperty failed to retrieve application property for:" + property)
            throw new IllegalArgumentException(s"ERROR - getProperty failed to retrieve application property for:$property")
          }
        }
      }
    }

    private def defaultValue(propertyType: String) = {
      propertyType match {
        case "String" => ""
        case "Int" => "-1"
        case "Boolean" => "false"
      }
    }
  }

  object ReportChange {
    val StoppedCaring = 'stoppedCaring
    val AddressChange = 'addressChange
    val EmploymentChange = 'EmploymentChange
    val PaymentChange = 'paymentChange
    val AdditionalInfo = 'additionalInfo
    val BreakFromCaring = 'breakFromCaring
    val BreakFromCaringYou = 'breakFromCaringYou
  }
}
