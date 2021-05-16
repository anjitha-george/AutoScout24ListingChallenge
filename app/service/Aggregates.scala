package service

import models.{Contact, Listing}

import java.text.{DecimalFormat, DecimalFormatSymbols}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Aggregates(val listingList: List[Listing], val contactList: List[Contact]) {
  val formatterCurrency = new DecimalFormat("€ ###,###.-")
  val dfs = new DecimalFormatSymbols
  dfs.setCurrencySymbol("€")
  dfs.setGroupingSeparator('.')
  dfs.setDecimalSeparator(',')
  formatterCurrency.setDecimalFormatSymbols(dfs)

  def averagePricePerSellerType: mutable.Map[String, String] = {
    //**Average Listing Selling Price per Seller Type
    //group by seller Type
    val aggregatePricePerSellerType: mutable.Map[String, Array[Int]] = listingList.foldLeft(mutable.Map[String, Array[Int]]().withDefaultValue(Array(0, 0)))((res, v) => {
      val key = v.seller_type
      res(key) = Array(res(key)(0) + v.price, res(key)(1) + 1)
      res
    })
    //calculating average price by seller type
    val averagePricePerSellerType = mutable.Map[String, String]()
    for ((k, v) <- aggregatePricePerSellerType) {
      averagePricePerSellerType(k) = formatterCurrency.format(v(0).toDouble / v(1).toDouble)
    }
    averagePricePerSellerType
  }

  def listingPercentPerMake: mutable.Map[String, Double] = {
    //**Distribution (in percent) of available cars by Make
    //group by make
    val aggregateCountPerMake: mutable.Map[String, Int] = listingList.foldLeft(mutable.Map[String, Int]().withDefaultValue(0))((res, v) => {
      val key = v.make
      res(key) = res(key) + 1
      res
    })
    //calculate distribution of listings by make
    var listingPercentPerMake = mutable.Map[String, Double]()
    for ((k, v) <- aggregateCountPerMake) {
      val percentage = (v.toDouble / listingList.size.toDouble) * 100
      listingPercentPerMake(k) = (math rint percentage * 100) / 100
    }
    listingPercentPerMake = mutable.LinkedHashMap(listingPercentPerMake.toSeq.sortWith(_._2 > _._2): _*)
    listingPercentPerMake
  }

  def averagePriceMostContacted(percent: Int): String = {
    //**Average price of the {percent}% most contacted listings
    //calculating number of counts per listing
    var contactCountPerListing: mutable.Map[Int, Int] = contactList.foldLeft(mutable.Map[Int, Int]().withDefaultValue(0))((res, v) => {
      val key = v.listing_id
      res(key) = res(key) + 1
      res
    })
    contactCountPerListing = mutable.LinkedHashMap(contactCountPerListing.toSeq.sortWith(_._2 > _._2): _*)
    //filtering to only 30% most contacted listings
    val mostContactedListings: scala.collection.Set[Int] = contactCountPerListing.take(contactCountPerListing.size * percent / 100).keySet
    var totalPriceMostContacted: Double = 0
    for (listingId <- mostContactedListings) {
      for (listing <- listingList) {
        if (listing.id == listingId)
          totalPriceMostContacted += listing.price
      }
    }
    //calculating average price of listings
    val averagePriceMostContacted: String = formatterCurrency.format(totalPriceMostContacted / mostContactedListings.size)
    averagePriceMostContacted
  }

  def mostContactedListingPerMonth(maxListings: Int): mutable.Map[(Int, Int), ListBuffer[ListBuffer[Any]]] = {
    //**The Top {maxListings} most contacted listings per Month
    //collecting contact counts per listing and month
    val monthlyContactDetails = contactList.groupBy(contact => (contact.contact_month, contact.contact_year, contact.listing_id)).view.mapValues(_.size)
    var mostContactedListingPerMonth = mutable.Map[(Int, Int), ListBuffer[ListBuffer[Any]]]()
    for (monthlyContactDetailsEvent <- monthlyContactDetails) {
      //finding listing entry to enrich the data
      for (listing <- listingList) {
        if (monthlyContactDetailsEvent._1._3.equals(listing.id)) {
          val key = (monthlyContactDetailsEvent._1._1, monthlyContactDetailsEvent._1._2)
          if (!mostContactedListingPerMonth.contains(key))
            mostContactedListingPerMonth(key) = ListBuffer(ListBuffer(monthlyContactDetailsEvent._2, listing.id, listing.make, formatterCurrency.format(listing.price), listing.mileage))
          else
            mostContactedListingPerMonth(key).append(ListBuffer(monthlyContactDetailsEvent._2, listing.id, listing.make, formatterCurrency.format(listing.price), listing.mileage))
        }
      }
    }
    for (k <- mostContactedListingPerMonth.keys) {
      //sorting listings within a month by contact count
      mostContactedListingPerMonth(k) = mostContactedListingPerMonth(k).sortWith(_.head.toString.toInt > _.head.toString.toInt).take(maxListings)
    }
    //sorting month and year
    mostContactedListingPerMonth = mutable.LinkedHashMap(mostContactedListingPerMonth.toSeq.sortWith((x, y) => (x._1._2 < y._1._2) || ((x._1._2 == y._1._2) && (x._1._1 < y._1._1))): _*)
    mostContactedListingPerMonth
  }
}
