terraform {
  required_providers {
    azurerm = {
        source = "hashicorp/azurerm"
        version = "~> 4.0"
    }
  }

   backend "azurerm" {
    resource_group_name  = "rg-terraform-state"
    storage_account_name = "storagehubitc"
    container_name       = "tfstate"
    key                  = "miapp.terraform.tfstate"
  }

}

provider "azurerm"{

    features {}
}