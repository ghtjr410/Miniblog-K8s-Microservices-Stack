# Spring App
kubectl apply -f api-gateway.yaml
kubectl apply -f account-service.yaml
kubectl apply -f profile-service.yaml
kubectl apply -f image-service.yaml
kubectl apply -f post-service.yaml
kubectl apply -f viewcount-service.yaml
kubectl apply -f comment-service.yaml
kubectl apply -f like-service.yaml
kubectl apply -f query-service.yaml

# React App
kubectl apply -f blog-front.yaml
kubectl apply -f musical-front.yaml