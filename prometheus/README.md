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

## 3. 실행 결과
### 3.1. 프로메테우스 PromQL 실행 예시
<img width="1586" alt="스크린샷 2023-11-12 오후 8 13 28" src="https://github.com/yellowsunn/home-infra-playground/assets/43487002/7d099f1b-38a6-4e76-a18c-b0ff56c24dfc">

### 3.2. Grafana 와 연동하여 어플리케이션 모니터링 시스템 구성
<img width="1592" alt="스크린샷 2023-11-12 오후 8 18 13" src="https://github.com/yellowsunn/home-infra-playground/assets/43487002/cc7c4bba-b96e-409c-8ff3-1c8ca4b9b074">

