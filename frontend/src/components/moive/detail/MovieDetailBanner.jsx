

export const MovieDetailBanner = ({ movie }) => {
    return (
        <div className="bg-gray-500 h-[30rem] relative">
            <div className="container absolute m-auto bottom-10 left-0 right-0">
                <div className="grid gap-y-4 text-left">
                    <h1 className="text-3xl font-black">{movie.title}</h1>
                    <div className="grid gap-y-1.5 text-sm">
                        <p>{movie.title}</p>
                        <p>{movie.releaseDate}
                        {movie.genres.map((g, idx) => (
                            <span key={idx} className=""> · {g}</span>
                        ))}
                        </p>
                        <p>{movie.productionCountries}</p>
                    </div>
                </div>
            </div>
        </div>
    )
}