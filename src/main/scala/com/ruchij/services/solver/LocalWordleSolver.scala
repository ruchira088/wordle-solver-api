package com.ruchij.services.solver

import cats.Applicative
import com.ruchij.services.solver.LocalWordleSolver.characterCounts
import com.ruchij.services.solver.models.Trie

class LocalWordleSolver[F[_]: Applicative](wordsList: Seq[String]) extends WordleSolver[F] {

  override def possibleSolutions(
    length: Int,
    excludedChars: Set[Char],
    notInPosition: Map[Int, Set[Char]],
    inPosition: Map[Int, Char]
  ): F[Seq[String]] = {
    val words: Seq[String] = solutions(length, excludedChars, notInPosition, inPosition)

    val chars: Seq[Char] =
      characterCounts(words).toSeq
        .sortBy { case (_, count) => -count }
        .map { case (char, _) => char }

    ???
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

  private def buildTrie(words: List[String]): Trie[String] = ???
}
