package com.ruchij.web.requests

case class WordleConstraintsRequest(
  length: Int,
  limit: Option[Int],
  excludedLetters: Option[Set[Char]],
  notInPosition: Option[Map[Int, Set[Char]]],
  inPosition: Option[Map[Int, Char]]
)

object WordleConstraintsRequest {
  val DefaultResultsCount = 10
}
