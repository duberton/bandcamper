terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
    kubernetes = "~> 1.11"
    http = {
      source  = "terraform-aws-modules/http"
      version = ">= 2.4.1"
    }
  }

  backend "s3" {
    key    = "bandcamper.tfstate"
    bucket = "bandcamper-tf"
    region = "us-east-1"
  }

}
