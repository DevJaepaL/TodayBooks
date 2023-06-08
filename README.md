## 📌 프로젝트 소개

`네이버 API`를 활용한 오늘의 책을 추천해주는 어플리케이션입니다. 

`Tools` :  <a href="#" target="_blank"><img src="https://img.shields.io/badge/Android Studio-3DDC84.svg?style=flat&logo=Android-Studio&logoColor=FFFFFF"/></a>&nbsp;

`Language` :  <a href="#" target="_blank"><img src="https://img.shields.io/badge/Java-f89820.svg?style=flat&logo=javalogoColor=FFFFFF"/></a>&nbsp;

`DB` :  <a href="#" target="_blank"><img src="https://img.shields.io/badge/SQLite-003B57.svg?style=flat&logo=SQLite&logoColor=FFFFFF"/></a>&nbsp;

`API` : <a href="#" target="_blank"><img src="https://img.shields.io/badge/Naver API-03C75A.svg?style=flat&logo=Naver&logoColor=FFFFFF"/></a>&nbsp;

## 🧱 기능
+ 사용자가 선택한 카테고리에 따라, 매일 오늘의 책을 소개해주는 기능입니다.
+ 사용자는 원하는 책의 상세 정보를 볼 수 있습니다.
+ 책에 개인 메모 및 별점을 통해 입력할 수 있습니다.
+ 읽었어요 버튼을 통해 사용자가 지금까지 읽은 책들을 모아볼 수 있습니다.

___

`23-06-07 01:00 문제 발생`

+ ~~API 연동 및 데이터 파싱해서 가져오는거 까지 구현 됐으나, 네이버 API 자체적으로 카테고리 검색 지원을 안해서 하드 코딩으로 카테고리에 따른 키워드 검색어를 지정해줘야함.~~
→ **23-06-07 21:00 해결 완료 ✅**

___

### 앞으로 구현해야 할 기능

+ 네비게이션 바를 통해 카테고리 바꾸기 기능
+ 좋아요 버튼을 통해 좋아요을 누른 책 목록 보는 기능
+ ~~책 새로고침 버튼 기능~~ → `06/08` ✅
+ ~~껐다 켰을 때 기존에 보였던 책이 그대로 보여야 함.~~ `06/08` ✅
+ DB에 있는 메모 및 별점 쓰는 메소드 이용해서 좋아요를 누른 책에 메모를 할 수 있는 기능.
