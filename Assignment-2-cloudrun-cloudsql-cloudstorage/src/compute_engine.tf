# create compute engine
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

resource "google_compute_instance" "appserver" {
    name = "prithvi-application-server"
    machine_type = "f1-micro"

    boot_disk {
      initialize_params{
          image = "debian-cloud/debian-9"
      }
    }
  network_interface {
    network = "default"
  }
}