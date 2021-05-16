package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

class UploadControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "UploadController GET" should {

    "render the upload page from a new instance of controller" in {
      val controller = new UploadController(stubControllerComponents())
      val upload = controller.uploadPageLoad().apply(FakeRequest(GET, "/upload"))

      status(upload) mustBe OK
      contentType(upload) mustBe Some("text/html")
      contentAsString(upload) must include("Upload Form")
    }

    "render the upload page from the application" in {
      val controller = inject[UploadController]
      val upload = controller.uploadPageLoad().apply(FakeRequest(GET, "/upload"))

      status(upload) mustBe OK
      contentType(upload) mustBe Some("text/html")
      contentAsString(upload) must include("Upload Form")
    }

    "render the upload page from the router" in {
      val request = FakeRequest(GET, "/upload")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Upload Form")
    }
  }
}
