import {MovieSlide} from "@/components/main/MovieSlide.jsx";

const galleryImages = Array(10).fill(
    "https://image.tmdb.org/t/p/original/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg"
);

export const dummyMovies = [
    {
        id: 101,
        title: '기생충',
        overview: '가난한 가족과 부유한 가족 사이에서 벌어지는 충격적인 이야기. 예상치 못한 전개가 이어지는 블랙 코미디 스릴러.',
        releaseDate: '2019-05-30',
        posterPath: 'https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg',
        backdropPath: 'https://image.tmdb.org/t/p/w780/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg',
        voteAverage: 8.6,
        genres: ['드라마', '스릴러'],
        productionCountries: '대한민국',
        casts: [
            {
                id: 1,
                name: '송강호',
                role: '기택',
                profilePath: ''
            },
            {
                id: 2,
                name: '이선균',
                role: '동익',
                profilePath: ''
            }, {
                id: 1,
                name: '송강호',
                role: '기택',
                profilePath: ''
            },
            {
                id: 2,
                name: '이선균',
                role: '동익',
                profilePath: ''
            }, {
                id: 1,
                name: '송강호',
                role: '기택',
                profilePath: ''
            },
            {
                id: 2,
                name: '이선균',
                role: '동익',
                profilePath: ''
            }, {
                id: 1,
                name: '송강호',
                role: '기택',
                profilePath: ''
            },
            {
                id: 2,
                name: '이선균',
                role: '동익',
                profilePath: ''
            }, {
                id: 1,
                name: '송강호',
                role: '기택',
                profilePath: ''
            },
            {
                id: 2,
                name: '이선균',
                role: '동익',
                profilePath: ''
            }, {
                id: 1,
                name: '송강호',
                role: '기택',
                profilePath: ''
            },
            {
                id: 2,
                name: '이선균',
                role: '동익',
                profilePath: ''
            },
        ],
        directors: [
            {
                id: 100,
                name: '봉준호',
                role: '감독',
                profilePath: ''
            },
            {
                id: 100,
                name: '봉준호',
                role: '감독',
                profilePath: ''
            },
            {
                id: 100,
                name: '봉준호',
                role: '감독',
                profilePath: ''
            },{
                id: 100,
                name: '봉준호',
                role: '감독',
                profilePath: ''
            },{
                id: 100,
                name: '봉준호',
                role: '감독',
                profilePath: ''
            },{
                id: 100,
                name: '봉준호',
                role: '감독',
                profilePath: ''
            },{
                id: 100,
                name: '봉준호',
                role: '감독',
                profilePath: ''
            },{
                id: 100,
                name: '봉준호',
                role: '감독',
                profilePath: ''
            },
        ],
        gallery: galleryImages,
        category: 'movie',
        videos:[
            {
                key: "1mTjfMFyPi8",
                site: "YouTube",
                name: "Spider-Mans Trailer",
                type: "Trailer"
            },
            {
                key: "1mTjfMFyPi8",
                site: "YouTube",
                name: "Spider-Mans Trailer",
                type: "Trailer"
            },
            {
                key: "1mTjfMFyPi8",
                site: "YouTube",
                name: "Spider-Mans Trailer",
                type: "Trailer"
            },
        ],

    },
    {
        id: 102,
        title: '부산행',
        overview: '좀비 바이러스가 확산된 대한민국, 부산행 열차 안에서 생존을 위한 사투가 벌어진다.',
        releaseDate: '2016-07-20',
        posterPath: 'https://image.tmdb.org/t/p/w500/sample2.jpg',
        backdropPath: 'https://image.tmdb.org/t/p/w780/sample2_backdrop.jpg',
        voteAverage: 7.7,
        genres: ['액션', '스릴러'],
        productionCountries: '대한민국',
        casts: [
            {
                id: 3,
                name: '공유',
                role: '석우',
                profilePath: 'https://image.tmdb.org/t/p/w185/sample_actor3.jpg'
            },
            {
                id: 4,
                name: '마동석',
                role: '상화',
                profilePath: 'https://image.tmdb.org/t/p/w185/sample_actor4.jpg'
            }
        ],
        directors: [
            {
                id: 101,
                name: '연상호',
                role: '감독',
                profilePath: 'https://image.tmdb.org/t/p/w185/sample_director2.jpg'
            }
        ],
        category: 'movie'
    },
    {
        id: 103,
        title: '살인의 추억',
        overview: '1980년대 대한민국을 뒤흔든 연쇄살인사건을 다룬 실화 기반의 스릴러. 수사팀의 집요한 추적이 이어진다.',
        releaseDate: '2003-04-25',
        posterPath: 'https://image.tmdb.org/t/p/w500/sample3.jpg',
        backdropPath: 'https://image.tmdb.org/t/p/w780/sample3_backdrop.jpg',
        voteAverage: 8.1,
        genres: ['범죄', '스릴러', '드라마'],
        productionCountries: '대한민국',
        casts: [
            {
                id: 5,
                name: '김상경',
                role: '서형사',
                profilePath: 'https://image.tmdb.org/t/p/w185/sample_actor5.jpg'
            },
            {
                id: 1,
                name: '송강호',
                role: '박형사',
                profilePath: 'https://image.tmdb.org/t/p/w185/sample_actor1.jpg'
            }
        ],
        directors: [
            {
                id: 100,
                name: '봉준호',
                role: '감독',
                profilePath: 'https://image.tmdb.org/t/p/w185/sample_director.jpg'
            }
        ],
        category: 'movie'
    }
];


export default function Home() {
    return (
        <div className="max-w-[1320px] w-full pt-10 m-auto">
            <MovieSlide title="최신 리뷰" movies={dummyMovies} />
            <MovieSlide title="평점 높은 영화" movies={dummyMovies} />
            <MovieSlide title="액션 영화" movies={dummyMovies} />
        </div>
    );
}