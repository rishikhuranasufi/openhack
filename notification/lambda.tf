provider "aws" {
  region     = "us-east-1"
  access_key = "${var.access_key}"
  secret_key = "${var.secret_key}"
}

resource "aws_cloudwatch_event_rule" "pipeline" {
  name        = "code-pipeline-failures"
  description = "CloudWatch Events rule to automatically update"
  event_pattern = <<PATTERN
{
  "detail": {
    "pipeline": [
      "dev-${var.repo_name}-code-pipeline"
    ],
    "state": [
      "FAILED",
      "STARTED",
      "RESUMED",
      "SUCCEEDED"
    ]
  },
  "detail-type": [
    "CodePipeline Pipeline Execution State Change"
  ],
  "source": [
    "aws.codepipeline"
  ]
}
PATTERN
}

resource "aws_cloudwatch_event_target" "sns" {
  rule      = "${aws_cloudwatch_event_rule.pipeline.name}"
  target_id = "SendToSNS"
  arn       = "${aws_sns_topic.pipeline_failure_event.arn}"  
  input_transformer {
    input_paths = {"pipeline":"$.detail.pipeline","state":"$.detail.state"}
    #input_template = "${jsonencode(":rotating_light: The Pipeline *<pipeline>* has failed. :rotating_light:")}"
    input_template = tostring("\":vertical_traffic_light: *<pipeline>* has *<state>*. :vertical_traffic_light: ${var.message}\"")
  }
}

resource "aws_sns_topic" "pipeline_failure_event" {
  name = "pipeline_failure_event"
}

resource "aws_sns_topic_policy" "default" {
  arn    = "${aws_sns_topic.pipeline_failure_event.arn}"
  policy = "${data.aws_iam_policy_document.sns_topic_policy.json}"
}

data "aws_iam_policy_document" "sns_topic_policy" {
  statement {
    effect  = "Allow"
    actions = ["SNS:Publish"]

    principals {
      type        = "Service"
      identifiers = ["events.amazonaws.com"]
    }

    resources = ["${aws_sns_topic.pipeline_failure_event.arn}"]
  }
}


######################## Lambda #############################


resource "aws_lambda_function" "lambda_function" {
  role             = "${aws_iam_role.lambda_exec_role.arn}"
  handler          = "${var.handler}"
  runtime          = "${var.runtime}"
  filename         = "lambda.zip"
  function_name    = "${var.function_name}"
  #source_code_hash = "${filesha256(file("lambda.zip"))}"
  
  environment {
    variables = {
      SLACK_WEBHOOK_URL = "${var.slack_webhook_url}"
      SLACK_CHANNEL     = "${var.slack_channel}"
      SLACK_USERNAME    = "${var.slack_username}"
    }
  }
}

resource "aws_iam_role" "lambda_exec_role" {
  name        = "lambda_exec"
  path        = "/"
  description = "Allows Lambda Function to call AWS services on your behalf."
  
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}


resource "aws_iam_policy" "lambda_logging" {
  name = "lambda_logging"
  path = "/"
  description = "IAM policy for logging from a lambda"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*",
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role = "${aws_iam_role.lambda_exec_role.name}"
  policy_arn = "${aws_iam_policy.lambda_logging.arn}"
}

resource "aws_lambda_permission" "with_sns" {
  statement_id  = "AllowExecutionFromSNS"
  action        = "lambda:InvokeFunction"
  function_name = "${aws_lambda_function.lambda_function.function_name}"
  principal     = "sns.amazonaws.com"
  source_arn    = "${aws_sns_topic.pipeline_failure_event.arn}"
}

resource "aws_sns_topic_subscription" "lambda" {
  topic_arn = "${aws_sns_topic.pipeline_failure_event.arn}"
  protocol  = "lambda"
  endpoint  = "${aws_lambda_function.lambda_function.arn}"
}
