data "aws_availability_zones" "az" {}

module "vpc" {
  source = "./module/vpc"

  name = local.vpc_name
  cidr = local.vpc_cidr

  azs = local.vpc_azs

  public_subnets  = local.public_subnet_cidrs
  private_subnets = local.private_subnet_cidrs

  enable_nat_gateway   = true // NAT Gateway per public subnets
  enable_dns_hostnames = true
  enable_dns_support   = true
}
