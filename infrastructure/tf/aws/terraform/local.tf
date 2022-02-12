locals {
  cidr_blocks_sucho = ["170.246.80.124/32"]

  # provider
  aws_credentials_path = var.aws_credentials_path != "" ? var.aws_credentials_path : "~/.aws/credentials"
  aws_profile          = var.aws_profile != "" ? var.aws_profile : "mfa"
  region               = var.region != "" ? var.region : "us-east-1"

  # vpc
  vpc_name                 = var.vpc_name != "" ? var.vpc_name : "bandcamper-vpc"
  vpc_azs                  = var.vpc_azs[0] != "" ? var.vpc_azs : ["us-east-1a", "us-east-1c"]
  vpc_cidr                 = var.vpc_cidr != "" ? var.vpc_cidr : "10.0.0.0/16"
  public_subnet_cidrs      = var.public_subnet_cidrs[0] != "" ? var.public_subnet_cidrs : ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  private_subnet_cidrs     = var.private_subnet_cidrs[0] != "" ? var.private_subnet_cidrs : ["10.0.100.0/24", "10.0.101.0/24", "10.0.102.0/24"]
  wait_for_cluster_timeout = var.wait_for_cluster_timeout != "" ? tonumber(var.wait_for_cluster_timeout) : 600

  # eks
  cluster_name                 = var.cluster_name != "" ? var.cluster_name : "bandcamper-eks"
  cluster_version              = var.cluster_version != "" ? var.cluster_version : "1.21"
  cluster_suffix               = var.cluster_suffix != "" ? var.cluster_suffix : local.cluster_name
  kubernetes_service_ipv4_cidr = var.kubernetes_service_ipv4_cidr != "" ? var.kubernetes_service_ipv4_cidr : "172.20.0.0/16"
  endpoint_private_access      = true
  endpoint_public_access       = true

  # node group
  webapp_ami_name_filter = var.webapp_ami_name_filter != "" ? var.webapp_ami_name_filter : "amazon-eks-node-${local.cluster_version}-v*"
  webapp_ami_owner_id    = var.webapp_ami_owner_id != "" ? var.webapp_ami_owner_id : "amazon"

  node_group_webapp_settings = {
    cluster_name  = aws_eks_cluster.eks.name
    node_role_arn = aws_iam_role.webapp.arn
    subnet_ids    = module.vpc.private_subnets
    scaling_config = {
      desired_size = var.node_group_desired_size != "" ? tonumber(var.node_group_desired_size) : 5
      min_size     = var.node_group_min_size != "" ? tonumber(var.node_group_min_size) : 5
      max_size     = var.node_group_max_size != "" ? tonumber(var.node_group_max_size) : 7
    }

    node_group_name = var.node_group_name != "" ? var.node_group_name : "bandcamper"
    ami_id          = data.aws_ami.webapp.id
    ami_type        = "AL2_x86_64"
    instance_types  = var.node_group_instance_types[0] != "" ? var.node_group_instance_types : ["t2.micro"]
    disk_size       = var.node_group_disk_size != "" ? var.node_group_disk_size : 20
    iam_role_id     = resource.aws_iam_role.webapp.id
    labels = {
      "app" : "bandcamper"
    }
  }



  # iam role
  ec2_principal            = "ec2.${data.aws_partition.current.dns_suffix}"
  eks_principal            = "eks.amazonaws.com"
  cluster_iam_role_name    = var.cluster_iam_role_name != "" ? var.cluster_iam_role_name : "cluster-iam-role"
  webapp_iam_role_name     = var.webapp_iam_role_name != "" ? var.webapp_iam_role_name : "bandcamper-iam-role"
  policy_arn_prefix        = "arn:${data.aws_partition.current.partition}:iam::aws:policy"
  iam_path                 = var.iam_path != "" ? var.iam_path : "/"
  iam_permissions_boundary = var.iam_permissions_boundary != "" ? var.iam_permissions_boundary : null

}
