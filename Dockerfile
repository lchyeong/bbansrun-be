# 1. Build stage
FROM gradle:7.6.0-jdk17-alpine AS build

# 컨테이너 내 작업 디렉토리 설정
WORKDIR /app

# 의존성 캐시 활용을 위한 Gradle 디렉토리 복사
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts ./
COPY --chown=gradle:gradle gradle ./gradle

# 의존성 캐시를 생성
RUN gradle build -x test --no-daemon || return 0

# 나머지 파일 복사
COPY . .

# 애플리케이션 빌드
RUN ./gradlew build -x test --no-daemon

# 2. Runtime stage
FROM openjdk:17-alpine

# 컨테이너 내 작업 디렉토리 설정
WORKDIR /app

# 빌드한 JAR 파일을 가져와서 컨테이너로 복사
COPY --from=build /app/build/libs/bbansrun-0.0.1-SNAPSHOT.jar /app/bbansrun.jar

# 애플리케이션이 사용할 포트 노출
EXPOSE 8080

# 컨테이너 시작 시 실행할 명령어 설정
CMD ["java", "-jar", "/app/bbansrun.jar"]
