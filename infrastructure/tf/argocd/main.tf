terraform {
  required_providers {
    argocd = {
      source  = "oboukili/argocd"
      version = "2.2.0"
    }
  }
}

provider "argocd" {
  server_addr = var.server_address
  insecure    = var.server_insecure_connection
  username    = var.server_username
  password    = var.server_password
}

resource "argocd_application" "helm" {
  metadata {
    name      = "bandcamper"
    namespace = "argocd"
    labels = {
      app = "bandcamper"
    }
  }

  spec {
    project = "default"

    sync_policy {
      automated = {
        allow_empty = true
      }
    }

    source {
      repo_url = "git@github.com:duberton/bandcamper.git"
      path     = "infrastructure/helm"
      helm {
        value_files = ["values.yaml"]
      }
    }

    destination {
      server    = var.server_address
      namespace = "default"
    }
  }
}
