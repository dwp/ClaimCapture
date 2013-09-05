package object app {

  object AccountStatus {
    val BankBuildingAccount = 'bankBuildingAccount
    val AppliedForAccount = 'appliedForAccount
    val NotOpenAccount = 'notOpenAccount
  }

  object ActingType {
    val Guardian = 'guardian
    val Attorney = 'attorney
    val Appointee = 'appointee
    val Judicial = 'judicial
    val Deputy = 'deputy
    val Curator = 'curator
  }

  object PaymentFrequency {
    val EveryWeek = 'everyWeek
    val FourWeekly = 'fourWeekly
  }

  object PensionPaymentFrequency {
    val Weekly = "02"
    val Fortnightly = "03"
    val FourWeekly = "04"
    val Monthly = "05"
    val Other = "other"

    def mapToHumanReadableString(code:String) = {
      code match {
        case Weekly => "Weekly"
        case Fortnightly => "Fortnightly"
        case FourWeekly => "Four-weekly"
        case Monthly => "Monthly"
        case Other => "Other"
        case _ => ""
      }
    }
  }

  object StatutoryPaymentFrequency {
    val Weekly = "weekly"
    val Fortnightly = "fortnightly"
    val FourWeekly = "fourWeekly"
    val Monthly = "monthly"
    val Other = "other"

    def mapToHumanReadableString(frequencyCode:String, otherCode:Option[String]): String = {

      frequencyCode match {
        case Weekly => "Weekly"
        case Fortnightly => "Fortnightly"
        case FourWeekly => "Four-weekly"
        case Monthly => "Monthly"
        case Other =>  otherCode match {
          case Some(s) => "Other: " + s
          case _ => "Other"
        } //+ paymentFrequency.other.getOrElse("")
        case _ => ""
      }
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
  }

  object WhoseNameAccount {
    val YourName = 'yourName
    val Yourpartner = 'partner
    val Both = 'bothNames
    val PersonActingBehalf = 'onBehalfOfYou
    val YouPersonBehalf = 'allNames
  }
}