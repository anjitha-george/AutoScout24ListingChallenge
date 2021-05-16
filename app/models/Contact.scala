package models
import org.joda.time.Instant

import scala.io.Source

case class Contact(
                    listing_id: Int,
                    contact_month: Int,
                    contact_year: Int
                  )

object Contact {

  def readCsv(fileName: String): List[Contact] = {
    val contactSource = Source.fromFile(fileName)
    val contactIterator = contactSource.getLines()
    var contactsList: List[Contact] = List()
    for (line <- contactIterator.drop(1)) {
      val Array(listingId, timestamp) = line.split(",").map(_.trim)
      val contact = Contact(listingId.toInt, Instant.ofEpochMilli(timestamp.toLong).toDateTime.getMonthOfYear, Instant.ofEpochMilli(timestamp.toLong).toDateTime.getYear)
      contactsList = contactsList.::(contact)
    }
    contactSource.close()
    contactsList
  }
}
