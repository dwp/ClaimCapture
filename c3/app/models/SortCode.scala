package models

case class SortCode(sort1: String, sort2: String, sort3: String) {

  def fold[B](empty: => B)(s: (String, String, String) => B) = {
    if (sort1.isEmpty && sort2.isEmpty && sort3.isEmpty) empty
    else s(sort1, sort2, sort3)
  }

  def stringify = sort1 + sort2 + sort3
}
