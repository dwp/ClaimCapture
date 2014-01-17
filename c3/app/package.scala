import play.api.Play
import scala.util.{Success, Try}

package object app {

  object AccountStatus {
    val BankBuildingAccount = "UK bank or building society account"
    val AppliedForAccount = "You don't have an account but intend to open one"
    val NotOpenAccount = "Other - you would like more information"
  }

  object ActingType {
    val Guardian = "Parent or guardian"
    val Attorney = "Attorney"
    val Appointee = "Appointee"
    val Judicial = "Judicial factor"
    val Deputy = "Deputy"
    val Curator = "Curator bonis"
  }

  object Whereabouts {
    val Home = "Home"
    val Hospital = "Hospital"
    val Holiday = "Holiday"
    val RespiteCare = "Respite Care"
    val CareHome = "Care Home"
    val NursingHome = "Nursing Home"
    val Other = "Other"
  }

  object PaymentFrequency {
    val EveryWeek = "Weekly"
    val FourWeekly = "Four-Weekly"
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
    val Monthly = "Monthly"
    val Other = "Other"
  }

  object XMLValues {
    val Yes = "Yes"
    val No = "No"
    val yes = "yes"
    val no = "no"
    val GBP = "GBP"
    val Other = "Other"
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

  val mb = 131072
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
}