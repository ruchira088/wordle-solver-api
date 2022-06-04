package com.ruchij.services.solver

import cats.Applicative
import com.ruchij.services.solver.LocalWordleSolver.characterCounts
import com.ruchij.services.solver.models.PossibleSolution

class LocalWordleSolver[F[_]: Applicative](wordsList: Seq[String]) extends WordleSolver[F] {

  override def possibleSolutions(
    length: Int,
    limit: Int,
    excludedChars: Set[Char],
    notInPosition: Map[Int, Set[Char]],
    inPosition: Map[Int, Char]
  ): F[Seq[PossibleSolution]] = {
    val words: Seq[String] = solutions(length, excludedChars, notInPosition, inPosition)
    val (results, max): (Map[String, Int], Int) = calculateScores(words, characterCounts(words))

    Applicative[F].pure {
      results.toList
        .sortBy { case (word, score) => (-score, word) }
        .take(limit)
        .map { case (word, score) => PossibleSolution(word, score * 100 / max) }
    }
  }

  private def calculateScores(words: Seq[String], chars: Map[Char, Int]): (Map[String, Int], Int) =
    words.foldLeft((Map.empty[String, Int], 0)) {
      case ((acc, max), word) =>
        val (_, sum) =
          word.toList.foldLeft((chars, 0)) {
            case (current @ (currentChars, currentSum), char) =>
              currentChars.get(char)
                .map { count => (currentChars ++ Map(char -> Math.max(1, count / 10)), currentSum + count) }
                .getOrElse(current)
          }

        acc ++ Map(word -> sum) -> Math.max(max, sum)
    }

  private def solutions(
    length: Int,
    excludedChars: Set[Char],
    notInPosition: Map[Int, Set[Char]],
    inPosition: Map[Int, Char]
  ): Seq[String] =
    wordsList
      .filter { word =>
        word.length == length &&
        excludedChars.forall(char => !word.contains(char)) &&
        notInPosition.forall {
          case (position, chars) =>
            word.drop(position).headOption.forall(char => !chars.contains(char))
        } &&
        notInPosition.values.flatten.forall(char => word.contains(char)) &&
        inPosition.forall {
          case (position, char) =>
            word.drop(position).headOption.contains(char)
        }
      }

}

object LocalWordleSolver {
  private def characterCounts(words: Seq[String]): Map[Char, Int] =
    words.foldLeft(Map.empty[Char, Int]) {
      case (acc, word) =>
        word.foldLeft(acc) {
          case (result, char) => result + (char -> (result.getOrElse(char, 0) + 1))
        }
    }
}
