package services.submission

import scala.xml.{NodeSeq, Elem}

case class GGClaimSubmission(dwpClaim : NodeSeq) {

  val transactionId = dwpClaim \\ "DWPCAClaim" \ "@id"

  def createClaimSubmission: Elem = {

    // TODO : validateDwpClaim(dwpClaim)

    val dwpBody = buildDwpBody

    val xml = <GovTalkMessage xmlns="http://www.govtalk.gov.uk/CM/envelope"
                              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                              xsi:schemaLocation="http://www.govtalk.gov.uk/CM/envelope schema/gg/envelope-v2.0.xsd">
      <EnvelopeVersion>1.0</EnvelopeVersion>
      <Header>
        <MessageDetails>
          <Class>DWP-CA-CLAIM-0T0</Class>
          <Qualifier>request</Qualifier>
          <Function>submit</Function>
          <TransactionID>54593654563947</TransactionID>  <!-- ?? What is this ?? -->
          <CorrelationID/>
          <Transformation>XML</Transformation>
          <GatewayTimestamp/>
        </MessageDetails>
        <SenderDetails/>
      </Header>
      <GovTalkDetails>
        <Keys/>
        <ChannelRouting>
          <Channel>
            <URI>http://www.ezgov.com</URI>
            <Product>EzGov DWP-CA-CLAIM-0T0</Product>
            <Version>3.4</Version>
          </Channel>
        </ChannelRouting>
      </GovTalkDetails>
      <Body>
        {dwpBody}
      </Body>
    </GovTalkMessage>

    xml
  }

  def buildDwpBody = {

    <DWPBody xmlns="http://www.govtalk.gov.uk/dwp/ca/claim"
             xmlns:gds="http://www.govtalk.gov.uk/people/AddressAndPersonalDetails"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://www.govtalk.gov.uk/dwp/ca/claim schema/ca/dwp-ca-claim-v1_10.xsd">
      <DWPEnvelope>
        <DWPCAHeader>
          <TestMessage>1</TestMessage>
          <Keys>
            <Key type="DWPOfficeNumber">083</Key>
            <Key type="DWPOfficeReference">BS9576</Key>
          </Keys>
          <Language>en</Language>
          <DefaultCurrency>GBP</DefaultCurrency>
          <Manifest>
            <Reference>
              <Namespace>http://www.govtalk.gov.uk/dwp/ca/claim</Namespace>
              <SchemaVersion>1_10</SchemaVersion>
              <TopElementName>DWPBody</TopElementName>
            </Reference>
          </Manifest>
          <TransactionId>{transactionId}</TransactionId>
        </DWPCAHeader>
        {dwpClaim}
      </DWPEnvelope>
      {signDwpClaim(dwpClaim)}
    </DWPBody>
  }

  def signDwpClaim(dwpClaim :NodeSeq) = {
    <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
      <ds:SignedInfo>
        <ds:CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/>
        <ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#dsa-sha1"/>
        <ds:Reference URI={s"#{{transactionId}}"}>
          <ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
          <ds:DigestValue>SadVcIUbeepTfvhp2BzI2V3EPYo=</ds:DigestValue>
        </ds:Reference>
      </ds:SignedInfo>
      <ds:SignatureValue>V6NzTYMiickLrbenHakT1UTnawk7CxWpqPtOh++XyCpg94LlWT682A==</ds:SignatureValue>
    </ds:Signature>
  }
}
