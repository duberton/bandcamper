kubectl create secret generic google-credentials --from-env-file ../../backend/.googleCredentials
kubectl create secret generic aws-credentials --from-env-file ../../backend/.awsCredentials