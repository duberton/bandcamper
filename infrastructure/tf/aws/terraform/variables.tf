# provider
variable "aws_credentials_path" {
  type        = string
  description = "Path to AWS credential configuration file"
  default     = ""
}

variable "aws_profile" {
  type        = string
  description = "AWS profile configuration. Refers to AWS_PROFILE"
  default     = ""
}

variable "region" {
  type    = string
  default = "" //
}

# vpc
variable "vpc_name" {
  type    = string
  default = ""
}

variable "vpc_azs" {
  type    = list(string)
  default = [""]
}

variable "vpc_cidr" {
  type        = string
  default     = ""
  description = "CIDR block for VPC"
}

variable "public_subnet_cidrs" {
  type        = list(string)
  description = "CIDR blocks for public subnet in VPC"
  default     = [""]
}

variable "private_subnet_cidrs" {
  type        = list(string)
  description = "CIDR blocks for private subnet in VPC"
  default     = [""]
}

# eks
variable "cluster_version" {
  type    = string
  default = ""
}

variable "cluster_name" {
  type    = string
  default = ""
}

variable "cluster_suffix" {
  type    = string
  default = ""
}

variable "kubernetes_service_ipv4_cidr" {
  type        = string
  description = "CIDR block for Kubernetes Service objects in cluster"
  default     = ""
}

variable "wait_for_cluster_timeout" {
  type        = string
  description = "A timeout (in seconds) to wait for the cluster to be ready"
  default     = ""
}

# node grupos
variable "node_group_name" {
  type        = string
  default     = ""
  description = "Name of the node group"
}

variable "node_group_instance_types" {
  type        = list(string)
  default     = [""]
  description = "Instance type of the node group"
}

variable "node_group_disk_size" {
  type        = string
  default     = ""
  description = "Disk size of nodes in node group"
}

variable "node_group_desired_size" {
  type        = string
  default     = ""
  description = "Desired webapp capacity in ASG"
}

variable "node_group_max_size" {
  type        = string
  default     = ""
  description = "Maximum webapp capacity in ASG"
}

variable "node_group_min_size" {
  type        = string
  default     = ""
  description = "Minimum webapp capacity in ASG"
}


# ami
variable "webapp_ami_name_filter" {
  type    = string
  default = ""
}

variable "webapp_ami_owner_id" {
  description = "The ID of the owner for the AMI to use for the AWS EKS webapp"
  type        = string
  default     = ""
}


# aws iam
variable "cluster_iam_role_name" {
  type    = string
  default = ""
}

variable "webapp_iam_role_name" {
  type    = string
  default = ""
}

variable "iam_path" {
  type    = string
  default = ""
}

variable "iam_permissions_boundary" {
  type    = string
  default = ""
}
