import { useState, useEffect, useRef, useCallback } from "react";
import { useSearchParams } from "react-router-dom";
import MovieList from "@/components/moive/search/MovieList.jsx";
import {searchMovies} from "@/pages/api/movieApi.js";
import InlineLoadingSpinner from "@/components/ui/InlineLodingSpinner.jsx";
import LoadingSpinner from "@/components/ui/LoadingSpinner.jsx";

export default function SearchPage() {
    const [searchParams] = useSearchParams();
    const query = searchParams.get("query");

    const [movies, setMovies] = useState([]);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const [loading, setLoading] = useState(false);
    const [initialLoading, setInitialLoading] = useState(true);
    const loaderRef = useRef(null);

    // fetchMovies에서 page를 받도록 변경
    const fetchMovies = useCallback(async (targetPage) => {
        if (!query) return;
        setLoading(true);
        try {

            const newMovies = await searchMovies(query, targetPage);
            if (targetPage === 1) {
                setMovies(newMovies.content);
            } else {
                setMovies((prev) => [...prev, ...newMovies.content]);
            }
            if (newMovies.content.length === 0) {
                setHasMore(false);
            }
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
            setInitialLoading(false);
        }
    }, [query]);

    useEffect(() => {
        setMovies([]);
        setHasMore(true);

        setPage(1);
    }, [query]);

    useEffect(() => {
        fetchMovies(page);
    }, [page, fetchMovies]);

    useEffect(() => {
        if (!hasMore) return;
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting && !loading) {
                    setPage((prev) => prev + 1);
                }
            },
            { threshold: 1 }
        );
        if (loaderRef.current) observer.observe(loaderRef.current);
        return () => {
            if (loaderRef.current) observer.unobserve(loaderRef.current);
        };
    }, [hasMore, loading]);

    if (initialLoading) return <LoadingSpinner />;

    return (
        <div className="container mt-20">
            {!loading && !initialLoading && movies.length === 0 && query &&  (
                <div className="absolute top-0 left-0 right-0 pb-24 flex flex-col items-center justify-center text-center text-gray-500 h-full">
                    <div className="text-5xl mb-3">🎬</div>
                    <p className="text-lg font-semibold">결과에 해당하는 영화가 없습니다</p>
                    <p className="text-sm text-gray-400 mt-1">
                        다른 검색어로 다시 시도해보세요!
                    </p>
                </div>
            )}

            <MovieList movies={movies} />
            {loading && <InlineLoadingSpinner />}
            <div ref={loaderRef} style={{ height: "20px" }} />
            {!loading && hasMore === false && movies.length > 0 &&
                <div className="mt-10 mx-auto w-fit px-4 py-2 rounded-xl bg-gray-100 text-gray-500 text-sm shadow-sm">
                    🎬 더 이상 불러올 영화가 없습니다
                </div>
            }
        </div>
    );
}
