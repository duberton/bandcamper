terraform {
  required_providers {
    argocd = {
      source  = "oboukili/argocd"
      version = "1.1.3"
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

  wait = true

  spec {
    project = "default"

    source {
      repo_url = "https://github.com/duberton/bandcamper"
      path     = "infrastructure/helm"
      helm {
        value_files = ["values.yaml"]
      }
    }

    destination {
      server    = "https://kubernetes.default.svc"
      namespace = "default"
    }
  }
}
