variable "access_key" {
	type = string
}

variable "secret_key"{
	type = string
}

variable "repo_name" {
	type = string
	#default = "openhack"
}

variable "env" {
	type = string	
}

variable "function_name" {
  default = "minimal_lambda_function"
}

variable "handler" {
  default = "lambda.handler"
}

variable "runtime" {
  default = "python2.7"
}

variable "slack_webhook_url" {
  type = string
  #default = "https://hooks.slack.com/services/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"
}

variable "slack_channel" {
	type = string
  #default = "testing"
}

variable "slack_username" {
  type = string
  #default = "BOT"
}

variable "message" {
	type = string
}