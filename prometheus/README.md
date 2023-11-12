# Prometheus 로 모니터링 시스템 구성하기

## 1. 쿠버네티스에 Prometheus 설정 예시

### 1.1. Prometheus Configuration 예시
```yaml
# List of Kubernetes service discovery configurations.
kubernetes_sd_configs:
  - role: pod
# List of target relabel configurations.
relabel_configs:
  - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_scrape]
    action: keep
    regex: true
  - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_path]
    action: replace
    target_label: __metrics_path__
    regex: (.+)
  - source_labels: [__address__ __meta_kubernetes_pod_annotation_prometheus_io_port]
    action: replace
    regex: ([^:]+)(?::\d+)?;(\d+)
    replacement: $1:$2
    target_label: __address__
```

### 1.2. 쿠버네티스 Pod 설정 예시
```yaml
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: test-app
  annotations:
    prometheus.io/scrape: "true"
    prometheus.io/port: "8080"
    prometheus.io/path: "/actuator/prometheus"
```

## 2. Helm 으로 Prometheus 설치하기
```shell
helm install prometheus prometheus-community/prometheus --version 23.4.0 -f values.yaml
```

* 설정: [values.yaml](./helm/values.yaml)

## 3. Prometheus 실행 확인



## 4. Grafana 연동 결과 확인
