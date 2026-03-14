variable "subscription_id" {
  description = "ID de tu suscripción de Azure"
  type        = string
}

variable "project_name" {
  description = "Nombre corto del proyecto"
  type        = string
  default = "hubescolar"
}

variable "environment" {
  description = "Entorno a trabajar"
  type        = string
  default = "prod"
}

variable "location" {
  description = "Región de Azure donde se crearán los recursos"
  type        = string
  default = "eastus"
}

variable "image_tag" {
  description = "Tag de la imagen Docker a desplegar"
  type        = string
  default     = "latest"
}

variable "container_cpu" {
  description = "CPU para el contenedor (ej: 0.5, 1.0)"
  type        = number
  default = 0.5
}

variable "container_memory" {
  description = "Memoria para el contenedor (ej: 1Gi, 2Gi)"
  type        = string
  default = "1Gi"
}

variable "container_port" {
  description = "Puerto que expone tu app Java (ej: 8080)"
  type        = number
  default = 8080
}

variable "min_replicas" {
  description = "Mínimo de instancias corriendo"
  type        = number
  default     = 1
}

variable "max_replicas" {
  description = "Máximo de instancias para escalar"
  type        = number
  default     = 3
}