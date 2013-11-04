package controllers.submission

object Signature {

  def signatureXml = {

    <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
      <ds:SignedInfo>
        <ds:CanonicalizationMethod Algorithm="http://www.w3.org/TR/2001/REC-xml-c14n-20010315"/>
        <ds:SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#dsa-sha1"/>
        <ds:Reference URI="DWPCATransaction">
          <ds:DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
          <ds:DigestValue>SadVcIUbeepTfvhp2BzI2V3EPYo=</ds:DigestValue>
        </ds:Reference>
      </ds:SignedInfo>
      <ds:SignatureValue>V6NzTYMiickLrbenHakT1UTnawk7CxWpqPtOh++XyCpg94LlWT682A==</ds:SignatureValue>
    </ds:Signature>

  }
}
