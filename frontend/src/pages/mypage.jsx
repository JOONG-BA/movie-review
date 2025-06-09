import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

export default function MyPage() {
    const navigate = useNavigate();
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [showAllReviews, setShowAllReviews] = useState(false);
    const [showAllFavorites, setShowAllFavorites] = useState(false);

    const fetchMyPage = async () => {
        try {
            const token = localStorage.getItem("token");
            if (!token) throw new Error("로그인이 필요합니다.");

            const res = await fetch("http://localhost:8080/api/users/me", {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });
            if (!res.ok) throw new Error(`status ${res.status}`);
            const dto = await res.json();

            const withMovie = await Promise.all(
                dto.recentReviews.map(async (rv) => {
                    try {
                        const mvRes = await fetch(
                            `http://localhost:8080/api/movies/detail/${rv.movieId}`,
                            {
                                headers: {
                                    "Content-Type": "application/json",
                                    Authorization: `Bearer ${token}`,
                                },
                            }
                        );
                        const movie = await mvRes.json();
                        return {
                            ...rv,
                            title: movie.title,
                            posterPath: movie.poster_path || null,
                            createdAt: rv.createdAt || "",
                        };
                    } catch {
                        return rv;
                    }
                })
            );
            const favoritesWithMovieData = await Promise.all(
                dto.favoriteMovies.map(async (fav) => {
                    try {
                        const mvRes = await fetch(
                            `http://localhost:8080/api/movies/detail/${fav.movieId}`,
                            {
                                headers: {
                                    "Content-Type": "application/json",
                                    Authorization: `Bearer ${token}`,
                                },
                            }
                        );
                        const movie = await mvRes.json();
                        return {
                            ...fav,
                            title: movie.title,
                            posterPath: movie.poster_path || null,
                            originalTitle: movie.originalTitle || "",
                            releaseDate: movie.releaseDate || movie.releaseYear || "",
                            genres: Array.isArray(movie.genres) ? movie.genres : [],
                            country: movie.country || "",
                            runtime: movie.runtime || 0,
                        };
                    } catch {
                        // 실패 시 DTO 그대로
                        return {...fav, genres: [], releaseDate: "", country: "", runtime: 0};
                    }
                })
            );


            setProfile({...dto, recentReviews: withMovie, favoriteMovies: favoritesWithMovieData,});
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchMyPage();
    }, []);

    if (loading) return <div className="p-8 text-center">Loading…</div>;
    if (error)
        return (
            <div className="p-8 text-center text-red-600">
                {error}{" "}
                <button onClick={() => navigate("/login")} className="underline">
                    로그인 페이지로
                </button>
            </div>
        );
    if (!profile) return null;

    const displayedReviews = showAllReviews
        ? profile.recentReviews
        : profile.recentReviews.slice(0, 3);

    const displayedFavorites = showAllFavorites
        ? profile.favoriteMovies
        : profile.favoriteMovies.slice(0, 3);

    return (
        <div className="min-h-screen bg-gray-100 pt-24 px-4 flex justify-center">
            <div className="w-full max-w-3xl space-y-8">

                {/* 프로필 카드 */}
                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex flex-col sm:flex-row gap-6 items-center sm:items-start">
                        <div className="w-24 h-24 rounded-full overflow-hidden border border-gray-300">
                            <img
                                src="https://i.pinimg.com/736x/2f/55/97/2f559707c3b04a1964b37856f00ad608.jpg"
                                alt="프로필"
                                className="w-full h-full object-cover"
                            />
                        </div>
                        <div className="text-center pt-4 sm:text-left">
                            <h2 className="text-lg sm:text-xl font-semibold">
                                {profile.nickname}
                            </h2>
                            <p className="text-sm sm:text-base text-gray-600">
                                {profile.email}
                            </p>
                        </div>
                    </div>
                    <div className="grid grid-cols-2 divide-x mt-6 text-center">
                        <div>
                            <p className="text-xs sm:text-sm text-gray-500">평가</p>
                            <p className="text-base sm:text-lg font-semibold">
                                {profile.reviewCount}
                            </p>
                        </div>
                        <div>
                            <p className="text-xs sm:text-sm text-gray-500">즐겨찾기</p>
                            <p className="text-base sm:text-lg font-semibold">
                                {profile.favoriteCount}
                            </p>
                        </div>
                    </div>
                </div>

                {/* 작성한 평가 */}
                <div className="bg-white rounded-lg shadow p-6">
                    <h3 className="text-2xl font-bold mb-4 text-left">작성한 평가</h3>
                    <div className="space-y-4">
                        {displayedReviews.map((review, i) => (
                            <div
                                key={i}
                                className="flex items-start border border-gray-300 rounded-lg overflow-hidden"
                            >
                                <div className="w-32 h-40 p-1 flex-shrink-0">
                                    {review.posterPath ? (
                                        <img
                                            src={review.posterPath}
                                            alt={review.title}
                                            className="w-full h-full object-cover rounded"
                                        />
                                    ) : (
                                        <div className="w-full h-full bg-gray-200 rounded"/>
                                    )}
                                </div>
                                <div className="flex-1 p-4">
                                    <h4 className="text-xl font-semibold text-left">
                                        {review.title}
                                    </h4>
                                    {(review.score != null&&review.score>=1) && (
                                        <div className="flex items-center mt-2">
                                            <span className="text-lg">★</span>
                                            <span className="ml-1 text-lg">{review.score}/5</span>
                                        </div>
                                    )}

                                    <p className="mt-2 text-base leading-relaxed text-left">
                                        {review.content}
                                    </p>
                                    <p className="mt-3 text-sm text-gray-600 text-left">
                                        {profile.nickname} {" "}
                                        {/*  {review.createdAt ? review.createdAt : "작성일 없음"}*/}
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="mt-6 text-center">
                        <button
                            className="px-5 py-2 border rounded-lg hover:bg-gray-50"
                            onClick={() => setShowAllReviews((prev) => !prev)}
                        >
                            {showAllReviews ? "접기" : "더보기"}
                        </button>
                    </div>
                </div>

                {/* 즐겨찾는 영화 */}
                <div className="bg-white rounded-lg shadow p-6">
                    <h3 className="text-2xl font-bold mb-4 text-left">
                        즐겨찾는 영화
                    </h3>
                    {displayedFavorites.length === 0 ? (
                        <p className="text-gray-500 text-sm">
                            즐겨찾기한 영화가 없습니다.
                        </p>
                    ) : (
                        <div className="space-y-4">
                            {displayedFavorites.map((movie, idx) => (
                                <div
                                    key={idx}
                                    className="flex items-start border border-gray-300 rounded-lg overflow-hidden"
                                >
                                    {/* 리뷰 포스터와 동일한 크기로 맞춤 */}
                                    <div className="w-32 h-40 p-1 flex-shrink-0">
                                        {movie.posterPath ? (
                                            <img
                                                src={movie.posterPath}
                                                alt={movie.title}
                                                className="w-full h-full object-cover rounded"
                                            />
                                        ) : (
                                            <div className="w-full h-full bg-gray-200 rounded"/>
                                        )}
                                    </div>
                                    <div className="flex-1 p-4">
                                        {/* 제목 크기와 위치를 리뷰와 동일하게 */}
                                        <h4 className="text-xl font-semibold text-left">
                                            {movie.title}
                                        </h4>
                                        {movie.originalTitle && (
                                            <p className="text-sm text-gray-500 mt-1 text-left">
                                                {movie.originalTitle}
                                            </p>
                                        )}
                                        <p className="text-sm text-gray-500 mt-2 text-left">
                                            {movie.releaseDate}
                                            {movie.genres.length
                                                ? ` · ${movie.genres.join("·")}`
                                                : ""}
                                            {movie.country
                                                ? ` · ${movie.country}`
                                                : ""}
                                        </p>
                                        <p className="text-sm text-gray-500 mt-1 text-left">
                                            {movie.runtime}분
                                        </p>

                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                    <div className="mt-4 text-center">
                        <button
                            className="px-4 py-2 border rounded hover:bg-gray-50 text-sm"
                            onClick={() => setShowAllFavorites((prev) => !prev)}
                        >
                            {showAllFavorites ? "접기" : "더보기"}
                        </button>
                    </div>
                </div>

            </div>
        </div>
    );
}
