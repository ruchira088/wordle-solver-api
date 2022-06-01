package com.ruchij.services.solver.models

import com.ruchij.services.solver.models.Trie.Node

sealed trait Trie[A] { self =>
  type TrieImpl[x] <: Trie[x]

  val children: Map[A, Node[A]]

  def add(list: List[A]): TrieImpl[A] =
    list match {
      case Nil => updateChildren { Map.empty }

      case x :: Nil =>
        val childNode = children.get(x).map(_.copy(hasValue = true)).getOrElse(Node(x, hasValue = true, Map.empty[A, Node[A]]))
        updateChildren { children ++ Map(x -> childNode) }

      case x :: xs =>
        val childNode = children.getOrElse(x, Node(x, hasValue = false, Map.empty[A, Node[A]]))
        updateChildren { children ++ Map(x -> childNode.add(xs)) }
    }

  def find(list: List[A]): Boolean


  protected def updateChildren(children: Map[A, Node[A]]): TrieImpl[A]
}

object Trie {
  case class Node[A](current: A, hasValue: Boolean, children: Map[A, Node[A]]) extends Trie[A] { self =>
    override type TrieImpl[x] = Node[x]

    override def updateChildren(children: Map[A, Node[A]]): Node[A] =
      self.copy(children = children)

    override def find(list: List[A]): Boolean =
      list match {
        case x :: xs => children.get(x).exists(_.find(xs))
        case Nil => hasValue
      }
  }

  case class Root[A](children: Map[A, Node[A]]) extends Trie[A] {
    override type TrieImpl[x] = Root[x]

    override def updateChildren(children: Map[A, Node[A]]): Root[A] =
      Root(children)

    override def find(list: List[A]): Boolean =
      list match {
        case x :: xs => children.get(x).exists(_.find(xs))
        case _ => false
      }
  }

  def apply[A](iterables: Iterable[Iterable[A]]): Root[A] =
    iterables.foldLeft(Root[A](Map.empty)) {
      case (root, values) => root.add(values.toList)
    }

}
