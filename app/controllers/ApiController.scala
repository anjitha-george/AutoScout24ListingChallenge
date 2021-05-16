package controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import models.{Contact, Listing}
import play.api.libs.json.Json
import play.api.mvc._
import service.Aggregates

import java.text.{DecimalFormat, DecimalFormatSymbols}
import javax.inject._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ApiController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  //Read Listing csv to list of Objects
  val listingList: List[Listing] = Listing.readCsv("public/listings.csv")

  //Read Contact csv to list of Objects
  val contactsList: List[Contact] = Contact.readCsv("public/contacts.csv")

  def averagePricePerSellerType(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val aggregates: Aggregates = new Aggregates(listingList, contactsList)
    Ok(mapper.writeValueAsString(aggregates.averagePricePerSellerType))
  }

  def listingPercentPerMake(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val aggregates: Aggregates = new Aggregates(listingList, contactsList)
    Ok(mapper.writeValueAsString(aggregates.listingPercentPerMake))
  }

  def averagePriceMostContacted(percent: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val aggregates: Aggregates = new Aggregates(listingList, contactsList)
    Ok(mapper.writeValueAsString(aggregates.averagePriceMostContacted(percent)))
  }

  def mostContactedListingPerMonth(maxListing: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val aggregates: Aggregates = new Aggregates(listingList, contactsList)
    Ok(mapper.writeValueAsString(aggregates.mostContactedListingPerMonth(maxListing)))
  }
}
