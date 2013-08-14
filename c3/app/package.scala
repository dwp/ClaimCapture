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

  object XMLValues {
    val NotAsked = "Not asked"
    val NotKnown = "Not known"
  }

}


