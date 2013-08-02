package utils.pageobjects

/**
 * Builds a ClaimScenario from a CSV input file, which should follow tests.resources.ClaimScenarioTemplate.csv format.
 * @author Jorge Migueis
 * Date: 02/08/2013
 */
object ClaimScenarioFileReader {

  def buildClaimFromFile(fileName: String) = {
    val claim = new ClaimScenario
    FactoryFromFile.buildFromFile(fileName, claim.updateDynamic)
    claim
  }
}
