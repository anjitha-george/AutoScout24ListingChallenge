package models

import scala.collection.mutable
import scala.io.Source

case class Listing(
                    id: Int,
                    make: String,
                    price: Int,
                    mileage: Int,
                    seller_type: String
                  )

object Listing {

  def readCsv(fileName: String): List[Listing] = {
    val listingSource = Source.fromFile(fileName)
    val listingIterator = listingSource.getLines()
    var listingList: List[Listing] = List()
    for (line <- listingIterator.drop(1)) {
      val Array(id, make, price, mileage, seller_type) = line.split(",").map(_.trim)
      val listing = Listing(id.toInt, make.replace("\"", ""), price.toInt, mileage.toInt, seller_type.replace("\"", ""))
      listingList = listingList.::(listing)
    }
    listingSource.close()
    listingList
  }
}