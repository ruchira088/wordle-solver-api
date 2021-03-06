package com.ruchij.services.solver

import com.ruchij.services.solver.models.PossibleSolution

trait WordleSolver[F[_]] {
  def possibleSolutions(
    length: Int,
    limit: Int,
    excludedChars: Set[Char],
    notInPosition: Map[Int, Set[Char]],
    inPosition: Map[Int, Char]
  ): F[Seq[PossibleSolution]]
}
