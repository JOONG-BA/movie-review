
const galleryImages = Array(10).fill(
    "https://image.tmdb.org/t/p/original/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg"
);

export const dummyMovies = [
    {
        id: 101,
        title: "기생충",
        overview: "가난한 가족과 부유한 가족 사이에서 벌어지는 충격적인 이야기. 예상치 못한 전개가 이어지는 블랙 코미디 스릴러.",
        releaseDate: "2019-05-30",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 8.6,
        genres: ["드라마", "스릴러"],
        productionCountries: "대한민국",
        casts: [
            { id: 1, name: "송강호", role: "기택", profilePath: "" },
            { id: 2, name: "이선균", role: "동익", profilePath: "" },
            { id: 3, name: "송강호", role: "기택", profilePath: "" },
            { id: 4, name: "이선균", role: "동익", profilePath: "" },
            { id: 5, name: "이선균", role: "동익", profilePath: "" },
            { id: 6, name: "이선균", role: "동익", profilePath: "" },
            { id: 7, name: "이선균", role: "동익", profilePath: "" },
        ],
        directors: [
            { id: 100, name: "봉준호", role: "감독", profilePath: "" },
            { id: 101, name: "봉준호", role: "감독", profilePath: "" },
            { id: 102, name: "봉준호", role: "감독", profilePath: "" },
            { id: 103, name: "봉준호", role: "감독", profilePath: "" },
        ],
        gallery: galleryImages,
        category: "movie",
        videos: [
            { key: "1mTjfMFyPi8", site: "YouTube", name: "Spider-Mans Trailer", type: "Trailer" },
            { key: "1mTjfMFyPi8", site: "YouTube", name: "Spider-Mans Trailer", type: "Trailer" },
            { key: "1mTjfMFyPi8", site: "YouTube", name: "Spider-Mans Trailer", type: "Trailer" }
        ]
    },
    {
        id: 102,
        title: "부산행",
        overview: "좀비 바이러스가 확산된 대한민국, 부산행 열차 안에서 생존을 위한 사투가 벌어진다.",
        releaseDate: "2016-07-20",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 7.7,
        genres: ["액션", "스릴러"],
        productionCountries: "대한민국",
        casts: [
            { id: 3, name: "공유", role: "석우", profilePath: "" },
            { id: 4, name: "마동석", role: "상화", profilePath: "" }
        ],
        directors: [
            { id: 101, name: "연상호", role: "감독", profilePath: "" }
        ],
        gallery: galleryImages,
        category: "movie"
    },
    {
        id: 103,
        title: "살인의 추억",
        overview: "1980년대 대한민국을 뒤흔든 연쇄살인사건을 다룬 실화 기반의 스릴러. 수사팀의 집요한 추적이 이어진다.",
        releaseDate: "2003-04-25",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 8.1,
        genres: ["범죄", "스릴러", "드라마"],
        productionCountries: "대한민국",
        casts: [
            { id: 5, name: "김상경", role: "서형사", profilePath: "" },
            { id: 1, name: "송강호", role: "박형사", profilePath: "" }
        ],
        directors: [
            { id: 100, name: "봉준호", role: "감독", profilePath: "" }
        ],
        gallery: galleryImages,
        category: "movie"
    },
    {
        id: 104,
        title: "브로커",
        overview: "아기를 유기한 여성과 아기를 입양하려는 남자들의 이야기를 그린 드라마.",
        releaseDate: "2022-06-08",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 7.5,
        genres: ["드라마"],
        productionCountries: "대한민국",
        casts: [
            { id: 1, name: "송강호", role: "상현", profilePath: "" },
            { id: 2, name: "강동원", role: "동수", profilePath: "" },
            { id: 3, name: "배두나", role: "소영", profilePath: "" },
            { id: 4, name: "이정은", role: "수녀", profilePath: "" }
        ],
        directors: [
            { id: 101, name: "고레에다 히로카즈", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 105,
        title: "다음 소희",
        overview: "콜센터 인턴십을 하던 소희가 극단적 선택을 하게 되는 이야기를 그린 드라마.",
        releaseDate: "2022-05-18",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 7.8,
        genres: ["드라마"],
        productionCountries: "대한민국",
        casts: [
            { id: 5, name: "김시은", role: "소희", profilePath: "" },
            { id: 6, name: "배두나", role: "오유진", profilePath: "" },
            { id: 7, name: "심희섭", role: "정민", profilePath: "" },
            { id: 8, name: "김우겸", role: "동규", profilePath: "" }
        ],
        directors: [
            { id: 102, name: "정주리", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 106,
        title: "킬러의 보수",
        overview: "퇴직한 킬러가 복수를 위해 다시 일에 뛰어드는 액션 영화.",
        releaseDate: "2023-03-15",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 6.9,
        genres: ["액션", "스릴러"],
        productionCountries: "대한민국",
        casts: [
            { id: 9, name: "전지현", role: "길수", profilePath: "" },
            { id: 10, name: "김수현", role: "정훈", profilePath: "" },
            { id: 11, name: "허준호", role: "장 회장", profilePath: "" }
        ],
        directors: [
            { id: 103, name: "이정범", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 107,
        title: "헤어질 결심",
        overview: "산에서 발생한 남자의 죽음을 수사하는 형사가 의심스러운 여인과의 관계를 맺게 되는 이야기.",
        releaseDate: "2022-06-29",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 8.0,
        genres: ["드라마", "미스터리", "로맨스"],
        productionCountries: "대한민국",
        casts: [
            { id: 12, name: "박해일", role: "장해준", profilePath: "" },
            { id: 13, name: "탕웨이", role: "송서래", profilePath: "" },
            { id: 14, name: "이정현", role: "조형사", profilePath: "" }
        ],
        directors: [
            { id: 104, name: "박찬욱", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 108,
        title: "마더",
        overview: "아들의 누명을 벗기기 위해 진실을 쫓는 어머니의 이야기.",
        releaseDate: "2009-05-28",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 8.4,
        genres: ["드라마", "스릴러"],
        productionCountries: "대한민국",
        casts: [
            { id: 15, name: "김혜자", role: "어머니", profilePath: "" },
            { id: 1, name: "송강호", role: "형사", profilePath: "" }
        ],
        directors: [
            { id: 100, name: "봉준호", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 109,
        title: "왕의 남자",
        overview: "조선시대 광대들의 이야기를 그린 드라마.",
        releaseDate: "2005-07-29",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 8.2,
        genres: ["드라마", "역사"],
        productionCountries: "대한민국",
        casts: [
            { id: 16, name: "장동건", role: "공길", profilePath: "" },
            { id: 17, name: "공유", role: "광대", profilePath: "" }
        ],
        directors: [
            { id: 105, name: "이준익", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 110,
        title: "신과 함께",
        overview: "죽음 이후 저승에서 벌어지는 재판 이야기를 담은 판타지 드라마.",
        releaseDate: "2017-12-20",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 7.8,
        genres: ["판타지", "드라마"],
        productionCountries: "대한민국",
        casts: [
            { id: 18, name: "차태현", role: "강림", profilePath: "" },
            { id: 19, name: "주지훈", role: "해원맥", profilePath: "" }
        ],
        directors: [
            { id: 106, name: "김용화", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 111,
        title: "곡성",
        overview: "외딴 마을에서 벌어진 의문의 사건을 다룬 미스터리 스릴러.",
        releaseDate: "2016-05-12",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 7.9,
        genres: ["미스터리", "스릴러", "드라마"],
        productionCountries: "대한민국",
        casts: [
            { id: 20, name: "곽도원", role: "경찰", profilePath: "" },
            { id: 21, name: "황정민", role: "목사", profilePath: "" }
        ],
        directors: [
            { id: 107, name: "나홍진", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 112,
        title: "탐정: 더 비기닝",
        overview: "두 탐정이 벌이는 범죄 수사 코미디.",
        releaseDate: "2015-05-13",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 6.8,
        genres: ["코미디", "범죄"],
        productionCountries: "대한민국",
        casts: [
            { id: 22, name: "강동원", role: "탐정", profilePath: "" },
            { id: 23, name: "이광수", role: "조수", profilePath: "" }
        ],
        directors: [
            { id: 108, name: "김정훈", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 113,
        title: "검사외전",
        overview: "검사와 조직폭력배의 특별한 인연과 대결을 그린 액션 코미디.",
        releaseDate: "2016-01-06",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 7.3,
        genres: ["액션", "코미디"],
        productionCountries: "대한민국",
        casts: [
            { id: 24, name: "황정민", role: "검사", profilePath: "" },
            { id: 25, name: "정우성", role: "조직폭력배", profilePath: "" }
        ],
        directors: [
            { id: 109, name: "이일형", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 114,
        title: "1987",
        overview: "1987년 대한민국 민주화 운동을 다룬 역사 드라마.",
        releaseDate: "2017-12-27",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 8.0,
        genres: ["드라마", "역사"],
        productionCountries: "대한민국",
        casts: [
            { id: 26, name: "김윤석", role: "검사", profilePath: "" },
            { id: 27, name: "하정우", role: "학생운동가", profilePath: "" }
        ],
        directors: [
            { id: 110, name: "장준환", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 115,
        title: "더 킹",
        overview: "권력을 쥐기 위한 정치 스릴러.",
        releaseDate: "2017-01-18",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 7.5,
        genres: ["드라마", "범죄"],
        productionCountries: "대한민국",
        casts: [
            { id: 28, name: "조인성", role: "검사", profilePath: "" },
            { id: 29, name: "정우성", role: "정치인", profilePath: "" }
        ],
        directors: [
            { id: 111, name: "한재림", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 116,
        title: "도둑들",
        overview: "아시아 최고의 도둑들이 펼치는 대규모 강도극.",
        releaseDate: "2012-07-25",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 7.3,
        genres: ["액션", "범죄"],
        productionCountries: "대한민국",
        casts: [
            { id: 30, name: "김윤진", role: "도둑", profilePath: "" },
            { id: 31, name: "전지현", role: "도둑", profilePath: "" }
        ],
        directors: [
            { id: 112, name: "최동훈", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 117,
        title: "곡성: 악몽의 시작",
        overview: "곡성의 사건 이후 벌어지는 미스터리 후속편.",
        releaseDate: "2024-01-01",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 0.0,
        genres: ["미스터리", "스릴러"],
        productionCountries: "대한민국",
        casts: [],
        directors: [],
        category: "movie"
    },
    {
        id: 118,
        title: "어벤져스: 엔드게임",
        overview: "어벤져스가 우주의 절반을 잃은 후 최후의 전투를 벌인다.",
        releaseDate: "2019-04-24",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 8.4,
        genres: ["액션", "모험", "SF"],
        productionCountries: "미국",
        casts: [
            { id: 32, name: "로버트 다우니 주니어", role: "아이언맨", profilePath: "" },
            { id: 33, name: "크리스 에반스", role: "캡틴 아메리카", profilePath: "" }
        ],
        directors: [
            { id: 113, name: "루소 형제", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 119,
        title: "인셉션",
        overview: "꿈 속의 꿈을 조작하는 도둑의 이야기.",
        releaseDate: "2010-07-16",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 8.8,
        genres: ["SF", "스릴러", "액션"],
        productionCountries: "미국",
        casts: [
            { id: 34, name: "레오나르도 디카프리오", role: "도미닉", profilePath: "" },
            { id: 35, name: "조셉 고든-레빗", role: "아서", profilePath: "" }
        ],
        directors: [
            { id: 114, name: "크리스토퍼 놀란", role: "감독", profilePath: "" }
        ],
        category: "movie"
    },
    {
        id: 120,
        title: "라라랜드",
        overview: "꿈을 쫓는 두 연인의 사랑과 예술 이야기.",
        releaseDate: "2016-12-09",
        posterPath: "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
        backdropPath: "https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
        voteAverage: 8.0,
        genres: ["드라마", "뮤지컬", "로맨스"],
        productionCountries: "미국",
        casts: [
            { id: 36, name: "라이언 고슬링", role: "세바스찬", profilePath: "" },
            { id: 37, name: "엠마 스톤", role: "미아", profilePath: "" }
        ],
        directors: [
            { id: 115, name: "데이미언 셔젤", role: "감독", profilePath: "" }
        ],
        category: "movie"
    }
];
