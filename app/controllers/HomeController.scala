package controllers

import models.{Contact, Listing}
import javax.inject._
import play.api.mvc._
import service.Aggregates
import java.text.{DecimalFormat, DecimalFormatSymbols}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    //Read Listing csv to list of Objects
    val listingList: List[Listing] = Listing.readCsv("public/listings.csv")

    //Read Contact csv to list of Objects
    val contactsList: List[Contact] = Contact.readCsv("public/contacts.csv")

    //currency formatting
    val formatterCurrency = new DecimalFormat("€ ###,###.-")
    val dfs = new DecimalFormatSymbols
    dfs.setCurrencySymbol("€")
    dfs.setGroupingSeparator('.')
    dfs.setDecimalSeparator(',')
    formatterCurrency.setDecimalFormatSymbols(dfs)

    val aggregates:Aggregates = new Aggregates(listingList,contactsList)
    val averagePricePerSellerType = aggregates.averagePricePerSellerType
    val listingPercentPerMake = aggregates.listingPercentPerMake
    val averagePriceMostContacted = aggregates.averagePriceMostContacted(30)
    val mostContactedListingPerMonth =aggregates.mostContactedListingPerMonth(5)

    Ok(views.html.index(averagePricePerSellerType, listingPercentPerMake, averagePriceMostContacted, mostContactedListingPerMonth))
  }
}
