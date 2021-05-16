package controllers

import models.Listing
import play.api.mvc.{BaseController, ControllerComponents}

import java.nio.file.Paths
import javax.inject.{Inject, Singleton}
import scala.io.Source

@Singleton
class UploadController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def uploadPageLoad = Action {
    Ok(views.html.upload(""))
  }

  def upload = Action(parse.multipartFormData) {
    request =>
      val listing = request.body.file("Listing")
      val contact = request.body.file("Contact")
      if (listing.isEmpty || contact.isEmpty) {
        Ok(views.html.upload("Error : Please upload both files."))
      }
      else if (listing.get.filename.split("\\.").last!="csv" || contact.get.filename.split("\\.").last!="csv") {
        Ok(views.html.upload("Error : Please upload properly formatted csv files."))
      }
      else {
        val listingLine = (Source.fromFile(listing.get.ref.getAbsoluteFile).getLines.toSeq(0))
        val contactLine = (Source.fromFile(contact.get.ref.getAbsoluteFile).getLines.toSeq(0))
        if (!(listingLine.split(",").size.equals(5)) || !(contactLine.split(",").size.equals(2))) {
          Ok(views.html.upload("File Formats Incorrect"))
        }
        else {
          listing.get.ref.copyTo(Paths.get("public/listings.csv"), replace = true)
          contact.get.ref.copyTo(Paths.get("public/contacts.csv"), replace = true)
          Ok(views.html.upload("Files Uploaded Successfully."))
        }
      }
  }
}