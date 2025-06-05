

export const MovieDetailBanner = ({ movie }) => {
    return (
        <div className="relative h-[30rem] bg-black overflow-hidden">
            {/* 백드롭 이미지 (원본 크기, 가운데 정렬) */}
            <div
                className="absolute inset-0 bg-no-repeat bg-top bg-auto"
                style={{
                    backgroundImage: `url(${movie.backdrop_path})`,
                }}
            />
            {/* 양옆 그라데이션 오버레이 */}
            <div className="absolute max-w-[1280px] w-full h-full left-1/2 -translate-x-1/2 top-0 bg-gradient-to-r from-black via-transparent to-black" />

            <div className="container absolute bottom-10 left-0 right-0 text-white z-10">
                <div className="grid gap-y-4 text-left">
                    <h1 className="text-3xl font-black">{movie.title}</h1>
                    <div className="grid gap-y-1.5 text-sm">
                        <p>{movie.originalTitle}</p>
                        <p>
                            {movie.releaseYear}
                            {movie.genres?.map((g, idx) => (
                                <span key={idx}> · {g}</span>
                            ))}
                        </p>
                        <p>{movie.country}</p>
                    </div>
                </div>
            </div>
        </div>
    );
};
