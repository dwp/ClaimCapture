package jmx.claiminspector

import org.joda.time.DateTime

case class ClaimSubmitted(start: DateTime, end: DateTime)

case object GetClaimStatistics

case class ClaimStatistics(numberOfClaims: Int, averageTime: Int)

case object GetSessionCount

case object RefererRedirect

case object GetRefererRedirects

case object FastClaimDetected

case object GetFastClaimsDetected