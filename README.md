# 직접 활용한 외부 라이브러리 (1)

    testImplementation group: 'org.jmock', name: 'jmock-junit5', version: '2.12.0'
    동시성 테스트를 위해, Blitzer 를 사용

# 직접 활용한 외부 라이브러리 (2)

    'com.github.ben-manes.caffeine:caffine'
    장소 정보 조회 데이터에 대한 캐싱 처리 -> 조회 성능 개선 목표

# 구현 요구 사항 설명

# 1. 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 설계 및 구현 (예시. 키워드 별로 검색된 횟수)

     - ConCurrentHashMap을 이용하여, 동기화 보장

# 2. 카카오, 네이버 등 검색 API 제공자의 “다양한” 장애 발생 상황에 대한 고려

     - Async API 호출

     - Read TimeOut, Connection TimeOut -> Exception try / catch

     - 검색 결과, 기타 에러 발생에 대한 처리

     - RestControllerExcpeitonHandler

# 3. 구글 장소 검색 등 새로운 검색 API 제공자의 추가 시 변경 영역 최소화에 대한 고려

     - calculateScore 계산 메서드에 넘기는 Map에 api target Service, api result list를 key, value 형태로 추가

# 4. 대용량 트래픽 처리를 위한 반응성(Low Latency), 확장성(Scalability), 가용성(Availability)을 높이기 위한 고려

     - 반응성 : 캐싱 처리 (로컬 캐시)
     - 가용성 : Asyn Api Call