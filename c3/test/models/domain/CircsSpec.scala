package models.domain

import org.specs2.mutable.Specification

class CircsSpec extends Specification {
  "Circs" should {
    "honeypot" in {
      "returns false given CircumstancesDeclaration answered yes and honeyPot not filled" in {
        val circs = Circs().update(CircumstancesDeclaration(obtainInfoAgreement = "yes", obtainInfoWhy = None))

        val result = circs.honeyPot

        result must beFalse
      }

      "returns false given CircumstancesDeclaration answered no and honeyPot filled" in {
        val circs = Circs().update(CircumstancesDeclaration(obtainInfoAgreement = "no", obtainInfoWhy = Some("stuff")))

        val result = circs.honeyPot

        result must beFalse
      }

      "return true given CircumstancesDeclaration answered yes and honeypot filled in" in {
        val circs = Circs().update(CircumstancesDeclaration(obtainInfoAgreement = "yes", obtainInfoWhy = Some("stuff")))

        val result = circs.honeyPot

        result must beTrue
      }
    }
  }
}