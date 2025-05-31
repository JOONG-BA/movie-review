import {MovieSlide} from "@/components/main/MovieSlide.jsx";

export const dummyMovies =
[
    {
        id: 101,
        title: '기생충',
        overview: '가난한 가족과 부유한 가족 사이에서 벌어지는 충격적인 이야기. 예상치 못한 전개가 이어지는 블랙 코미디 스릴러.',
        releaseDate: '2019-05-30',
        posterPath: 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
        voteAverage: 8.6,
        genres: ['드라마', '스릴러'],
        productionCountries: '대한민국',
        cast: '송강호, 이선균, 조여정',
        crew: '봉준호',
        category: 'movie'
    },
    {
        id: 102,
        title: '부산행',
        overview: '좀비 바이러스가 확산된 대한민국, 부산행 열차 안에서 생존을 위한 사투가 벌어진다.',
        releaseDate: '2016-07-20',
        posterPath: 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
        voteAverage: 7.7,
        genres: ['액션', '스릴러'],
        productionCountries: '대한민국',
        cast: '공유, 마동석, 정유미',
        crew: '연상호',
        category: 'movie'
    },
    {
        id: 103,
        title: '살인의 추억',
        overview: '1980년대 대한민국을 뒤흔든 연쇄살인사건을 다룬 실화 기반의 스릴러. 수사팀의 집요한 추적이 이어진다.',
        releaseDate: '2003-04-25',
        posterPath: 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
        voteAverage: 8.1,
        genres: ['범죄', '스릴러', '드라마'],
        productionCountries: '대한민국',
        cast: '송강호, 김상경, 박해일',
        crew: '봉준호',
        category: 'movie'
    },
    {
        id: 104,
        title: '명량',
        overview: '임진왜란, 이순신 장군이 12척의 배로 330척의 왜군을 무찌른 명량해전을 그린 역사 대서사극.',
        releaseDate: '2014-07-30',
        posterPath: 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
        voteAverage: 7.5,
        genres: ['전쟁', '드라마', '역사'],
        productionCountries: '대한민국',
        cast: '최민식, 류승룡, 조진웅',
        crew: '김한민',
        category: 'movie'
    },
    {
        id: 105,
        title: '헤어질 결심',
        overview: '산에서 벌어진 의문의 죽음을 수사하던 형사는, 용의자인 여인과 묘한 감정의 줄다리기를 하게 된다.',
        releaseDate: '2022-06-29',
        posterPath: 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
        voteAverage: 7.9,
        genres: ['로맨스', '스릴러', '드라마'],
        productionCountries: '대한민국',
        cast: '박해일, 탕웨이',
        crew: '박찬욱',
        category: 'movie'
    }
];

export default function Home() {
    return (
        <div className="max-w-[1320px] pt-10 m-auto">
            <MovieSlide title="최신 리뷰" movies={dummyMovies} />
            <MovieSlide title="평점 높은 영화" movies={dummyMovies} />
            <MovieSlide title="액션 영화" movies={dummyMovies} />
        </div>
    );
}