data "aws_region" "current" {name = "us-east-1"}
provider "aws" {
  region     = "us-east-1"
  access_key = "${var.access_key}"
  secret_key = "${var.secret_key}"
}

resource "aws_iam_role" "iam_codepipeline_role" {
  name = "${var.env}-iam_codepipeline"  
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "codepipeline.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "iam_codepipeline_policy" {
  name = "${var.env}-iam_codepipeline_policy"
  role = "${aws_iam_role.iam_codepipeline_role.id}"
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "s3:*",
                "s3:GetObjectVersion",
                "s3:GetBucketVersioning",
		"sts:AssumeRole",
                "cloudwatch:*"
            ],
            "Resource": "*",
            "Effect": "Allow"
        },
        {
            "Action": [
                "s3:*"
            ],
            "Resource": [
                "arn:aws:s3:::codepipeline*",
                "arn:aws:s3:::elasticbeanstalk*"
            ],
            "Effect": "Allow"
        },
        {
            "Action": [
                "codecommit:CancelUploadArchive",
                "codecommit:GetBranch",
                "codecommit:GetCommit",
                "codecommit:GetUploadArchiveStatus",
                "codecommit:UploadArchive"
            ],
            "Resource": "*",
            "Effect": "Allow"
        },
        {
            "Action": [
                "codedeploy:CreateDeployment",
                "codedeploy:GetApplicationRevision",
                "codedeploy:GetDeployment",
                "codedeploy:GetDeploymentConfig",
                "codedeploy:RegisterApplicationRevision"
            ],
            "Resource": "*",
            "Effect": "Allow"
        },
        {
            "Action": [
                "elasticbeanstalk:*",
                "ec2:*",
                "elasticloadbalancing:*",
                "autoscaling:*",
                "cloudwatch:*",
                "s3:*",
                "sns:*",
                "cloudformation:*",
                "rds:*",
                "sqs:*",
                "ecs:*",
                "iam:PassRole"
            ],
            "Resource": "*",
            "Effect": "Allow"
        },
        {
            "Action": [
                "lambda:InvokeFunction",
                "lambda:ListFunctions"
            ],
            "Resource": "*",
            "Effect": "Allow"
        },
        {
            "Action": [
                "opsworks:CreateDeployment",
                "opsworks:DescribeApps",
                "opsworks:DescribeCommands",
                "opsworks:DescribeDeployments",
                "opsworks:DescribeInstances",
                "opsworks:DescribeStacks",
                "opsworks:UpdateApp",
                "opsworks:UpdateStack"
            ],
            "Resource": "*",
            "Effect": "Allow"
        },
        {
            "Action": [
                "cloudformation:CreateStack",
                "cloudformation:DeleteStack",
                "cloudformation:DescribeStacks",
                "cloudformation:UpdateStack",
                "cloudformation:CreateChangeSet",
                "cloudformation:DeleteChangeSet",
                "cloudformation:DescribeChangeSet",
                "cloudformation:ExecuteChangeSet",
                "cloudformation:SetStackPolicy",
                "cloudformation:ValidateTemplate",
                "iam:PassRole"
            ],
            "Resource": "*",
            "Effect": "Allow"
        },
        {
            "Action": [
		"codebuild:*",
                "codebuild:BatchGetBuilds",
                "codebuild:StartBuild"
            ],
            "Resource": "*",
            "Effect": "Allow"
        }
    ]
}
EOF
}


resource "aws_iam_role" "iam_code_build_role" {
  name = "${var.env}-iam_code_build_role"  
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "codebuild.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}


resource "aws_iam_role_policy_attachment" "iam_code_build_policy_attach" {
  #name       = "EC2FULLACCESS"
  role       = "${aws_iam_role.iam_code_build_role.id}"
  policy_arn = "arn:aws:iam::aws:policy/AmazonECS_FullAccess"
}

resource "aws_iam_role_policy_attachment" "iam_code_build_policy_attach2" {
  #name= "AWSCodeBuildAdminAccess"
  role= "${aws_iam_role.iam_code_build_role.id}"
  policy_arn = "arn:aws:iam::aws:policy/AWSCodeBuildAdminAccess"
}

resource "aws_iam_role" "iam_ecs_service_role" {
  name = "${var.env}-ecsServiceRole"
  path = "/"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ecs.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "ecsServiceRolePolicy" {
  name = "${var.env}-ecsServiceRolePolicy"
  role = "${aws_iam_role.iam_ecs_service_role.id}"

  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
        "Effect": "Allow",
        "Action": [
            "ecs:*",
            "elasticloadbalancing:DeregisterInstancesFromLoadBalancer",
            "elasticloadbalancing:DeregisterTargets",
            "elasticloadbalancing:Describe*",
            "elasticloadbalancing:RegisterInstancesWithLoadBalancer",
            "elasticloadbalancing:RegisterTargets"
        ],
        "Resource": "*"
    }
  ]
}
POLICY
}

resource "aws_iam_role_policy_attachment" "iam_code_build_policy_attach3" {
  #name= "EC2ContainerRegistry"
  role= "${aws_iam_role.iam_code_build_role.id}"
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryFullAccess"
}

resource "aws_iam_role_policy_attachment" "iam_code_build_policy_attach4" {
  #name= "EC2ContainerRegistry"
  role= "${aws_iam_role.iam_code_build_role.id}"
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchFullAccess"
}
resource "aws_iam_role_policy" "iam_code_build_policy" {
  name = "${var.env}-iam_code_build_policy"
  role = "${aws_iam_role.iam_code_build_role.id}"

  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
          "s3:*",
          "s3:GetObject",
          "s3:GetObjectVersion",
          "s3:GetBucketVersioning"
      ],
      "Resource": "*",
      "Effect": "Allow",
      "Sid": "AccessCodePipelineArtifacts"
    },
    {
      "Action": [
          "ecr:GetDownloadUrlForLayer",
          "ecr:BatchGetImage",
          "ecr:BatchCheckLayerAvailability",
          "ecr:PutImage",
          "ecr:InitiateLayerUpload",
          "ecr:UploadLayerPart",
          "ecr:CompleteLayerUpload"
      ],
      "Resource": "*",
      "Effect": "Allow",
      "Sid": "AccessECR"
    },
    {
      "Action": [
          "ecr:GetAuthorizationToken"
      ],
      "Resource": "*",
      "Effect": "Allow",
      "Sid": "ecrAuthorization"
    },
    {
      "Action": [
          "ecs:RegisterTaskDefinition",
          "ecs:DescribeTaskDefinition",
          "ecs:DescribeServices",
          "ecs:CreateService",
          "ecs:ListServices",
          "ecs:UpdateService"
      ],
      "Resource": "*",
      "Effect": "Allow",
      "Sid": "ecsAccess"
    },
    {
         "Sid":"logStream",
         "Effect":"Allow",
         "Action":[
            "logs:PutLogEvents",
            "logs:CreateLogGroup",
            "logs:CreateLogStream"
         ],
         "Resource":"arn:aws:logs:${data.aws_region.current.name}:*:*"
    },
    {
            "Effect": "Allow",
            "Action": [
                "iam:GetRole",
                "iam:PassRole"
            ],
            "Resource": "${aws_iam_role.iam_ecs_service_role.arn}"
    }
  ]
}
POLICY
}

resource "aws_s3_bucket" "default" {  
  bucket = "${var.env}-${var.app_name}-codepipeline-ecs"
  acl    = "private"
  tags = {
    Name        = "${var.env}-${var.app_name}"
    Environment = "${var.env}"
  }
}

resource "aws_codebuild_project" "create_build" {  
  name              = "${var.env}-Create-Build-${var.app_name}"
  description       = "Create Build"
  build_timeout     = "300"
  service_role      = "${aws_iam_role.iam_code_build_role.arn}"

  artifacts {
    type = "S3"
	location = "${aws_s3_bucket.default.bucket}"
  }

  environment {
    compute_type                = "BUILD_GENERAL1_SMALL"
    image                       = "aws/codebuild/standard:1.0"
    type                        = "LINUX_CONTAINER"
    image_pull_credentials_type = "CODEBUILD"
  }

  source {
    type            = "CODECOMMIT"
    location        = "${var.gitURL}"
	git_clone_depth = "1"
	#BranchName      = "master"
  }
  cache {
    modes = [
       "LOCAL_DOCKER_LAYER_CACHE",
       "LOCAL_SOURCE_CACHE",
    ]
  type  = "LOCAL"
  }
}

resource "aws_codepipeline" "codepipeline" {  
  name     = "${var.env}-${var.app_name}-code-pipeline"
  role_arn = "${aws_iam_role.iam_codepipeline_role.arn}"

  artifact_store {
    location = "${aws_s3_bucket.default.bucket}"
    type     = "S3"
  }

  stage {
    name = "${var.app_name}-Source-Code-${var.env}"
    action {
      name             = "Source"
      category         = "Source"
      owner            = "AWS"
      provider         = "CodeCommit"
      version          = "1"
      output_artifacts = ["source"]

      configuration = {
        RepositoryName       = "${var.app_name}"
        BranchName           = "master"
      }
    }
  }

stage {
    name = "${var.env}-create_build-${var.app_name}"

    action {
      name            = "Build"
      category        = "Build"
      owner           = "AWS"
      provider        = "CodeBuild"
      input_artifacts = ["source"]
      output_artifacts= ["imagedefinitions"]
      version         = "1"
      configuration = {
         ProjectName = "${aws_codebuild_project.create_build.name}"
      }
    }
  }
}



