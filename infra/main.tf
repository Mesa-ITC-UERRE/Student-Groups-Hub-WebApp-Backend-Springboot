resource "azurerm_resource_group" "main" {
  name     = "rg-${var.project_name}-${var.environment}"
  location = var.location
}

resource "azurerm_container_registry" "acr" {
  name                = "acr${var.project_name}${var.environment}"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  sku                 = "Basic"
  admin_enabled       = true
}

resource "azurerm_log_analytics_workspace" "main" {
  name                = "law-${var.project_name}-${var.environment}"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  sku                 = "PerGB2018"
  retention_in_days   = 30
}

resource "azurerm_container_app_environment" "main" {
  name                       = "env-${var.project_name}-${var.environment}"
  resource_group_name        = azurerm_resource_group.main.name
  location                   = azurerm_resource_group.main.location
  log_analytics_workspace_id = azurerm_log_analytics_workspace.main.id
}

resource "azurerm_container_app" "app" {
  name                         = "ca-${var.project_name}-${var.environment}"
  container_app_environment_id = azurerm_container_app_environment.main.id
  resource_group_name          = azurerm_resource_group.main.name
  revision_mode                = "Single"

  registry {
    server               = azurerm_container_registry.acr.login_server
    username             = azurerm_container_registry.acr.admin_username
    password_secret_name = "acr-password"
  }

  secret {
    name  = "acr-password"
    value = azurerm_container_registry.acr.admin_password
  }
  secret {
    name="db-password"
    value = var.db_password
  }

  template {
    min_replicas = var.min_replicas
    max_replicas = var.max_replicas

    container {
      name   = "${var.project_name}-container"
      #image  = "mcr.microsoft.com/azuredocs/containerapps-helloworld:latest"
      image  = "${azurerm_container_registry.acr.login_server}/${var.project_name}:${var.image_tag}"
      cpu    = var.container_cpu
      memory = var.container_memory
      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = var.environment
      }
      env {
        name= "DB_URL"
        value = var.db_url
      }
      env {
        name="DB_USER"
        value= var.db_user
      }
      env {
        name = "db-password"
        secret_name = "db-password"
      }
    }
  }

  ingress {
    external_enabled = true
    target_port      = var.container_port
    traffic_weight {
      latest_revision = true
      percentage      = 100
    }
  }
}