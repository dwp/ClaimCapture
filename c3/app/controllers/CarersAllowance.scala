package controllers

import play.api.mvc._
import models.view.example._
import play.api.data.Form
import play.api.data.Forms._
import models.view.example.Section
import play.api.cache.Cache
import play.api.Play.current

object CarersAllowance extends Controller with CachedClaim {

  val benefitsForm =
    Form(
      mapping(
        "answer" -> boolean
      )(BenefitsForm.apply)(BenefitsForm.unapply)
    )

  def questionGroup1 = newClaim { claim => request =>
      Ok("")
  }

  def questionGroup1Submit = ActionWithClaim { implicit claim => implicit request =>
        val key = request.session.get("connected").get
        benefitsForm.bindFromRequest.fold(
          formWithErrors => BadRequest(views.html.carersAllowance("test")),
          inputForm => {
            Cache.set(key, updateClaim(claim, inputForm), 3600)
            Ok("Carer's Allowance - Command")
          })
  }

  def updateClaim(claim: Claim, form: BenefitsForm) = {
    Claim(claim.sections.map {
      section => {
        section.id match {
          case "s1" => {
            new Section("s1", section.forms.map {
              questionGroup => {
                questionGroup.id match {
                  case form.id => form.copy()
                  case _ => questionGroup
                }
              }
            })
          }

          case _ => section
        }
      }
    })
  }
}