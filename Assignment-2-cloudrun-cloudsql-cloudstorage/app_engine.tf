# create app engine
terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 3.53"
    }
  }
}
provider "google" {
    credentials = file("/home/gcpthree3/Assignment-2-cloudrun-cloudsql-cloudstorage/src/main/resources/service-account.json")

    project = "nimble-perigee-337406"
    region = "us-central1"
    zone = "us-central1-c"
}

resource "google_app_engine_application" "prithviappserver"  {
    location_id = "us-central"
}
