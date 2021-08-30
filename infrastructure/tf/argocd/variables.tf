variable "server_address" {
  default = "localhost:8080"
  type    = string
}

variable "server_username" {
  default = "admin"
  type    = string
}

variable "server_password" {
  default = "admin"
  type    = string
}

variable "server_insecure_connection" {
  default = true
  type    = bool
}