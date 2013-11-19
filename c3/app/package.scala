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

  object Whereabouts {
    val AtHome = "At Home"
    val Hospital = "Hospital"
    val Holiday = "Holiday"
    val RespiteCare = "Respite Care"
    val CareHome = "Care Home"
    val NursingHome = "Nursing Home"
    val Other = "Other"
  }

  object PaymentFrequency {
    val EveryWeek = 'Weekly
    val FourWeekly = 'fourWeekly
  }

  object PensionPaymentFrequency {
    val Weekly = "Weekly"
    val Fortnightly = "Fortnightly"
    val FourWeekly = "Four-Weekly"
    val Monthly = "Monthly"
    val Other = "Other" // TODO [SKW] the xsd is inconsistent and needs changing as there is no value for other, so I just made up a value and Jorge will change the schema and can replace this with a sensible value.

    def mapToHumanReadableString(code: models.PensionPaymentFrequency): String = {
      mapToHumanReadableString(code.frequency)
    }

    def mapToHumanReadableString(code: String): String = {
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
    val Other = "Other"

    def mapToHumanReadableString(frequencyCode: String, otherCode: Option[String]): String = frequencyCode match {
      case Weekly => "Weekly"
      case Fortnightly => "Fortnightly"
      case FourWeekly => "Four-Weekly"
      case Monthly => "Monthly"
      case Other => "Other"
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
  }

  object WhoseNameAccount {
    val YourName = 'yourName
    val Yourpartner = 'partner
    val Both = 'bothNames
    val PersonActingBehalf = 'onBehalfOfYou
    val YouPersonBehalf = 'allNames
  }
}