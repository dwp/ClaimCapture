package models.yesNo

import models.MultiLineAddress

case class YesNoMandWithAddress(answer: String = "", address: Option[MultiLineAddress] = None, postCode: Option[String] = None)
