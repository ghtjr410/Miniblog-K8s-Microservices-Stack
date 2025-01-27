
# 1번 - Kafka
helm install zookeeper ./zookeeper
helm install broker ./broker
helm install schema-registry ./schema-registry

# 2번 - Monitoring
helm install loki ./loki
helm install prometheus ./prometheus
helm install tempo ./tempo
helm install grafana ./grafana

# 3번 - 나머지
helm install keycloak-custom ./keycloak-custom
helm install mongodb ./mongodb
