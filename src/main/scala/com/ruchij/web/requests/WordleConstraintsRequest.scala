package com.ruchij.web.requests

case class WordleConstraintsRequest(
  length: Int,
  excludedLetters: Option[Set[Char]],
  notInPosition: Option[Map[Int, Set[Char]]],
  inPosition: Option[Map[Int, Char]]
)
