import {MovieSlide} from "@/components/main/MovieSlide.jsx";

export const dummyMovies = [
    {
        id: 1,
        title: "기억의 조각들",
        posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg",
        rating: 4.8,
        releaseYear: 2025,
        genre: ["드라마"],
    },
    {
        id: 2,
        title: "마지막 여름",
        posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg",
        rating: 4.3,
        releaseYear: 2024,
        genre: ["로맨스", "드라마"],
    },
    {
        id: 3,
        title: "초능력자들",
        posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg",
        rating: 4.6,
        releaseYear: 2023,
        genre: ["액션", "SF"],
    },
    {
        id: 4,
        title: "코미디의 왕",
        posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg",
        rating: 3.9,
        releaseYear: 2022,
        genre: ["코미디"],
    },
    {
        id: 5,
        title: "공포의 밤",
        posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg",
        rating: 4.1,
        releaseYear: 2024,
        genre: ["공포", "스릴러"],
    },
    {
        id: 6,
        title: "우주 전쟁",
        posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg",
        rating: 4.7,
        releaseYear: 2025,
        genre: ["SF", "액션"],
    },
    {
        id: 7,
        title: "비밀의 숲",
        posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg",
        rating: 4.5,
        releaseYear: 2021,
        genre: ["미스터리", "드라마"],
    },
    {
        id: 8,
        title: "사랑의 시작",
        posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg",
        rating: 3.8,
        releaseYear: 2020,
        genre: ["로맨스"],
    },
    {
        id: 9,
        title: "미래 도시",
        posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg",
        rating: 4.9,
        releaseYear: 2025,
        genre: ["SF"],
    },
    {
        id: 10,
        title: "전설의 검",
        posterUrl: "https://image.tmdb.org/t/p/w500/ww7jn7lv1YzTAGd5m0R6CP1VXAs.jpg",
        rating: 4.2,
        releaseYear: 2023,
        genre: ["액션", "판타지"],
    }
];

export default function Home() {
    return (
        <div className="container m-auto">
            <MovieSlide title="최신 리뷰" movies={dummyMovies} />
            <MovieSlide title="평점 높은 영화" movies={dummyMovies} />
            <MovieSlide title="액션 영화" movies={dummyMovies} />
        </div>
    );
}