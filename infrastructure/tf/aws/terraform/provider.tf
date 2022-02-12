provider "aws" {
  region                  = local.region
  shared_credentials_file = local.aws_credentials_path
  profile                 = local.aws_profile

}

provider "http" {

}
