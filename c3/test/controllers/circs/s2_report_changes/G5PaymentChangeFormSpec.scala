package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import models.SortCode

class G5PaymentChangeFormSpec extends Specification with Tags {
   "Report a change in your circumstances - Payment Change" should {

     val yes = "yes"
     val no = "no"
     val nameOfCurrentBank = "Nat West"
     val currentPaymentMethod = "Cheque"
     val accountHolderName = "Mr John Doe"
     val whoseNameIsTheAccountIn = "yourName"
     val bankFullName = "HSBC"
     val sortCode1 = "11"
     val sortCode2 = "22"
     val sortCode3 = "33"
     val sortCode = SortCode(sortCode1,sortCode2,sortCode3)
     val accountNumber = "12345678"
     val rollOrReferenceNumber = "My Ref: 123"
     val paymentFrequency = "weekly"
     val moreAboutChanges = "Some additional info goes here"

     "map data into case class" in {
       G5PaymentChange.form.bind(
         Map(
           "currentlyPaidIntoBank.answer" -> yes,
           "currentlyPaidIntoBank.text1" -> nameOfCurrentBank,
           "currentlyPaidIntoBank.text2" -> currentPaymentMethod,
           "currentPaymentMethod" -> currentPaymentMethod,
           "accountHolderName" -> accountHolderName,
           "whoseNameIsTheAccountIn" -> whoseNameIsTheAccountIn,
           "bankFullName" -> bankFullName,
           "sortCode.sort1" -> sortCode1,
           "sortCode.sort2" -> sortCode2,
           "sortCode.sort3" -> sortCode3,
           "accountNumber" -> accountNumber,
           "rollOrReferenceNumber" -> rollOrReferenceNumber,
           "paymentFrequency" -> paymentFrequency,
           "moreAboutChanges" -> moreAboutChanges
         )
       ).fold(
         formWithErrors => {
           "This mapping should not happen." must equalTo("Error")
         },
         f => {
           f.currentlyPaidIntoBank.answer must equalTo(yes)
           f.currentlyPaidIntoBank.text1.get must equalTo(nameOfCurrentBank)
           f.currentlyPaidIntoBank.text2.get must equalTo(currentPaymentMethod)
           f.accountHolderName must equalTo(accountHolderName)
           f.whoseNameIsTheAccountIn must equalTo(whoseNameIsTheAccountIn)
           f.bankFullName must equalTo(bankFullName)
           f.sortCode must equalTo(sortCode)
           f.accountNumber must equalTo(accountNumber)
           f.rollOrReferenceNumber must equalTo(rollOrReferenceNumber)
           f.paymentFrequency must equalTo(paymentFrequency)
           f.moreAboutChanges.get must equalTo(moreAboutChanges)
         }
       )
     }

     "mandatory fields must be populated" in {
       G5PaymentChange.form.bind(
         Map(
           "moreAboutChanges" -> ""
         )
       ).fold(
           formWithErrors => {
             formWithErrors.errors.length must equalTo(10)
             formWithErrors.errors(0).message must equalTo("error.required")
             formWithErrors.errors(1).message must equalTo("error.required")
             formWithErrors.errors(2).message must equalTo("error.required")
             formWithErrors.errors(3).message must equalTo("error.required")
             formWithErrors.errors(4).message must equalTo("error.required")
             formWithErrors.errors(5).message must equalTo("error.required")
             formWithErrors.errors(6).message must equalTo("error.required")
             formWithErrors.errors(7).message must equalTo("error.required")
             formWithErrors.errors(8).message must equalTo("error.required")
             formWithErrors.errors(9).message must equalTo("error.required")
           },
           f => "This mapping should not happen." must equalTo("Valid")
         )
     }

     "mandatory fields must be populated when currentlyPaidIntoBank is yes" in {
       G5PaymentChange.form.bind(
         Map(
           "currentlyPaidIntoBank.answer" -> yes
         )
       ).fold(
           formWithErrors => {
             formWithErrors.errors.length must equalTo(10)
             formWithErrors.errors(0).message must equalTo("required")
             formWithErrors.errors(1).message must equalTo("error.required")
             formWithErrors.errors(2).message must equalTo("error.required")
             formWithErrors.errors(3).message must equalTo("error.required")
             formWithErrors.errors(4).message must equalTo("error.required")
             formWithErrors.errors(5).message must equalTo("error.required")
             formWithErrors.errors(6).message must equalTo("error.required")
             formWithErrors.errors(7).message must equalTo("error.required")
             formWithErrors.errors(8).message must equalTo("error.required")
             formWithErrors.errors(9).message must equalTo("error.required")
           },
           f => "This mapping should not happen." must equalTo("Valid")
         )
     }

     "mandatory fields must be populated when currentlyPaidIntoBank is no" in {
       G5PaymentChange.form.bind(
         Map(
           "currentlyPaidIntoBank.answer" -> no
         )
       ).fold(
           formWithErrors => {
             formWithErrors.errors.length must equalTo(10)
             formWithErrors.errors(0).message must equalTo("required")
             formWithErrors.errors(1).message must equalTo("error.required")
             formWithErrors.errors(2).message must equalTo("error.required")
             formWithErrors.errors(3).message must equalTo("error.required")
             formWithErrors.errors(4).message must equalTo("error.required")
             formWithErrors.errors(5).message must equalTo("error.required")
             formWithErrors.errors(6).message must equalTo("error.required")
             formWithErrors.errors(7).message must equalTo("error.required")
             formWithErrors.errors(8).message must equalTo("error.required")
             formWithErrors.errors(9).message must equalTo("error.required")
           },
           f => "This mapping should not happen." must equalTo("Valid")
         )
     }

   } section("unit", models.domain.CircumstancesSelfEmployment.id)
 }
