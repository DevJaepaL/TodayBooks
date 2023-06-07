package com.devjaepal.android.todaybooks.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// 카테고리에 따른 검색 키워드 지정해주는 클래스임.
public class CategoryKewordUtility {
    private static final Random random = new Random();

    /* 지정한 여러 개의 키워드 중 하나를 검색함.
    *  현재 매개 변수를 가변 인자로 지정해, 인자를 여러 개 넘겨주고 리턴할 때 1개의 키워드만 랜덤으로 추출함. */
    private static String getRandomKeyword(String... kewords) {
        List<String> kewordList = new ArrayList<>(Arrays.asList(kewords));
        return kewordList.get(random.nextInt(kewordList.size()));
    }

    public static String getCategoriesKeyword(String category) {
        switch (category) {
            case "소설":
                return getRandomKeyword("추리소설", "판타지소설", "로맨스소설", "역사소설", "SF소설", "공포소설", "로맨스소설", "청소년소설", "판타지소설", "역사소설");
            case "시/에세이":
                return getRandomKeyword("현대시", "고전시", "에세이", "자전적시", "한시", "사랑시", "자연시", "사회비판시", "유머시", "감성에세이");
            case "컴퓨터/IT":
                return getRandomKeyword("프로그래밍", "웹개발", "데이터베이스", "알고리즘", "인공지능", "머신러닝", "네트워크", "보안", "클라우드컴퓨팅", "모바일앱");
            case "만화":
                return getRandomKeyword("일본만화", "한국만화", "웹툰", "액션만화", "로맨스만화", "판타지만화", "코믹만화", "스포츠만화", "BL만화", "개그만화");
            case "정치":
                return getRandomKeyword("한국정치", "세계정치", "정치이론", "국제관계", "정치경제", "국가론", "정치사회학", "민주주의", "선거", "외교");
            case "국어/외국어":
                return getRandomKeyword("국어문법", "한자", "작문", "수필", "영어회화", "독일어", "중국어", "일본어", "스페인어", "프랑스어");
            case "여행":
                return getRandomKeyword("국내여행", "해외여행", "유럽여행", "아시아여행", "아프리카여행", "북미여행", "남미여행", "호주여행", "관광명소", "여행에세이");
            case "가정/요리":
                return getRandomKeyword("요리레시피", "베이킹", "한식요리", "양식요리", "일식요리", "중식요리", "디저트", "건강요리", "간편요리", "밥상");
            case "어린이":
                return getRandomKeyword("그림책", "동화", "어린이소설", "어린이전집", "창의력", "교양", "학습", "공부", "과학", "역사");
            case "유아":
                return getRandomKeyword("육아백서", "유아교육", "어린이영어", "운동", "창의력", "아기간식", "나만의책", "예술", "동요", "팝업북");
            case "종교":
                return getRandomKeyword("기독교", "불교", "천주교", "이슬람교", "도교", "정교", "종교문학", "성서", "영성서적", "종교철학");
            case "예술/대중문화":
                return getRandomKeyword("미술", "음악", "영화", "연극", "무용", "미술사", "예술철학", "사진", "디자인", "만화");
            case "자연/과학":
                return getRandomKeyword("과학", "물리학", "화학", "생물학", "천문학", "지구과학", "지리학", "환경", "자연보호", "과학기술");
            case "사회 / 정치":
                return getRandomKeyword("사회학", "사회문제", "사회심리학", "사회통계학", "사회사", "인권", "사회이론", "사회복지", "사회구조", "사회과학");
            case "역사":
                return getRandomKeyword("한국역사", "세계역사", "동양사", "서양사", "고대사", "중세사", "근대사", "현대사", "역사인물", "역사기행");
            case "자기계발":
                return getRandomKeyword("성공학", "취업", "자기개발", "리더십", "성공스토리", "금융", "자기관리", "스트레스관리", "명상", "자기계발서");
            case "경제 / 경영":
                return getRandomKeyword("경제이론", "경제사", "경영이론", "경영학", "마케팅", "재무관리", "투자", "창업", "경제정책", "국제경제");
            default:
                return "";
        }
    }
}
