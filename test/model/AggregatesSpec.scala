package model

import models.{Contact, Listing}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import service.Aggregates

import scala.collection.mutable.ListBuffer

class AggregatesSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "Aggregates" must {
    "return the average listing price per seller type" in {
      val listingList = List(Listing(0, "BMW", 50000, 14251, "private"), Listing(1, "BMW", 60000, 14251, "private"), Listing(2, "Audi", 40000, 14251, "other"))
      val contactList = List(Contact(0, 12, 2020), Contact(0, 1, 2021))
      val aggregates = new Aggregates(listingList, contactList)
      val calculatedAveragePricePerSellerType = aggregates.averagePricePerSellerType
      assert(calculatedAveragePricePerSellerType.get("private").head.equals("€ 55.000,-"))
      assert(calculatedAveragePricePerSellerType.get("other").head.equals("€ 40.000,-"))
    }
  }

  "Aggregates" must {
    "return the average listing percent per make" in {
      val listingList = List(Listing(0, "BMW", 50000, 14251, "private"), Listing(1, "BMW", 60000, 14251, "private"), Listing(2, "Audi", 40000, 14251, "other"))
      val contactList = List(Contact(0, 12, 2020), Contact(0, 1, 2021))
      val aggregates = new Aggregates(listingList, contactList)
      val calculatedListingPercentPerMake = aggregates.listingPercentPerMake
      assert(calculatedListingPercentPerMake.get("BMW").head.equals(66.67))
      assert(calculatedListingPercentPerMake.get("Audi").head.equals(33.33))
    }
  }

  "Aggregates" must {
    "return the average price of most contacted listings" in {
      val listingList = List(Listing(0, "BMW", 50000, 14251, "private"), Listing(1, "BMW", 60000, 14251, "private"), Listing(2, "Audi", 40000, 14251, "other"))
      val contactList = List(Contact(0, 12, 2020), Contact(0, 12, 2020), Contact(0, 1, 2021), Contact(1, 1, 2021), Contact(1, 1, 2021), Contact(2, 1, 2021))
      val aggregates = new Aggregates(listingList, contactList)
      val calculatedAveragePriceMostContacted = aggregates.averagePriceMostContacted(70)
      assert(calculatedAveragePriceMostContacted.equals("€ 55.000,-"))
    }
  }

  "Aggregates" must {
    "return the most Contacted Listing Per Month" in {
      val listingList = List(Listing(0, "BMW", 50000, 14251, "private"), Listing(1, "BMW", 60000, 14252, "private"), Listing(2, "Audi", 40000, 14252, "other"))
      val contactList = List(Contact(0, 12, 2020), Contact(0, 12, 2020), Contact(0, 1, 2021), Contact(1, 1, 2021), Contact(1, 1, 2021), Contact(2, 1, 2021))
      val aggregates = new Aggregates(listingList, contactList)
      val calculatedAveragePriceMostContacted = aggregates.mostContactedListingPerMonth(5)
      assert(calculatedAveragePriceMostContacted.keySet.equals(Set((12,2020),(1,2021))))
      assert(calculatedAveragePriceMostContacted.get(12,2020).head.equals(ListBuffer(ListBuffer(2, 0, "BMW", "€ 50.000,-", 14251))))
      assert(calculatedAveragePriceMostContacted.get(1,2021).head.equals(ListBuffer(ListBuffer(2, 1, "BMW", "€ 60.000,-", 14252), ListBuffer(1, 0, "BMW", "€ 50.000,-", 14251), ListBuffer(1, 2, "Audi", "€ 40.000,-", 14252))))
    }
  }
}
