# Kafka
kubectl apply -f zookeeper.yaml
kubectl apply -f broker.yaml
kubectl apply -f schema-registry.yaml
# Monitoring
kubectl apply -f loki.yaml
kubectl apply -f prometheus.yaml
kubectl apply -f tempo.yaml
kubectl apply -f grafana.yaml
# 나머지
kubectl apply -f keycloak-custom.yaml
kubectl apply -f mongodb.yaml