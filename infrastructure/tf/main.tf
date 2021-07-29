provider "helm" {
  kubernetes {
    config_context = "minikube"
    config_path    = "~/.kube/config"
  }
}

provider "kubernetes" {
  config_context = "minikube"
}

terraform {
  backend "local" {
  }
}

resource "helm_release" "bandcamper" {
  name       = "bandcamper"
  repository = ".."
  chart      = "bandcamper"
  version    = "1.16.0"
  namespace  = "default"
}
