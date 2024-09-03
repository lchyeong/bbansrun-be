// pipeline {
//     agent any
//
//     environment {
//         DOCKER_CREDENTIALS_ID = '2de552dc-d91d-4448-a119-eb81e12fc75c'  // Docker Hub 자격 증명 ID
//         DOCKER_IMAGE = 'chany91/bbansrun-be'  // Docker Hub에 저장할 이미지 이름
//         EC2_SSH_CREDENTIALS_ID = 'EC2_SSH_CREDENTIALS_ID'  // EC2 SSH 자격 증명 ID
//         EC2_HOST = '54.180.241.109'  // EC2 인스턴스의 퍼블릭 IP 또는 DNS
//     }
//
//     stages {
//         stage('Build Docker Image') {
//             steps {
//                 script {
//                     // Dockerfile을 기반으로 이미지 빌드
//                     docker.build("${DOCKER_IMAGE}:latest")
//                 }
//             }
//         }
//
//         stage('Push Docker Image') {
//             steps {
//                 script {
//                     // Docker Hub에 이미지 푸시
//                     docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
//                         docker.image("${DOCKER_IMAGE}:latest").push()
//                     }
//                 }
//             }
//         }
//
//         stage('Deploy') {
//             steps {
//                 script {
//                     sshagent (credentials: ['EC2_SSH_CREDENTIALS_ID']) {
//                         sh """
//                         ssh -o StrictHostKeyChecking=no ubuntu@${EC2_HOST} << 'EOF'
//                         cd /var/www/bbansrun-be
//                         docker-compose down
//                         docker-compose pull
//                         docker-compose up -d
//                         EOF
//                         """
//                     }
//                 }
//             }
//         }
//     }
//
//     post {
//         always {
//             cleanWs()
//         }
//     }
// }
