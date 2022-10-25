### 프로젝트 소개 🤲🏻
```
블록체인을 활용한 투명한 기부 웹 어플리케이션
```

##

### 팀원 👩‍👦‍👦

<div align=center> 

|백엔드|백엔드|백엔드|백엔드|
|:-:|:-:|:-:|:-:|
|<img src="https://user-images.githubusercontent.com/106054507/194209563-bb6bfc05-e188-43a2-a673-b6e2f26eaf38.png" width="150">|<img src="https://user-images.githubusercontent.com/106054507/194209702-74e70e3b-2b3a-47a0-be45-2dc326593ce3.png" width="150">|<img src="https://user-images.githubusercontent.com/106054507/194209823-b70d76b8-6d9e-4181-80f3-0cc34fa8cfe0.png" width="150">|<img src="https://user-images.githubusercontent.com/106054507/194209890-ed1ff17f-7146-49f4-b347-f2a5eb81876d.png" width="150">|
|[우진](https://github.com/WooJinDeve)|[원진](https://github.com/jangwon3828)|[태용](https://github.com/HoodRyan)|[민건](https://github.com/mine702)|

</div>

##

### 프로젝트 기술스택 🏰

<div align=center> 
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=square&logo=Spring Boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=square&logo=Spring Security&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=square&logo=Spring Data JPA&logoColor=white"/>
  <br>
  <img src="https://img.shields.io/badge/Spring RestDocs-6DB33F?style=square&logo=Spring Data JPA&logoColor=white"/>
  <img src="https://img.shields.io/badge/Querydsl-003366?style=square&logo=Querydsl&logoColor=white"/>
  <br>

  <img src="https://img.shields.io/badge/MySQL-4479A1?style=square&logo=MySQL&logoColor=white"/>
  <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=square&logo=Amazon RDS&logoColor=white">
  <img src="https://img.shields.io/badge/Redis-DC382D?style=square&logo=Redis&logoColor=white"/>
  <img src="https://img.shields.io/badge/AWS S3-569A31?style=square&logo=Amazon S3&logoColor=white"> 

  <br>
  
  <img src="https://img.shields.io/badge/AWS-FF9900?style=square&logo=Amazon AWS&logoColor=white"> 
  <img src="https://img.shields.io/badge/Docker-2496ED?style=square&logo=Docker&logoColor=white">
  <br>
  
</div>

## 

### 프로젝트 아키텍쳐 🏛
- **CI/CD**
<div align=center> 
<img width="619" alt="cicd" src="https://user-images.githubusercontent.com/106054507/197660025-c7385431-1300-450d-bc75-8c4a2db4b557.PNG">
</div>

### 데이터 베이스 설계 🖼 

[Table Diagram](https://dbdiagram.io/d/633bfdb8f0018a1c5f8d14b7)

![image](https://user-images.githubusercontent.com/106054507/194212014-ab18dc5e-1e05-4b5e-8e33-b3ba35f38c2d.png)


##

### 프로젝트 중점사항 👕 
#### **프로젝트를 진행하면서 중점적으로 도입해 볼 목록을 정리했습니다.**

#### Common
- `Version` 관리 전략
- `API` 문서화
#### Spring
- `Spring` 기능을 충분히 활용
- `Spring` 내부 동작과 구조를 숙지하면서 사용
#### Performance
- 서버 확장성
- 대규모 트래픽을 처리에 대한 고려
- 테이블 설계에 대한 고려 사항 체험
#### Code Quality
- `OOP`와 관련된 원칙들을 준수
- 테스트가 쉽도록 설계 준수
- `Layer`에 대한 책임 구분 준수
#### 문제 상황 Simulation
- 데이터가 많은 경우 정산에 대한 처리 시간 문제
- 이벤트 시 한번의 트래픽이 몰리는 경우

##

### 협업 방식 💬 
#### Branch
- `main`, `dev`, `feature/*`
```
• main : 배포용
• dev : 개발용
• feature/* : 작업용
```
#### 커밋 메시지
```
[ 예시 ]
• Woojin_로컬 로그인 기능 추가
• Wonjin_기부 동시성 오류 수정
```
#### PR 규칙
- `feature/작업`에서 기능 개발 후 `dev`에 `PR`
- `PR` 제목은 브랜치에서 작업한 단위
- 본문에는 리뷰어들이 알아야 될 사항 명시
