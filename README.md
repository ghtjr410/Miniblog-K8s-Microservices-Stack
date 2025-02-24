# MiniBlog (MSA Version)

개인의 일상을 공유하는 블로그 플랫폼

## 🚀 기술 스택

- **Backend**: Java, Spring Boot, Spring Cloud, AOP, JPA, Kafka  
- **Database**: MySQL, MongoDB  
- **DevOps & Infra**: Docker, Kubernetes, AWS (EKS, ALB, CloudFront, S3, RDS, ECR, Route53, Certification Manager), Helm, JMeter, Jenkins  
- **Security**: Keycloak  
- **Monitoring & Logging**: Grafana, Prometheus, Loki, Tempo  
- **Frontend**: React  

## 🕰️ 개발 기간
- **2024.08** ~ **2024.12**

## 👥 팀 구성

- **개인** : 최호석

## ✨ 주요 기능

### 1️⃣ Kafka 기반 이벤트 발행: Outbox 패턴, Avro, 멱등성, SagaStatus를 활용한 트랜잭션 안정성 및 확장성 확보  
- **@Retryable**을 활용한 재시도 로직 구현  
- **SpEL 기반 동적 토픽 할당**  
- **원자적 상태 업데이트**를 통한 이벤트 발행 신뢰성 보장  

### 2️⃣ Kafka 이벤트 발행 병목 현상 해결: Virtual Thread 기반 병렬 처리  
- **이벤트 처리량 3배 증가, 성능 67% 향상**  
- Virtual Thread를 활용한 **고성능 이벤트 병렬 처리**  

### 3️⃣ API Gateway 인증 통합: 커스텀 헤더 및 스푸핑 방지로 신뢰성 강화  
- **하위 서버 중복 인증 제거 → 평균 응답 속도 26.32% 개선**  
- API Gateway에서 **토큰 검증 및 회원 정보 헤더 전달**  

### 4️⃣ 이미지 업로드 최적화: Base64 → URL 방식(S3, CloudFront) 전환  
- **데이터 전송 속도 93.4% 향상**  
- **저장 크기 99.97% 감소**  
- **로딩 속도 89.2% 개선**  

### 5️⃣ MSA 분산 추적 및 모니터링: 9개 서버 비동기 통신 TraceId 공유  
- **Tempo와 Zipkin을 활용한 TraceId 공유 및 이벤트 경로 추적**  
- **AOP 기반 Repository 관찰 → JPA 메서드까지 추적 가능**  
- **Grafana 기반 로그, 트레이싱, 매트릭 통합 → 오류 감지 시간 평균 70% 단축 (10분 → 3분)**  

### 6️⃣ Docker 기반 개발 환경 구축 및 최적화  
- **멀티 스테이징 빌드 적용 → 컨테이너 이미지 크기 44.4% 감소**  
- **Docker Compose 활용 → 9개의 오픈소스 컨테이너 통합 및 네트워크 구성**  

### 7️⃣ AWS EKS 기반 Kubernetes 환경 구성 및 최적화  
- **NLB(5개 IP, 월간 18USD) → ALB + Route53(1개 IP, 월간 3.60USD) 전환 → 월 비용 80% 절감**  
- **Certification Manager를 활용한 100% HTTPS 환경 구성** (HTTP 요청도 HTTPS로 리디렉션)  
- **React 앱 2개, Spring 서버 9개, 인프라 11개 구성 및 배포, YAML 작성**  

### 8️⃣ 9개 서버 배포 자동화: Jenkins와 Helm 활용  
- **kubectl 수작업 실수 방지, 배포 속도 10분 → 3분으로 단축**  
- **CI/CD 자동화로 운영 효율성 개선**  
