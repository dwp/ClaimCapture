package xml.claim

import models.domain._
import xml.XMLHelper._
import scala.Some
import xml.XMLComponent
import play.api.i18n.{MMessages => Messages}

object  Disclaimer extends XMLComponent{

  def xml(claim: Claim) = {

    val disclaimer = claim.questionGroup[models.domain.Disclaimer].getOrElse(models.domain.Disclaimer())

    <Disclaimer>
      <DisclaimerStatement>
        <Title>This is my claim for Carer's Allowance.</Title>
        <Content>{Messages("disclaimer.1")}</Content>
        <Content>{Messages("disclaimer.2")}</Content>
        <Content>{Messages("disclaimer.3")}</Content>
        <Content>{Messages("disclaimer.4")}</Content>
        <Content>{Messages("disclaimer.5")}</Content>
        <Content>{Messages("disclaimer.6")}</Content>
        <Content>{Messages("disclaimer.7", "", "")}</Content>
        <Content>{Messages("disclaimer.8", "", "")}</Content>
        <Content>{Messages("disclaimer.9", "", "")}</Content>
      </DisclaimerStatement>
      {question(<DisclaimerQuestion/>,"read", disclaimer.read)}
    </Disclaimer>
  }
}
