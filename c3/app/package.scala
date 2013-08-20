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
  }

  object StatutoryPaymentFrequency {
    val Weekly = "W"
    val Fortnightly = "FN"
    val FourWeekly = "4W"
    val Monthly = "M"
    val Other = "other"

    def optionToString(paymentFrequencyOption: Option[models.PaymentFrequency]):String = {

      def stringify(paymentFrequency: models.PaymentFrequency):String = {
        paymentFrequency.frequency match {
          case Weekly => "Weekly"
          case Fortnightly => "Fortnightly"
          case FourWeekly => "Four-weekly"
          case Monthly => "Monthly"
          case Other => "Other: " + paymentFrequency.other.getOrElse("")
          case _ => ""
        }
      }

      paymentFrequencyOption match {
        case Some(s) => stringify(s)
        case _ => ""
      }
    }
  }

  object XMLValues {
    val NotAsked = "Not asked"
    val NotKnown = "Not known"
  }

}


