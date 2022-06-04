package com.ruchij.web.responses

import com.ruchij.services.solver.models.PossibleSolution

case class WordleSolutionResponse(solutions: Seq[PossibleSolution])
