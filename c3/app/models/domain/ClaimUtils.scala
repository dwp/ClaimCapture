package models.domain

import controllers.mappings.Mappings

object ClaimUtils {

  def pensionStatementsRequired(claim: Claim): Boolean = {
    var pensionsPaidInto = 0
    val jobs = claim.questionGroup[Jobs]
    if (jobs.isDefined && jobs.get.jobs.size > 0){
      for (job <- jobs.get.jobs){
        if (job.questionGroup[PensionAndExpenses].getOrElse(PensionAndExpenses()).payPensionScheme.answer == Mappings.yes)
          pensionsPaidInto += 1
      }
    }
    val hasEmploymentPension = if (pensionsPaidInto > 0) true else false
    val selfEmploymentPensionsAndExpenses = claim.questionGroup[SelfEmploymentPensionsAndExpenses].getOrElse(SelfEmploymentPensionsAndExpenses())
    val hasSelfEmploymentPension = selfEmploymentPensionsAndExpenses.payPensionScheme.answer == Mappings.yes
    hasEmploymentPension || hasSelfEmploymentPension
  }

}