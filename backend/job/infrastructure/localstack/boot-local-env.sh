awslocal ses verify-email-identity --email-address duberton@gmail.com

awslocal dynamodb create-table \
  --table-name albums \
  --attribute-definitions \
    AttributeName=email,AttributeType=S \
    AttributeName=createdAt,AttributeType=S \
    AttributeName=releaseDate,AttributeType=S \
  --key-schema \
    AttributeName=email,KeyType=HASH \
    AttributeName=createdAt,KeyType=RANGE \
  --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
  --global-secondary-indexes \
      "IndexName=releaseDateIndex,KeySchema=[{AttributeName=releaseDate,KeyType=HASH},{AttributeName=email,KeyType=RANGE}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=1,WriteCapacityUnits=1}"
  --tags Key=owner,Value=duberton