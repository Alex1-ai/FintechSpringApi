variable subnet_cidr_block {
  default = "10.0.0.0/24"
}


variable vpc_cidr_block {
  default = "10.0.0.0/16"
}
variable avail_zone {
  default = "us-east-1a"
}

variable env_prefix {
  default = "dev"
}
variable  my_ip{
  default = "102.89.42.118/32"
}
variable instance_type {
  default = "t2.micro"
}

variable region {
  default = "us-east-1"
}