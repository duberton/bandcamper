data "aws_partition" "current" {}

data "aws_ami" "webapp" {
  filter {
    name   = "name"
    values = [local.webapp_ami_name_filter]
  }

  most_recent = true

  owners = [local.webapp_ami_owner_id]
}
