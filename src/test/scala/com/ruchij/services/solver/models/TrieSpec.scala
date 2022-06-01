package com.ruchij.services.solver.models

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class TrieSpec extends AnyFlatSpec with Matchers {

  "Trie" should "find a value" in {
    val trie = Trie(List("apple", "app").map(_.toList))

    trie.find("app".toList) mustBe true
    trie.find("apple".toList) mustBe true
    trie.find("appl".toList) mustBe false
    trie.find("ball".toList) mustBe false
  }

}
