package com.ruchij.services.solver.models

case class Trie[A](hasValue: Boolean, children: Map[A, Trie[A]]) { self =>
  def add(list: List[A]): Trie[A] =
    list match {
      case Nil => Trie(hasValue = true, children)

      case x :: xs =>
        val childNode = children.getOrElse(x, Trie.empty)
        Trie(hasValue, children ++ Map(x -> childNode.add(xs)))
    }

  def find(list: List[A]): Boolean =
    list match {
      case Nil => hasValue
      case x :: xs => children.get(x).exists(_.find(xs))
    }
}

object Trie {
  def empty[A]: Trie[A] = Trie(hasValue = false, Map.empty)

  def apply[A](values: List[List[A]]): Trie[A] =
    values.foldLeft(Trie.empty[A]) {
      case (trie, values) => trie.add(values)
    }
}

