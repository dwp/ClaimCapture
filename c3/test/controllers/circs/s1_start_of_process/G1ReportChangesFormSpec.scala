package controllers.circs.s1_start_of_process

import app.ReportChange._
import controllers.mappings.Mappings
import org.specs2.mutable.{Specification, Tags}

class G1ReportChangesFormSpec extends Specification with Tags {
   "Report a change in your circumstances - Change in circumstances" should {

     "map additionalInfo into case class" in {
       G1ReportChanges.form.bind(
         Map(
           "reportChanges" -> AdditionalInfo.name
         )
       ).fold(
         formWithErrors => "This mapping should not happen." must equalTo("Error"),
         f => {
           f.reportChanges must equalTo(AdditionalInfo.name)
         }
       )
     }

     "map breakFromcaring into case class" in {
       G1ReportChanges.form.bind(
         Map(
           "reportChanges" -> BreakFromCaring.name
         )
       ).fold(
         formWithErrors => "This mapping should not happen." must equalTo("Error"),
         f => {
           f.reportChanges must equalTo(BreakFromCaring.name)
         }
       )
     }

     "mandatory fields must be populated" in {
       G1ReportChanges.form.bind(
         Map("reportChanges" -> "")
       ).fold(
           formWithErrors => {
             formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
           },
           f => "This mapping should not happen." must equalTo("Valid")
         )
     }

   } section("unit", models.domain.CircumstancesSelfEmployment.id)
 }
