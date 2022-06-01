package com.ruchij.services.solver

trait WordleSolver[F[_]] {
  def possibleSolutions(
    length: Int,
    excludedChars: Set[Char],
    notInPosition: Map[Int, Set[Char]],
    inPosition: Map[Int, Char]
  ): F[Seq[String]]
}
