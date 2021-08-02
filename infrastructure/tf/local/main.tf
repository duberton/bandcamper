provider "aws" {
  region                      = "us-east-1"
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true

  endpoints {
    ses = "http://localhost:4566"
  }

}

resource "aws_ses_email_identity" "example" {
  email = "duberton@gmail.com"
}
