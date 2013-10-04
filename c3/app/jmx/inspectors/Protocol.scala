package jmx.inspectors

import org.joda.time.DateTime

case class ClaimSubmitted(start: DateTime, end: DateTime)

case class ChangeOfCircsSubmitted(start: DateTime, end: DateTime)

case object GetClaimStatistics

case object GetChangeOfCircsStatistics

case class ClaimStatistics(numberOfClaims: Int, averageTime: Int)

case class ChangeOfCircsStatistics(numberOfChangeOfCircs: Int, averageTime: Int)

case object FastClaimDetected

case object FastChangeOfCircsDetected

case object GetFastClaimsDetected

case object GetFastChangeOfCircsDetected